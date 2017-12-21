package news.tokoTebe;

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
import java.util.ArrayList;
import java.util.Date;

public class TokoTebe implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        Document document = getDocument(ConstantManager.TOKO_TEBE);
        if (document == null) {
            return new NewsItem[0];
        }
        newsItems.addAll(getItemsFromDocument(document));
        for (int i = 2; i < 20; i++) {
            String nextLink = ConstantManager.TOKO_TEBE + "_" + i;
            Document nextDoc = getDocument(nextLink);
            if (nextDoc == null) break;
            newsItems.addAll(getItemsFromDocument(nextDoc));
        }
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        return null;
    }

    private ArrayList<NewsItem> getItemsFromDocument(Document document) {
        long date = new Date().getTime();
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        Elements elements = document.getAllElements();
        for (Element el : elements) {
            String newLine = "";
            if (el.className().contains("fl_1")) {
                newLine = (el.child(0).html().replaceAll("<br>", "\n"));
                newLine = clearLine(newLine);
                newLine = newLine.replace(newLine.substring(newLine.indexOf('↑'), newLine.indexOf('↓') + 1), "");
                newLine = newLine.replace("рейтинг:", "");
                newsItems.add(new NewsItem(newLine,null,0,null,date));
            }
        }
        return newsItems;
    }

    private String clearLine(String line) {
        if (line.contains("<") && line.contains(">")) {
            int start = 0;
            int end = 0;
            int sch = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '<') {
                    start = i;
                }
                if (line.charAt(i) == '<') sch++;
                if (line.charAt(i) == '>') sch--;
                if (line.charAt(i) == '>' && sch == 0) {
                    end = i;
                    break;
                }
            }
            if (end > 0) {
                line = line.replace(line.substring(start, end + 1), "");
                return clearLine(line);
            }
        } else return line;
        return "";
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
        try {
           NewsItem[] newsItems= new TokoTebe().getItems("");
           for (NewsItem item:newsItems){
               System.out.println(item);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
