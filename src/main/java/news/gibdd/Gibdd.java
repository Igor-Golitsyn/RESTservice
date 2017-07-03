package news.gibdd;

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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by golit on 21.06.2017.
 */
public class Gibdd implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMMMM yyyy");

    @Override
    public NewsItem[] getItems(String searchWord) {
        String url1 = ConstantManager.GIBDDPAGE1;
        String url2 = ConstantManager.GIBDDPAGE2;
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
        return (newsItems.toArray(new NewsItem[newsItems.size()]));
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        return null;
    }

    private Thread createThreadForScan(String url, CopyOnWriteArrayList<NewsItem> newsItems) {
        Thread thread = new Thread(() -> {
            Document page = getDocument(url);
            if (page != null) {
                newsItems.addAll(getItemsFromPage(page));
            }
        });
        thread.start();
        return thread;
    }

    private ArrayList<NewsItem> getItemsFromPage(Document page) {
        ArrayList<NewsItem> arrayList = new ArrayList<>();
        Elements elements = page.getElementsByClass("news_item_img");
        for (Element element : elements) {
            Element title = element.getElementsByClass("title").first();
            Element dateElem = element.getElementsByClass("news-date").first();
            String name = title.text();
            String url = title.child(0).absUrl("href");
            Date date;
            try {
                synchronized (FORMAT) {
                    date = FORMAT.parse(dateElem.text());
                }
            } catch (ParseException e) {
                date = new Date();
            }
            NewsItem item = new NewsItem(name, url, 0, "", date);
            arrayList.add(item);
        }
        return arrayList;
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

    /*public static void main(String[] args) {
        NewsItem[] newsItems = new Gibdd().getItems("");
        for (NewsItem item: newsItems){
            System.out.println("****************************");
            System.out.println(item);
        }
    }
*/
}
