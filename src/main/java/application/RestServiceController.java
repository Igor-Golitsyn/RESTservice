package application;

import news.*;
import news.gibdd.Gibdd;
import news.mchs.Mchs74;
import newsList.NewsListController;
import newsList.NewsListItem;
import news.tv31.Channel31;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import news.ruTor.RuTorSearch;
import utils.ConstantManager;

/**
 * Created by golit on 05.06.2017.
 */
@RestController
public class RestServiceController {

    @RequestMapping("/torrent")
    public NewsItem[] getTorrent(@RequestParam(value = "word", required = false, defaultValue = "") String word) {
        Model model = new NewsFabrika().createNews("torrent");
        return model == null ? new NewsItem[0] : model.getItems(word);
    }

    @RequestMapping("/newsList")
    public NewsListItem[] getNewsList() {
        return NewsListController.CONTROLLER.getNewsList();
    }

    @RequestMapping("/updateNewsListItem")
    public NewsListItem[] getNewsList(@RequestBody NewsListItem item) {
        if (item == null) NewsListController.CONTROLLER.getNewsList();
        return NewsListController.CONTROLLER.writeToBase(item);
    }

    @RequestMapping("/gibdd")
    public NewsItem[] getGibdd() {
        Model model = new NewsFabrika().createNews("gibdd");
        System.out.println(model);
        return model == null ? new NewsItem[0] : model.getItems("");
    }

    @RequestMapping("/31tv")
    public NewsItem[] get31tv() {
        Model model = new NewsFabrika().createNews("31tv");
        return model == null ? new NewsItem[0] : model.getItems("");
    }

    @RequestMapping("/mchs74")
    public NewsItem[] getMchsOperational() {
        Model model = new NewsFabrika().createNews("mchs74");
        return model == null ? new NewsItem[0] : model.getItems("");
    }

    @RequestMapping("/newsPage")
    public NewsPage getNewsPage(@RequestBody PageRequest pageRequest) {
        Model model = new NewsFabrika().createNews(pageRequest.getClassName());
        return model == null ?
                new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, null, null, null, null, null) :
                model.getNewsPage(pageRequest);
    }
}
