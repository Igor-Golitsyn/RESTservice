package news.shkola;

import news.Model;
import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.ConstantManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class ShkolaOtmena implements Model {
    private DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        NewsItem[] newsItems = new NewsItem[2];
        newsItems[0] = new NewsItem("Администрация Челябинска",ConstantManager.CHELADMINURL,0,"",new Date().getTime());
        newsItems[1] = new NewsItem("Первый Областной",ConstantManager.OBLRU_SCHOOL,0,"",new Date().getTime());
        return newsItems;
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        if (pageRequest.getUrl().equals(ConstantManager.CHELADMINURL)){
            Document document = getDocument(pageRequest.getUrl());
            if (document == null){
                return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, "", "", "", "", "");
            } else {
                String text = document.getElementsByClass("field-items").first().text();
                HashSet<String> hashSet = new HashSet<>();
                hashSet.add(ConstantManager.CHELADMINPIC);
                return new NewsPage("Администрация Отмена занятий",hashSet,text,ConstantManager.OPENINBRAUZER,pageRequest.getUrl(),"","");
            }
        }
        if (pageRequest.getUrl().equals(ConstantManager.OBLRU_SCHOOL)){
            Document document = getDocument(pageRequest.getUrl());
            if (document == null){
                return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, "", "", "", "", "");
            } else {
                String text = document.getElementsByClass("quote_text").first().text();
                HashSet<String> hashSet = new HashSet<>();
                hashSet.add(ConstantManager.CHELADMINPIC);
                return new NewsPage("Первый Областной Отмена занятий",hashSet,text,ConstantManager.OPENINBRAUZER,pageRequest.getUrl(),"","");
            }
        }
        return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, "", "", "", "", "");
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
        ShkolaOtmena shkolaOtmena = new ShkolaOtmena();
        NewsItem[] items = shkolaOtmena.getItems("");
        for (NewsItem item :
                items) {
            System.out.println(item);
            System.out.println(shkolaOtmena.getNewsPage(new PageRequest(item.getLink(),item.getLink())));
        }
    }
}
