package news.kinopoisk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Kinopoisk {
    LinkedHashMap<String, String> data = new LinkedHashMap<>();
    String url = "";

    public Kinopoisk(String url) {
        this.url = url;
        createData();
    }

    public LinkedHashMap<String, String> getData() {
        return data;
    }

    private void createData() {
        try {
            Document document = getDocument(url);
            Element info = document.getElementsByClass("info").first().children().first();
            for (Element el : info.children()) {
                String key = el.children().first().text();
                String value = el.text().replaceFirst(key, "");
                data.put(key, value);
            }
            Element rating_ball = document.getElementsByClass("rating_ball").first();
            data.put("Рейтинг", rating_ball.text());
            Element descr = document.getElementsByClass("brand_words film-synopsys").first();
            data.put("Описание", descr.text());
            Element popupBigImage = document.getElementsByClass("popupBigImage").first();
            data.put("image",popupBigImage.children().first().attr("src"));
        } catch (IOException e) {
        }

    }

    private Document getDocument(String url) throws IOException {
        Document document = Jsoup.connect(url).timeout(60000).get();
        return document;
    }

    public static void main(String[] args) {
        Kinopoisk kinopoisk = new Kinopoisk("https://www.kinopoisk.ru/film/652692");
        for (String str : kinopoisk.data.keySet()) {
            System.out.println(str + " = " + kinopoisk.data.get(str));
        }
    }
}
