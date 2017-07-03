package news;

import news.gibdd.Gibdd;
import news.mchs.Mchs74;
import news.ruTor.RuTorSearch;
import news.tv31.Channel31;

/**
 * Created by golit on 03.07.2017.
 */
public enum NewsFabrika {
    NEWS_FABRIKA;

    public Model createNews(String name) {
        switch (name) {
            case "gibdd":
                return new Gibdd();
            case "31tv":
                return new Channel31();
            case "mchs74":
                return new Mchs74();
            case "torrent":
                return new RuTorSearch();
            default:
                return null;
        }
    }
}
