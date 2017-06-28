package application;

import news.gibdd.Gibdd;
import news.mchs.Mchs74;
import newsList.NewsListController;
import newsList.NewsListItem;
import news.tv31.Channel31;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import news.NewsItem;
import news.Model;
import news.ruTor.RuTorSearch;

/**
 * Created by golit on 05.06.2017.
 */
@RestController
public class RestServiceController {

    @RequestMapping("/torrent")
    public NewsItem[] getTorrent(@RequestParam(value = "word", required = false, defaultValue = "") String word) {
        Model rutor = new RuTorSearch();
        return rutor.getItems(word);
    }

    @RequestMapping("/newsList")
    public NewsListItem[] getNewsList() {
        return NewsListController.CONTROLLER.getNewsList();
    }

    @RequestMapping("/gibdd")
    public NewsItem[] getGibdd() {
        Model gibdd = new Gibdd();
        return gibdd.getItems("");
    }

    @RequestMapping("/31tv")
    public NewsItem[] get31tv() {
        Model channel31 = new Channel31();
        return channel31.getItems("");
    }
    @RequestMapping("/mchs74")
    public NewsItem[] getMchsOperational() {
        Model mchs = new Mchs74();
        return mchs.getItems("");
    }

}
