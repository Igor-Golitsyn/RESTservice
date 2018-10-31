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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by golit on 13.06.2017.
 */
public class Channel31 implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMMM yyyy - hh:mm");

    @Override
    public NewsItem[] getItems(String searchWord) {
        CopyOnWriteArrayList<NewsItem> newsItems = new CopyOnWriteArrayList<>();
        LinkedList<Thread> threads = new LinkedList<>();
        for (int i = 1; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<NewsItem> items = getNewsItemsFromPage(finalI);
                    for (NewsItem item : items) {
                        boolean find = false;
                        for (NewsItem mainItem : newsItems) {
                            if (item.getLink().equals(mainItem.getLink())) {
                                find = true;
                            }
                        }
                        if (!find) newsItems.add(item);
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
                return Long.compare(o2.getDate(), o1.getDate());
            }
        });
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    private ArrayList<NewsItem> getNewsItemsFromPage(int num){
        Document document = getDocument(ConstantManager.TV31NEWSURL + num);
        if (document == null) return new ArrayList<>();
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        Elements news = document.getElementsByClass("card border-on_grey shadow");
        for (Element element : news) {
            String name = element.child(0).text();
            String url = element.child(1).absUrl("href");
            Date date;
            try {
                synchronized (FORMAT) {
                    date = FORMAT.parse(element.getElementsByClass("text-muted").first().text());
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
        //return new CopyOnWriteArrayList<>(newsItems);
        return newsItems;
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        HashSet<String> setUrlImgs = new HashSet<>();
        String head = document.getElementsByClass("text-center").first().text();
        String text = "";
        Element mediacontainer = document.getElementsByClass("media-container").first().child(0);
        String lnk = mediacontainer.absUrl("src");
        if (lnk.endsWith("jpg")|| lnk.endsWith("jpeg")){
            setUrlImgs.add(lnk);
        }
        Elements ppp = document.getElementsByTag("p");
        for (Element el : ppp) {
            text = text + el.text() + "\n";
        }
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

   /* public static void main(String[] args) {
        Channel31 channel31 = new Channel31();
        NewsItem[] ooo = channel31.getItems("");
        System.out.println(ooo.length);
        for (NewsItem o : ooo) {
            System.out.println(o);
            System.out.println(new Date(o.getDate()));
        }
        PageRequest pageRequest = new PageRequest(ooo[ooo.length-1].getLink(),ooo[0].getName());
        NewsPage page = channel31.getNewsPage(pageRequest);
        System.out.println(page);
    }*/
}
