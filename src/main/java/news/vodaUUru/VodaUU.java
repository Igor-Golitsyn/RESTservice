package news.vodaUUru;

import news.Model;
import news.NewsItem;
import news.NewsPage;
import news.PageRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class VodaUU implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        return new NewsItem[0];
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        return null;
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
}
