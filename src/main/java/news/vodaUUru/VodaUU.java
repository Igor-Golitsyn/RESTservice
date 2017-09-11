package news.vodaUUru;

import com.google.gson.Gson;
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
import java.util.*;

public class VodaUU implements Model {
    @Override
    public NewsItem[] getItems(String searchWord) throws Exception {
        Document document = getDocument(ConstantManager.VODA_UU_RU);
        String message = document.getElementsByTag("script").get(15).html().split("\n")[1];
        message = message.replaceFirst("objects:", "").trim();
        message = message.substring(0, message.length() - 1);
        HashMap<String, String> strings = searchAndReplaceDate(message);
        Gson gson = new Gson();
        DataVoda dataVoda = gson.fromJson(strings.get("rezult"), DataVoda.class);
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        createItems(dataVoda.date1, newsItems, strings.get("date1"));
        createItems(dataVoda.date2, newsItems, strings.get("date2"));
        createItems(dataVoda.date3, newsItems, strings.get("date3"));
        if (!searchWord.trim().isEmpty()) {
            newsItems.removeIf(newsItem -> !newsItem.getName().toLowerCase().contains(searchWord.toLowerCase()));
        }
        return newsItems.toArray(new NewsItem[newsItems.size()]);
    }

    private void createItems(List<? extends DataVoda.DatesData> list, List<NewsItem> newsItems, String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        int year=new Date().getYear();
        for (DataVoda.DatesData dat : list) {
            Date dateFromData;
            try {
                dateFromData = dateFormat.parse(date);
                dateFromData.setYear(year);
            } catch (ParseException e) {
                dateFromData = new Date();
            }
            newsItems.add(new NewsItem(dat.name, dat.name + "\n" + String.valueOf(dateFromData.getTime()), 0, "", dateFromData.getTime()));
        }
    }

    @Override
    public NewsPage getNewsPage(PageRequest pageRequest) throws Exception {
        int year=new Date().getYear();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        String name = pageRequest.getUrl().split("\n")[0];
        long date = Long.parseLong(pageRequest.getUrl().split("\n")[1]);
        Document document = getDocument(ConstantManager.VODA_UU_RU);
        String message = document.getElementsByTag("script").get(15).html().split("\n")[1];
        message = message.replaceFirst("objects:", "").trim();
        message = message.substring(0, message.length() - 1);
        HashMap<String, String> strings = searchAndReplaceDate(message);
        Gson gson = new Gson();
        String content = "";
        Date date11=dateFormat.parse(strings.get("date1"));
        Date date22=dateFormat.parse(strings.get("date2"));
        Date date33=dateFormat.parse(strings.get("date3"));
        date11.setYear(year);
        date22.setYear(year);
        date33.setYear(year);
        DataVoda dataVoda = gson.fromJson(strings.get("rezult"), DataVoda.class);
        if (date11.getTime() == (date)) {
            content = getContent(dataVoda.date1, name);
        }
        if (date22.getTime() == (date)) {
            content = getContent(dataVoda.date2, name);
        }
        if (date33.getTime() == (date)) {
            content = getContent(dataVoda.date3, name);
        }
        return new NewsPage(name + "\n" + dateFormat.format(new Date(date)), null, content, null, null, null, null);
    }

    private String getContent(List<? extends DataVoda.DatesData> list, String name) {
        for (DataVoda.DatesData dat : list) {
            if (dat.name.equals(name)) {
                return dat.content.replaceAll("<br>", "\n");
            }
        }
        return ConstantManager.ERRORDOWNLOADPAGE;
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

    private HashMap<String, String> searchAndReplaceDate(String string) {
        int[] words = new int[6];
        words[0] = string.indexOf("\"");//начало первого слова
        words[1] = string.indexOf("\"", words[0] + 1);//конец первого слова
        int temp = nextWord(string.indexOf("["), string);
        words[2] = string.indexOf("\"", temp);
        words[3] = string.indexOf("\"", words[2] + 1);
        temp = nextWord(string.indexOf("[", words[3]), string);
        words[4] = string.indexOf("\"", temp);
        words[5] = string.indexOf("\"", words[4] + 1);
        HashMap<String, String> dateMap = new HashMap<>();
        dateMap.put("date1", string.substring(words[0] + 1, words[1]));
        dateMap.put("date2", string.substring(words[2] + 1, words[3]));
        dateMap.put("date3", string.substring(words[4] + 1, words[5]));

        for (String key : dateMap.keySet()) {
            string = string.replace(dateMap.get(key), key);
        }
        dateMap.put("rezult", string);
        return dateMap;
    }

    private int nextWord(int currentChar, String string) {
        char[] chars = string.toCharArray();
        int schet = 0;
        int next = 0;
        for (int i = currentChar; i < chars.length; i++) {
            if (chars[i] == '[') schet++;
            if (chars[i] == ']') schet--;
            if (schet == 0) {
                next = i;
                break;
            }
        }
        return next;
    }

    public static void main(String[] args) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        VodaUU vodaUU = new VodaUU();
        NewsItem[] items = vodaUU.getItems("");
        for (int i = 0; i < items.length; i++) {

        }
        NewsItem item = items[items.length-1];
        System.out.println(item);
        System.out.println(vodaUU.getNewsPage(new PageRequest(item.getLink(),"voda")));

    }
}
