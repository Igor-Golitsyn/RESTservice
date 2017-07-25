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

/**
 * Created by golit on 13.06.2017.
 */
public class Channel31 implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm");

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
            //DateFormat FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm");
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
        return null;
    }

    private Document getDocument() {
        String url = ConstantManager.TV31NEWSURL;
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
