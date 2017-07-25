package news;

import java.io.Serializable;

/**
 * Created by golit on 03.07.2017.
 */
public class PageRequest implements Serializable {
    private String url;
    private String newsName;
    private int size;

    public PageRequest(String url, String newsName, int size) {
        this.url = url;
        this.newsName = newsName;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "url='" + url + '\'' +
                ", newsName='" + newsName + '\'' +
                ", size=" + size +
                '}';
    }
}
