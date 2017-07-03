package news;

/**
 * Created by golit on 03.07.2017.
 */
public class PageRequest {
    private String url;
    private String className;
    private int size;

    public PageRequest(String url, String className, int size) {
        this.url = url;
        this.className = className;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
