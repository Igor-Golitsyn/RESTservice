package news.ru74;

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

public class Chelyabinsk74ru implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        CopyOnWriteArrayList<NewsItem> newsItems = new CopyOnWriteArrayList<>();
        newsItems.addAll(getNewsItemsFromPage(1));
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return Long.compare(o2.getDate(), o1.getDate());
            }
        });
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        Document document = getDocument(pageRequest.getUrl());
        Elements texts = document.getElementsByClass("article-text");
        StringBuilder text = new StringBuilder();
        Iterator<Element> iterator = texts.iterator();
        while (iterator.hasNext()) {
            text.append(iterator.next().text());
            text.append("\n");
        }
        String title = document.getElementsByAttributeValue("name", "title").first().attr("content");
        HashSet<String> imageLinks = new HashSet<>();
        Elements images = document.getElementsByClass("news-article__image");
        Iterator<Element> elementIterator = images.iterator();
        while (elementIterator.hasNext()) {
            String lnk = elementIterator.next().attr("src");
            if (lnk.endsWith("jpg")|| lnk.endsWith("jpeg")){
                imageLinks.add(lnk);
            }
        }
        NewsPage newsPage = new NewsPage(title, imageLinks, text.toString(), ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), "", "");
        return newsPage;
    }

    private CopyOnWriteArrayList<NewsItem> getNewsItemsFromPage(int num) {
        CopyOnWriteArrayList<NewsItem> list = new CopyOnWriteArrayList<>();
        Document document = getDocument(ConstantManager.CHELYABINSK74RU + num);
        if (document == null) return list;
        Elements elements = document.getElementsByClass("news_title_tape");
        Iterator<Element> itemIterator = elements.iterator();
        Set<Thread> threadSet = new HashSet<>();
        while (itemIterator.hasNext()) {
            Element element = itemIterator.next().getElementsByClass("bl_title").first().children().first();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String title = element.text();
                    String newsUrl = element.absUrl("href");
                    String dateString = getDocument(newsUrl).getElementsByAttributeValue("name", "mediator_published_time").first().attr("content");
                    Date date;
                    try {
                        synchronized (FORMAT) {
                            date = FORMAT.parse(dateString);
                        }
                    } catch (Exception e) {
                        date = new Date();
                    }
                    list.add(new NewsItem(title, newsUrl, 0, "", date.getTime()));
                }
            });
            threadSet.add(thread);
            thread.start();
        }
        for (Thread th : threadSet) {
            try {
                th.join();
            } catch (InterruptedException e) {
            }
        }
        return list;
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

    public static void main(String[] args) throws Exception {
        Chelyabinsk74ru chelyabinsk74ru = new Chelyabinsk74ru();
        NewsItem[] ooo = chelyabinsk74ru.getItems("");
        System.out.println(ooo.length);
        for (NewsItem o : ooo) {
            System.out.println(o);
            System.out.println(new Date(o.getDate()));

            PageRequest pageRequest = new PageRequest(o.getLink(), o.getName());
            NewsPage page = chelyabinsk74ru.getNewsPage(pageRequest);
            System.out.println(page);
        }
        /*PageRequest pageRequest = new PageRequest(ooo[0].getLink(), ooo[0].getName());
        NewsPage page = chelyabinsk74ru.getNewsPage(pageRequest);
        System.out.println(page);*/
    }
}
