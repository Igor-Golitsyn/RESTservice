package news.ruTor;

import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import news.Model;
import org.jsoup.select.Elements;
import utils.ConstantManager;

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
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        Element details = document.getElementById("details");
        Element download = document.getElementById("download");
        String head = document.getElementsByTag("h1").first().text();
        //String text = details.text().replaceAll("<br />", "");
        String text = getText(details);
        Elements images = details.getElementsByTag("img");
        HashSet<String> setUrls = new HashSet<>();
        Iterator<Element> iterator = images.iterator();
        while (iterator.hasNext()) {
            String url = iterator.next().absUrl("src");
            for (RuTorPic pic : RuTorPic.values()) {
                if (url.contains(pic.toString().toLowerCase()) && !url.endsWith("gif")) setUrls.add(url);
            }
        }
        download.children().first().remove();
        String torrent = "";
        for (Element el : download.children()) {
            torrent = el.absUrl("href");
            if (!torrent.isEmpty()) break;
        }
        return new NewsPage(head, setUrls, text, ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), ConstantManager.SAVELINK, torrent);
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
                return Long.compare(o2.getDate(), o1.getDate());
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

    private String getText(Element details) {
        List<Element> elementList = details.getElementsByTag("b");
        Map<String, String> data = new HashMap<>();
        StringBuilder rezult = new StringBuilder();
        for (Element element : elementList) {
            try {
                String key = element.text();
                String value = "";
                String[] parts = details.toString().split(element.toString());
                if (parts.length > 1) {
                    String[] subPart = parts[1].split("\\<br\\>", 2);
                    value = subPart[0];
                }
                if (value == null || value.isEmpty() || value.contains("http") || value.contains("href")) {
                    continue;
                }
                if (key != null) data.put(key, clearFromTag(value));
            } catch (Exception e) {
            }
        }
        List<String> set = new ArrayList<>(data.keySet());
        Collections.sort(set);
        for (String s : set) {
            String val = data.get(s).trim();
            if (!val.isEmpty()) rezult = rezult.append(s + val + "\n");
        }
        return rezult.toString();
    }

    private String clearFromTag(String string) {
        string = string.trim();
        if (!string.contains("<")) return string;
        if (string.startsWith("<") && string.endsWith(">")) return "";
        while (string.contains("<")) {
            String[] parts = string.split("\\<", 2);
            if (parts.length > 1) {
                String[] subParts = parts[1].split("\\>", 2);
                string = parts[0] + subParts[1];
            }
        }
        return string;
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

   /* public static void main(String[] args) {
        PageRequest pageRequest = new PageRequest("http://rutor.info/torrent/583336/shadow-warrior-2-deluxe-edition-v-1.1.11.1-2016-pc-licenzija", "gidbb");
        NewsPage newsPage = new RuTorSearch().getNewsPage(pageRequest);
        System.out.println(newsPage);
    }*/
}


