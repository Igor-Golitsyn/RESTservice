package news.tv31;

import news.Model;
import news.NewsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by golit on 13.06.2017.
 */
public class Channel31 implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) {
        Document document = getDocument();
        if (document == null) return new NewsItem[0];
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        Elements arhive = document.getElementsByClass("archive row-fluid");
        Elements news = arhive.first().getElementsByClass("text");
        for (Element element : news) {
            String name = element.getElementsByClass("title").first().text();
            String url = element.getElementsByClass("title").first().child(0).absUrl("href");
            DateFormat format = new SimpleDateFormat("dd.MM.yy hh:mm");
            Date date;
            try {
                date = format.parse(element.getElementsByClass("date").first().text());
            } catch (ParseException e) {
                date = new Date();
            }
            newsItems.add(new NewsItem(name, url, 0, "", date));
        }
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        System.out.println(newsItems);
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    private Document getDocument() {
        String url = "https://31tv.ru/novosti";
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            document = null;
        }
        return document;
    }
    /*public static void main(String[] args) {
        new Channel31().getItems("");
    }*/
}
