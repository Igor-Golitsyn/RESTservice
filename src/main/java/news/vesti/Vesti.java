package news.vesti;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Vesti implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        Document document = getDocument(ConstantManager.VESTI);
        if (document == null) return new NewsItem[0];
        //System.out.println(document);
        Elements elements = document.getElementsByClass("b-item_list item");
        ArrayList<NewsItem> newsItemArrayList = new ArrayList<>();
        for (Element el : elements) {
            try {
                String title = el.getElementsByClass("b-item__title").text();
                String link = el.getElementsByClass("b-item__title").first().getElementsByAttribute("href").first().absUrl("href");
                String timeStr = el.getElementsByClass("b-item__time").text();
                long date;
                try {
                    date = FORMAT.parse(timeStr).getTime();
                } catch (ParseException e) {
                    date = new Date().getTime();
                }
                newsItemArrayList.add(new NewsItem(title, link, 0, "", date));
            } catch (Exception e) {
            }
        }
        return newsItemArrayList.toArray(new NewsItem[newsItemArrayList.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        String text = "";
        try {
            text = text + document.getElementsByClass("article__text").text();
        } catch (Exception e) {
        }
        try {
            text = text + document.getElementById("Article").text();
        } catch (Exception e) {
        }
        try {
            text = text + document.getElementsByClass("article-text").text();
        } catch (Exception e) {
        }
        try {
            text = text + document.getElementsByClass("content_text").text();
        } catch (Exception e) {
        }
        try {
            text = text + document.getElementsByClass("article-content-main clearfix").text();
        } catch (Exception e) {
        }
        try {
            text = text + document.getElementsByClass("b-doc__body").text();
        } catch (Exception e) {
        }
        text = text.isEmpty() ? document.text() : text;
        String title = document.title();

        HashSet<String> images = new HashSet<>();
        Elements elements;
        try {
            elements = document.getElementsByClass("article-photo").first().getElementsByAttribute("src");
            for (Element el : elements) {
                images.add(el.absUrl("src"));
            }
        } catch (Exception e) {
        }
        try {
            elements = document.getElementsByAttribute("content");
            for (Element el : elements) {
                String url = el.absUrl("content");
                if (url.endsWith("jpg")) {
                    images.add(url);
                }
            }
        } catch (Exception e) {
        }
        return new NewsPage(title,images,text,ConstantManager.OPENINBRAUZER,pageRequest.getUrl(),"","");
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
        Vesti vesti = new Vesti();
        try {
            NewsItem[] newsItems = vesti.getItems("");
            for (int i = 0; i < newsItems.length; i++) {
                System.out.println(newsItems[i]);
                System.out.println(vesti.getNewsPage(new PageRequest(newsItems[i].getLink(), "vest")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //System.out.println(vesti.getNewsPage(new PageRequest("https://auto.vesti.ru/news/show/news_id/693832/", "vest")));
        } catch (Exception e) {
        }
    }*/
}
