package news.cosmo;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cosmo implements Model {
    private final DateFormat FORMAT_LONG = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final DateFormat FORMAT_SMALL = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        Document startDocument = getDocument(ConstantManager.COSMO);
        if (startDocument == null) return new NewsItem[0];
        Elements elements = startDocument.getElementsByClass("discussed-news-list");
        elements.addAll(startDocument.getElementsByClass("mobile-article-list article-list"));
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        for (Element elBig : elements) {
            for (Element el : elBig.children()) {
                String link = el.getElementsByAttribute("href").first().absUrl("href");
                String title = el.getElementsByAttribute("src").attr("alt");
                String timeStr = el.getElementsByAttribute("datetime").attr("datetime");
                if (timeStr.isEmpty()) {
                    String tempStr = el.getElementsByAttribute("href").last().attr("href");
                    Pattern pattern = Pattern.compile("[0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]");
                    Matcher matcher = pattern.matcher(tempStr);
                    if (matcher.find()) {
                        tempStr = tempStr.substring(matcher.start(), matcher.end());
                        timeStr = FORMAT_LONG.format(FORMAT_SMALL.parse(tempStr));
                    } else timeStr = FORMAT_LONG.format(new Date());
                }
                long date = FORMAT_LONG.parse(timeStr).getTime();
                newsItems.add(new NewsItem(title, link, 0, "", date));
            }
        }
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null)
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
        String titleDoc = document.head().getElementsByTag("title").first().text();
        String text = document.getElementsByClass("the-other-article-div").text();
        HashSet<String> images = new HashSet<>();
        Elements picElem = document.getElementsByClass("article-pic image-count-1");
        picElem.addAll(document.getElementsByClass("article-img-div"));
        for (Element el :picElem) {
            try {
                images.add(el.getElementsByAttribute("src").first().absUrl("src"));
            } catch (Exception e) {
            }
        }
        return new NewsPage(titleDoc.replaceAll("\\|","\n"),images,text,ConstantManager.OPENINBRAUZER,pageRequest.getUrl(),"","");
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

    /*public static void main(String[] args) {
        Cosmo cosmo = new Cosmo();
        try {
            NewsItem[] newsItems = cosmo.getItems("");
            for (NewsItem item : newsItems) {
                System.out.println(item);
                NewsPage newsPage = cosmo.getNewsPage(new PageRequest(item.getLink(), ""));
                System.out.println(newsPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
