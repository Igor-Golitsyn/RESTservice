package news;

import java.util.Date;

/**
 * Описывает элемент со страницы
 */
public class NewsItem {
    private String name;
    private String link;
    private int seeders;
    private String size;
    private Date date;

    public NewsItem(String name, String link, int seeders, String size, Date date) {
        this.name = name;
        this.link = link;
        this.seeders = seeders;
        this.size = size;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getSeeders() {
        return seeders;
    }

    public String getSize() {
        return size;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", seeders=" + seeders +
                ", size='" + size + '\'' +
                ", date=" + date +
                '}';
    }
}
