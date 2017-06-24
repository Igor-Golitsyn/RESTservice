package newsList;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Описывает новостной раздел
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "NewsListItem.getAll", query = "SELECT c from NewsListItem c"),
        @NamedQuery(name = "NewsListItem.find", query = "SELECT c from NewsListItem c where c.name=:name")
})
public class NewsListItem implements Serializable {
    private static final long serialVersionUID = -3239860594324151192L;
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String restLink;
    private boolean showNewsList;//Определяет видимость раздела 0/1
    private boolean typeFinder; //Определяет есть ли поиск в этом разделе 0/1

    public NewsListItem() {
    }

    public NewsListItem(String name, String restLink, boolean showNewsList, boolean typeFinder) {
        this.name = name;
        this.restLink = restLink;
        this.showNewsList = showNewsList;
        this.typeFinder = typeFinder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestLink() {
        return restLink;
    }

    public void setRestLink(String restLink) {
        this.restLink = restLink;
    }

    public boolean isShowNewsList() {
        return showNewsList;
    }

    public void setShowNewsList(boolean showNewsList) {
        this.showNewsList = showNewsList;
    }

    public boolean isTypeFinder() {
        return typeFinder;
    }

    public void setTypeFinder(boolean finderType) {
        this.typeFinder = finderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsListItem that = (NewsListItem) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NewsListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", restLink='" + restLink + '\'' +
                ", showNewsList=" + showNewsList +
                ", typeFinder=" + typeFinder +
                '}';
    }
}
