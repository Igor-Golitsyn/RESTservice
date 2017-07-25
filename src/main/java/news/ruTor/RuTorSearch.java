package news.ruTor;

import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import news.Model;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by golit on 05.06.2017.
 */
public class RuTorSearch implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMM yy");

    /**
     * Возвращает массив раздач
     *
     * @param word
     * @return NewsItem[]
     */
    @Override
    public NewsItem[] getItems(String word) {
        word = word.trim();
        word = word.replaceAll(" ", "%20");
        return word.length() > 2 ? getItemsFromWord(word) : getSartItems();
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        return null;
    }

    /**
     * Возвращает массив раздач стартовой страница
     *
     * @return NewsItem[]
     */
    private NewsItem[] getSartItems() {
        List<NewsItem> newsItems = getItemsFromDocument(getStartDocument());
        newsItems.sort(new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return Long.compare(o2.getDate(),o1.getDate());
            }
        });
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    /**
     * Возвращает стартовую страницу
     *
     * @return Document
     */
    private Document getStartDocument() {
        Document document = new Document("");
        for (RutorMirrors mirror : RutorMirrors.values()) {
            try {
                document = Jsoup.connect(mirror.toString()).get();
            } catch (IOException e) {
            }
        }
        return document;
    }

    /**
     * Возвращает Список раздач со страницы
     *
     * @param document
     * @return List<NewsItem>
     */
    private List<NewsItem> getItemsFromDocument(Document document) {
        List<Element> elements = new ArrayList<>();
        List<NewsItem> newsItems = new ArrayList<>();
        elements.addAll(document.getElementsByClass("gai"));
        elements.addAll(document.getElementsByClass("tum"));
        for (Element element : elements) {
            int sizeNode = element.childNodeSize();
            String name = element.child(1).child(2).text();
            String size = sizeNode < 7 ? element.child(2).text() : element.child(3).text();
            String link = element.child(1).child(2).absUrl("href");
            String seed = element.getElementsByClass("green").get(0).text();
            String stringDate = element.child(0).text();
            stringDate = element.child(0).text().replaceAll(String.valueOf(stringDate.charAt(2)), " ");
            //DateFormat FORMAT = new SimpleDateFormat("dd MMM yy");
            Date date;
            try {
                synchronized (FORMAT) {
                    date = FORMAT.parse(stringDate);
                }
            } catch (ParseException e) {
                date = new Date();
            }
            seed = seed.substring(1, seed.length());
            int seeders = Integer.parseInt(seed);
            if (seeders > 0) newsItems.add(new NewsItem(name, link, seeders, size, date.getTime()));
        }
        return newsItems;
    }

    /**
     * Возвращает запрашиваемую страницу по номеру
     *
     * @param searchString
     * @param page
     * @return Document
     */
    private Document getPage(String searchString, int page) {
        for (RutorMirrors mirror : RutorMirrors.values()) {
            String urlFormat = mirror.toString() + "/search/%d/0/000/0/%s";
            String url = URI.create(String.format(Locale.getDefault(), urlFormat, page, searchString)).toASCIIString();
            Document document;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                document = new Document("");
            }
            return document;
        }
        return new Document("");
    }

    /**
     * Возвращает массив раздач по слову
     *
     * @param word
     * @return
     */
    private NewsItem[] getItemsFromWord(String word) {
        CopyOnWriteArrayList<NewsItem> newsItems = new CopyOnWriteArrayList<>();
        Document document0 = getPage(word, 0);
        newsItems.addAll(getItemsFromDocument(document0));
        int pages;
        try {
            Element element = document0.getElementById("index");
            pages = element.getAllElements().get(1).getAllElements().size();
        } catch (Exception e) {
            pages = 0;
        }
        ArrayList<Thread> runnables = new ArrayList<>();
        for (int i = 1; i < pages; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                Document page = getPage(word, finalI);
                newsItems.addAll(getItemsFromDocument(page));
            });
            runnables.add(thread);
            thread.start();
        }
        for (Thread runner : runnables) {
            try {
                runner.join();
            } catch (InterruptedException e) {
            }
        }
        newsItems.sort((o1, o2) -> Integer.compare(o2.getSeeders(), o1.getSeeders()));
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    /*public static void main(String[] args) {
        RuTorSearch ruTorSearch = new RuTorSearch();
        NewsItem item[] = ruTorSearch.getItems("Дредд");
        for (NewsItem i : item) {
            System.out.println(i);
        }
        System.out.println(item.length);
    }*/
}


