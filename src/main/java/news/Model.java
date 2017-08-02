package news;

import news.NewsItem;

/**
 * Created by golit on 05.06.2017.
 */
public interface Model {
    NewsItem[] getItems(String searchWord) throws Exception;
    NewsPage getNewsPage(PageRequest pageRequest) throws Exception;
}
