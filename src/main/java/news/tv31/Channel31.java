package news.tv31;

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
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by golit on 13.06.2017.
 */
public class Channel31 implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm");

    @Override
    public NewsItem[] getItems(String searchWord) {
        Document document = getDocument(ConstantManager.TV31NEWSURL);
        if (document == null) return new NewsItem[0];
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        Elements arhive = document.getElementsByClass("archive row-fluid");
        Elements news = arhive.first().getElementsByClass("text");
        for (Element element : news) {
            String name = element.getElementsByClass("title").first().text();
            String url = element.getElementsByClass("title").first().child(0).absUrl("href");
            Date date;
            try {
                synchronized (FORMAT) {
                    date = FORMAT.parse(element.getElementsByClass("date").first().text());
                }
            } catch (ParseException e) {
                date = new Date();
            }
            newsItems.add(new NewsItem(name, url, 0, "", date.getTime()));
        }
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return Long.compare(o2.getDate(),o1.getDate());
            }
        });
        System.out.println(newsItems);
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        Element element = document.getElementsByClass("text article_text").first();
        String head = element.getElementsByTag("p").first().text();
        element.getElementsByTag("p").first().remove();
        Elements imagesEl = element.getElementsByTag("img");
        HashSet<String> setUrlImgs = new HashSet<>();
        for (Element im:imagesEl){
            setUrlImgs.add(im.absUrl("src"));
        }
        String text = element.text();
        return new NewsPage(head,setUrlImgs,text,ConstantManager.OPENINBRAUZER,pageRequest.getUrl(),"","");
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
        PageRequest pageRequest = new PageRequest("https://31tv.ru/novosti/yuzhnouralec-otkazalsya-oplachivat-shtraf--nazyvaya-sebya-grazhdaninom-sssr-i-zhivorozhdennym-muzhchinoy-1-8-2017-143020.html", "");
        NewsPage newsPage=new Channel31().getNewsPage(pageRequest);
        System.out.println(newsPage);
    }*/
}
