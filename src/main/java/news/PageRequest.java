package news;

import java.io.Serializable;

/**
 * Created by golit on 03.07.2017.
 */
public class PageRequest implements Serializable {
    private String url;
    private String newsName;

    public PageRequest() {
    }

    public PageRequest(String url, String newsName) {
        this.url = url;
        this.newsName = newsName;
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

    @Override
    public String toString() {
        return "PageRequest{" +
                "url='" + url + '\'' +
                ", newsName='" + newsName + '\'' +
                '}';
    }
}
