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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new BufferedImage[0], "", "", "", "", "");
        Element elementD = document.getElementById("content");
        Elements elements = elementD.getElementsByClass("news-detail");
        Elements fotos = elements.first().getElementsByClass("detail_pics");
        fotos.addAll(elements.first().getElementsByClass("detail_pics hidden-content"));
        elements = elements.first().children();
        elements.removeAll(fotos);
        String newsHeader = elements.get(0).text();
        String newsText = elements.get(2).text();
        return new NewsPage(newsHeader, getImages(fotos, pageRequest.getSize()), newsText, ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), "", "");
    }

    private BufferedImage[] getImages(Elements fotos, int wSize) {
        CopyOnWriteArrayList<BufferedImage> images = new CopyOnWriteArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        Set<String> urls = new HashSet<>();
        for (Element element : fotos) {
            urls.add(element.absUrl("href"));
        }
        for (String url : urls) {
            Thread thread = new Thread(new Runnable() {
                String urlThread = url;

                @Override
                public void run() {
                    BufferedImage image = null;
                    try {
                        URL url = new URL(urlThread);
                        image = ImageIO.read(url);
                        System.out.println(image.getWidth() + " ---" + image.getHeight());
                    } catch (Exception e) {
                    }
                    if (image != null) {
                        if (image.getHeight() > wSize || image.getWidth() > wSize) {
                            double corrector = (double) (image.getHeight()) /(double) image.getWidth();
                            int hSize = (int) (wSize * corrector);
                            int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
                            BufferedImage resizedImage = new BufferedImage(wSize, hSize, type);
                            Graphics2D g = resizedImage.createGraphics();
                            g.drawImage(image, 0, 0, wSize, hSize, null);
                            g.dispose();
                            images.add(resizedImage);
                        } else {
                            images.add(image);
                        }
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread tr : threads) {
            try {
                tr.join();
            } catch (InterruptedException e) {
            }
        }
        return images.toArray(new BufferedImage[images.size()]);
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
        NewsPage newsPage = new Gibdd().getNewsPage(new PageRequest("http://www.gibdd.ru/r/74/accident/3402350/", "Gibdd", 720));
        System.out.println("**************************");
        for (BufferedImage im :
                newsPage.getImages()) {
            System.out.println(im.getWidth() + "---" + im.getHeight());
        }
        System.out.println(newsPage);
    }*/
}
