package news.bashOrg;

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
import java.util.Date;
import java.util.Iterator;

public class BashOrg implements Model {
    private DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        Document document = getDocument(ConstantManager.BASHORG);
        if (document == null) return new NewsItem[0];
        ArrayList<NewsItem> list = new ArrayList<>();
        Elements elements = document.getElementsByClass("q");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String text = element.getElementsByClass("quote").text();
            String link = element.getElementsByClass("vote").first().child(0).absUrl("href");
            String dateStirng = element.getElementsByClass("vote").first().text().split("\\|")[1].trim();
            Date date;
            try {
                date = dateFormat.parse(dateStirng);
            } catch (ParseException e) {
                date = new Date();
            }
            list.add(new NewsItem(text, link, 0, "", date.getTime()));
        }
        return list.toArray(new NewsItem[list.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        Document document = getDocument(pageRequest.getUrl());
        if (document == null) return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, "", "", "", "", "");
        String text = document.getElementsByClass("quote").text();
        String title = ConstantManager.BASHORG;
        return new NewsPage(title, null, text, ConstantManager.OPENINBRAUZER, pageRequest.getUrl(), "", "");
    }

    private Document getDocument(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            document = null;
        }
        return document;
    }

    /*public static void main(String[] args) throws Exception {
        BashOrg bashOrg = new BashOrg();
        *//*NewsItem[] items = bashOrg.getItems("");
        for (NewsItem iii : items) {
            System.out.println(iii);
        }*//*
        System.out.println(bashOrg.getNewsPage(new PageRequest("http://www.bashorg.org/quote/64077", "dfsdfdsfds")));
    }*/
}
