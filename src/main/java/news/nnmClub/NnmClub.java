package news.nnmClub;

import news.Model;
import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.ConstantManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NnmClub implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {

        return new NewsItem[0];
    }

    private NewsItem[] getItemsFromRSS() {
        Document document = new Document("");
        try {
            document = getDocFromProxy(ConstantManager.NNMCLUBRSS);
            System.out.println(document);
            ArrayList<NewsItem> newsItems = new ArrayList<>();
            System.out.println(document.getElementsByTag("item").size());
            for (Element el : document.getElementsByTag("item")) {
                String name = el.getElementsByTag("title").text();
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NewsItem[0];
    }

    private NewsItem[] getItemsFromPage(String url){
        Document document = new Document("");
        try {
            document = getDocFromProxy(ConstantManager.NNMCLUB);
            Elements elements = document.getElementsByTag("tbody");
            System.out.println(elements.size());
            for (Element el:elements){
                System.out.println("***********************************");
                System.out.println(el);
                System.out.println("***********************************");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NewsItem[0];
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        return null;
    }

    private Document getDocFromProxy(String urlParse) throws IOException {
        URL url = new URL(urlParse);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ConstantManager.PROXY_ADR, ConstantManager.PROXY_PORT)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
        uc.connect();
        String line = null;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "windows-1251"));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        Document doc = Jsoup.parse(String.valueOf(tmp));
        return doc;
    }

    public static void main(String[] args) {
        NnmClub nnmClub = new NnmClub();
        nnmClub.getItemsFromPage(ConstantManager.NNMCLUB);
    }
}
