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
import utils.FTPFunctions;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


/**
 * Created by golit on 05.06.2017.
 */
public class RuTorSearch implements Model {
    private final DateFormat FORMAT = new SimpleDateFormat("dd MMM yy");
    private final int SIZE_OF_REZULT = 500;
    private String workMiror = "";

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
        NewsItem[] rezult = word.length() > 2 ? getItemsFromWord(word) : getSartItems();
        if (rezult.length > SIZE_OF_REZULT) {
            NewsItem[] rezult500 = new NewsItem[SIZE_OF_REZULT];
            for (int i = 0; i < SIZE_OF_REZULT; i++) {
                rezult500[i] = rezult[i];
            }
            rezult = rezult500;
        }
        return rezult;
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) {
        String presentationUrl = pageRequest.getUrl();
        for (RutorMirrors mirror : RutorMirrors.values()) {
            presentationUrl = presentationUrl.replace(mirror.toString(), RutorMirrors.rutor2.toString());
        }
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return ConstantManager.ERROR_PAGE;
        Element details = document.getElementById("details");
        Element download = document.getElementById("download");
        String head = document.getElementsByTag("h1").first().text();
        //String text = details.text().replaceAll("<br />", "");
        String text = getText(details);
        HashSet<String> setUrls = getImages(details);
        /*Elements images = details.getElementsByTag("img");
        try {
            Elements hidElems = document.getElementsByClass("hidearea");
            for (Element hidElem : hidElems) {
                Document doc = Jsoup.parse(hidElem.text());
                images.addAll(doc.getElementsByAttribute("src"));
                //text = text + "\n" + doc.text();
            }
        } catch (Exception e) {
        }
        HashSet<String> setUrls = new HashSet<>();
        Iterator<Element> iterator = images.iterator();
        while (iterator.hasNext()) {
            String url = iterator.next().absUrl("src");
            for (RuTorPic pic : RuTorPic.values()) {
                if (url.contains(pic.toString().toLowerCase()) && !url.endsWith("gif")) setUrls.add(url);
            }
        }*/
        //download.children().first().remove();
        String torrent = download.children().first().attr("href");
        /*for (Element el : download.children()) {
            torrent = el.absUrl("href");
            if (!torrent.isEmpty()) break;
        }*/
        //pageRequest.setUrl(pageRequest.getUrl().replace(workMiror,RutorMirrors.rutor2.toString()));
        //return new NewsPage(head, setUrls, text, ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), ConstantManager.SAVELINK, torrent);
        return new NewsPage(head, setUrls, text, ConstantManager.OPENINBRAUZER, presentationUrl, ConstantManager.MAGNET, torrent);
        //return new NewsPage(head, setUrls, text, "", "", ConstantManager.MAGNET, torrent);
    }

    private HashSet<String> getImages(Element details) {
        Elements images = details.getElementsByTag("img");
        HashSet<String> setUrls = new HashSet<>();
        for (Element el : images) {
            if (el.attributes().hasKey("style")) setUrls.add(el.absUrl("src"));
        }
        if (setUrls.isEmpty()) {
            Iterator<Element> iterator = images.iterator();
            while (iterator.hasNext()) {
                String url = iterator.next().absUrl("src");
                for (RuTorPic pic : RuTorPic.values()) {
                    if (url.contains(pic.toString().toLowerCase()) && !url.endsWith("gif")) setUrls.add(url);
                }
            }
        }
        return setUrls;
    }

    private Document getDocument(String url) {
        Document document;
        try {
            //document = Jsoup.connect(url).proxy("192.168.0.1",9050).timeout(60000).get();
            document = getDocFromProxy(url);
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
        for (RutorMirrors mirror : RutorMirrors.values()) {
            try {
                //document = Jsoup.connect(mirror.toString()).proxy("192.168.0.1",9050).timeout(60000).get();
                Document document = getDocFromProxy(mirror.toString());
                if (document.title().contains("rutor")) {
                    workMiror = mirror.toString();
                    if (workMiror.endsWith("/kino")) workMiror = workMiror.replace("/kino","");
                    return document;
                }
            } catch (IOException e) {
            }
        }
        return new Document("");
    }

    private Document getDocFromProxy(String urlParse) throws IOException {
        URL url = new URL(urlParse);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("192.168.0.1", 9050)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
        uc.connect();
        String line = null;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        Document doc = Jsoup.parse(String.valueOf(tmp));
        return doc;
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
            //int sizeNode = element.childNodeSize();
            String name = element.child(1).child(2).text();
            //String size = sizeNode < 7 ? element.child(2).text() : element.child(3).text();
            String size = "";
            for (Element el : element.getAllElements()) {
                if (el.text().endsWith("MB") || el.text().endsWith("GB")) {
                    size = el.text();
                }
            }
            String link = element.child(1).child(2).absUrl("href");
            if (link.isEmpty()) {
                link = workMiror + element.child(1).child(2).attr("href");
            }
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
        String text = "";
        if (details==null)return text;
        Elements elements = details.getElementsByTag("span");
        for (Element el : elements) {
            if (el.attributes().hasKey("style")) text = text + el.text() + "\n";
        }
        text = clearFromTag(text);
        if (text.isEmpty()) {
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
            text = rezult.toString();
        }
        return text;
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
        Document document = new Document("");
        for (RutorMirrors mirror : RutorMirrors.values()) {
            String urlFormat = mirror.toString() + "/search/%d/0/000/0/%s";
            String url = URI.create(String.format(Locale.getDefault(), urlFormat, page, searchString)).toASCIIString();
            try {
                //document = Jsoup.connect(url).proxy("192.168.0.1",9050).timeout(60000).get();
                document = getDocFromProxy(url);
                workMiror = mirror.toString();
                if (workMiror.endsWith("/kino")) workMiror = workMiror.replace("/kino","");
                if (!document.title().contains("rutor")) {
                    continue;
                }
            } catch (IOException e) {
                continue;
            }
            return document;
        }
        return document;
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

    public String getDocumentPage() {
        String rutorPage = "";
        try {
            FTPFunctions ftp = new FTPFunctions(ConstantManager.FtpHost, ConstantManager.FtpPort, ConstantManager.FtpTorrentUser, ConstantManager.FtpTorrentPassword);
            Calendar createdFileDate = ftp.getTimeOfFile(ConstantManager.FtpTorrentFileName, ConstantManager.FtpTorrentDir);
            Calendar currentTime = Calendar.getInstance();
            currentTime.add(Calendar.HOUR_OF_DAY, -4);
            if (createdFileDate.after(currentTime)) {
                rutorPage = ftp.getFileFromFTP(ConstantManager.FtpTorrentFileName);
                ftp.disconnect();
            } else {
                rutorPage = getDocumentPageThreads();
                ftp.uploadFTPFile(rutorPage, ConstantManager.FtpTorrentFileName, ConstantManager.FtpTorrentDir);
                ftp.disconnect();
            }
        } catch (Exception e) {
            rutorPage = getDocumentPageThreads();
        }
        return rutorPage;
    }

    private String getDocumentPageThreads() {
        NewsItem[] startItems = getSartItems();

        /*NewsItem[] small = new NewsItem[2];
        for (int i = 0; i < small.length; i++) {
            small[i] = startItems[i];
        }
        startItems = small;*/

        ArrayList<NewsPage> pages = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(70);
        List<Callable<NewsPage>> tasks = new ArrayList<>();
        for (NewsItem item : startItems) {
            tasks.add(new Callable<NewsPage>() {
                @Override
                public NewsPage call() throws Exception {
                    return getNewsPage(new PageRequest(item.getLink(), ""));
                }
            });
        }
        try {
            List<Future<NewsPage>> rezults = executor.invokeAll(tasks);
            for (Future<NewsPage> fut : rezults) {
                NewsPage page = null;
                try {
                    page = fut.get();
                } catch (ExecutionException e) {
                    page = ConstantManager.ERROR_PAGE;
                }
                if (ConstantManager.ERROR_PAGE != page) pages.add(page);
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        executor.shutdown();
        String rutorPage = createDoc(pages);
        return rutorPage;
    }

    private String createDoc(ArrayList<NewsPage> pages) {
        Document doc = Jsoup.parse("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ru-RU\"></html>");
        doc.head().appendElement("meta").attr("charset", "utf-8").attr("pageEncoding", "utf-8");
        doc.head().appendElement("meta").attr("content", "width=960").attr("name", "viewport");
        doc.title(ConstantManager.TITLE);
        doc.head().appendElement("style").attr("type", "text/css").text(ConstantManager.STYLE);

        Element shadow = doc.body().appendElement("div").addClass("shadow");
        Element block1 = shadow.appendElement("div").addClass("block1").attr("style", "background-color: #f2f2f2;");
        for (NewsPage page : pages) {
            Element block2 = block1.appendElement("div").addClass("block2");
            Element photoInfoTable = block2.appendElement("div").addClass("photoInfoTable");
            Element headerFilm = photoInfoTable.appendElement("div").addClass("headerFilm");
            Element moviename = headerFilm.appendElement("h1").addClass("moviename").text(page.getName());
            Element photoBlock = photoInfoTable.appendElement("div").addClass("photoBlock");
            Element filmImgBox = photoBlock.appendElement("div").addClass("film-img-box");
            for (String imgPage : page.getImages()) {
                filmImgBox.appendElement("img").attr("src", imgPage).attr("itemprop", "image").attr("width", "205");
            }
            Element infoTable = photoInfoTable.appendElement("div").addClass("infoTable");
            Element info = infoTable.appendElement("table").addClass("info");
            Element tbody = info.appendElement("tbody");
            Element tr = tbody.appendElement("tr");
            tr.appendElement("td").addClass("type").text("описание");
            Element td = tr.appendElement("td");
            td.appendElement("div").attr("style", "position: relative").text(page.getText());

            Element movieButtonsContainer = block2.appendElement("div").addClass("movie-buttons-container");
            movieButtonsContainer.appendElement("div").addClass("torrentbutton").attr("style", "").attr("onclick", "location.href='" + page.getButton1Action() + "'").text(page.getButton1Text());
            movieButtonsContainer.appendElement("div").addClass("torrentbutton").attr("style", "").attr("onclick", "location.href='" + page.getButton2Action() + "'").text(page.getButton2Text());
        }

        return doc.outerHtml();
    }

    private void saveDocumentToFile(String data, String file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(data.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String file = "C:\\Temp\\myfile.html";
        RuTorSearch ruTorSearch = new RuTorSearch();
        NewsItem[] newsItems = ruTorSearch.getItems("");
        for (int i = 0; i < 10; i++) {
            System.out.println(newsItems[i]);
            System.out.println(ruTorSearch.getNewsPage(new PageRequest(newsItems[i].getLink(), "")));
        }
        //String str = ruTorSearch.getDocumentPage();
        //System.out.println(str);
        //ruTorSearch.saveDocumentToFile(str,file);
       /* PageRequest request = new PageRequest("http://rutorc6mqdinc4cz.onion/torrent/690460/perspektiva_prospect-2018-web-dlrip-ot-ollandgroup-hdrezka-studio", "torrent");
        //PageRequest request = new PageRequest("http://rutorc6mqdinc4cz.onion/torrent/690649/iobit-malware-fighter-pro-6.6.1.5153-2019-pc", "torrent");
        NewsPage page = ruTorSearch.getNewsPage(request);
        System.out.println(page);*/
        //System.out.println("*********************************************************");
        //System.out.println(ruTorSearch.getDocumentPage());

    }
}

