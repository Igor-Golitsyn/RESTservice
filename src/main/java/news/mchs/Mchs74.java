package news.mchs;

import news.Model;
import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.ConstantManager;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by golit on 28.06.2017.
 */
public class Mchs74 implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMMMM");
    private final DateFormat FORMATCURRENTDAY = new SimpleDateFormat("HH:mm");

    @Override
    public NewsItem[] getItems(String searchWord) {
        Document startDocument = getDocument(ConstantManager.MCHSOPERATIONAL);
        if (startDocument == null) return new NewsItem[0];
        CopyOnWriteArrayList<NewsItem> newsItems = new CopyOnWriteArrayList<>();
        newsItems.addAll(getNewsItems(startDocument));
        Elements urlElements = startDocument.getElementsByClass("pager-center").first().getElementsByAttribute("href");
        ArrayList<Thread> threads = new ArrayList<>();
        for (Element el : urlElements) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Document doc = getDocument(el.absUrl("href"));
                    if (doc != null) {
                        newsItems.addAll(getNewsItems(doc));
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
            }
        }
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return Long.compare(o2.getDate(),o1.getDate());
            }
        });
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        return null;
    }

    private Document getDocument(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            document = null;
        }
        return document;
    }

    private ArrayList<NewsItem> getNewsItems(Document document) {
        ArrayList<NewsItem> items = new ArrayList<>();
        Elements elements = document.getElementsByClass("imnl-item");
        for (Element el : elements) {
            String name = el.getElementsByClass("imnl-title").first().text();
            String url = el.getElementsByClass("imnl-title").first().getElementsByAttribute("href").first().absUrl("href");
            Date date;
            try {
                synchronized (FORMAT) {
                    date = FORMAT.parse(el.getElementsByClass("imn-date").first().text());
                }
                if (date.getMonth() - new Date().getMonth() > 1) date.setYear(new Date().getYear() - 1);
                else date.setYear(new Date().getYear());
            } catch (ParseException e) {
                try {
                    synchronized (FORMATCURRENTDAY) {
                        date = FORMATCURRENTDAY.parse(el.getElementsByClass("imn-date").first().text());
                    }
                    date.setDate(new Date().getDate());
                    date.setYear(new Date().getYear());
                    date.setMonth(new Date().getMonth());
                } catch (ParseException e1) {
                    date = new Date();
                }
            }
            items.add(new NewsItem(name, url, 0, "", date.getTime()));
        }
        return items;
    }

   /* public static void main(String[] args) {
        NewsItem[] newsItems = new Mchs74().getItems("");
        for (NewsItem item:newsItems){
            System.out.println("****************************");
            System.out.println(item);
        }
    }*/
}
