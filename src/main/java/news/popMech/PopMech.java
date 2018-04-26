package news.popMech;

import news.Model;
import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.ConstantManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class PopMech implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMMMM yyyy HH:mm", Locale.getDefault());

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        Document startDocument = getDocument(ConstantManager.POPMECH);
        if (startDocument == null) return new NewsItem[0];
        Elements elements = startDocument.getElementsByClass("announce_box article");
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        for (Element el : elements) {
            String link = (el.getElementsByAttribute("href").first().absUrl("href"));
            String title = (el.getElementsByAttribute("href").first().attr("title"));
            long date = FORMAT.parse(el.getElementsByClass("announce_time").first().text()).getTime();
            newsItems.add(new NewsItem(title, link, 0, "", date));
        }
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        String titleDoc = document.head().getElementsByTag("title").first().text();
        Elements elements = document.getElementsByClass("text-page");
        String text = "";
        for (Element element : elements) {
            text = text.length() == 0 ? element.text() : text + "\n" + element.text();
        }
        Elements picElememts = document.getElementsByClass("img");
        HashSet<String> images = new HashSet<>();
        for (Element el : picElememts) {
            images.add(el.child(0).absUrl("src"));
        }
        NewsPage newsPage = new NewsPage(titleDoc.replaceAll("\\|","\n"), images, text, ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), "", "");
        return newsPage;
    }

    private Document getDocument(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).timeout(60000).get();
        } catch (IOException e) {
            document = null;
        }
        return document;
    }

    /*public static void main(String[] args) {
        PopMech popMech = new PopMech();
        NewsItem[] newsItems = new NewsItem[0];
        try {
            newsItems = popMech.getItems("");
            for (NewsItem item : newsItems) {
                //System.out.println(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NewsPage newsPage = popMech.getNewsPage(new PageRequest("https://www.popmech.ru/science/news-421132-samaya-bolshaya-karta-nashey-galaktiki-za-vsyu-istoriyu-chelovechestva/",""));
            System.out.println(newsPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
