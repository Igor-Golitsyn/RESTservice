package news;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Описывает элемент со страницы
 */
public class NewsItem {
    private String name;
    private String link;
    private int seeders;
    private String size;
    private long date;

    public NewsItem(String name, String link, int seeders, String size, long date) {
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

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", seeders=" + seeders +
                ", size='" + size + '\'' +
                ", date=" + new Date(date) +
                '}';
    }
}
