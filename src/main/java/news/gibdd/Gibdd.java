package news.gibdd;

import news.Model;
import news.NewsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by golit on 21.06.2017.
 */
public class Gibdd implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) {
        String url1 = "http://www.gibdd.ru/r/74/accident/?PAGEN_1=1";
        String url2 = "http://www.gibdd.ru/r/74/accident/?PAGEN_1=2";
        CopyOnWriteArrayList<NewsItem> newsItems = new CopyOnWriteArrayList<>();
        ArrayList<Thread> runnables = new ArrayList<>();
        runnables.add(createThreadForScan(url1, newsItems));
        runnables.add(createThreadForScan(url2, newsItems));
        for (Thread runner : runnables) {
            try {
                runner.join();
            } catch (InterruptedException e) {
            }
        }
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return ( newsItems.toArray(new NewsItem[newsItems.size()]));
    }

    private Thread createThreadForScan(String url, CopyOnWriteArrayList<NewsItem> newsItems) {
        Thread thread = new Thread(() -> {
            Document page = getDocument(url);
            if (page != null){
            newsItems.addAll(getItemsFromPage(page));
            }
        });
        thread.start();
        return thread;
    }

    private ArrayList<NewsItem> getItemsFromPage(Document page) {
        ArrayList<NewsItem> arrayList = new ArrayList<>();
        Elements elements = page.getElementsByClass("title");
        for (Element element:elements){
            System.out.println("***************************************");
            System.out.println(element);
        }
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

    public static void main(String[] args) {
        new Gibdd().getItems("");
    }

}
