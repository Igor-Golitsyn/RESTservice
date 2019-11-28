package application;

import news.*;
import news.ruTor.RuTorSearch;
import newsList.NewsListController;
import newsList.NewsListItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.ConstantManager;

import java.io.UnsupportedEncodingException;

/**
 * Created by golit on 05.06.2017.
 */
@RestController
public class RestServiceController {

    @RequestMapping("/torrent")
    public NewsItem[] getTorrent() {
        Model model = NewsFabrika.FABRIKA.createNews("torrent");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    //Если сервис с поиском добавляем поиск (имяСервиса+Search)
    @RequestMapping("/torrentSearch")
    public NewsItem[] findTorrent(@RequestBody PageRequest pageRequest) {
        Model model = NewsFabrika.FABRIKA.createNews("torrent");
        try {
            return model == null ? new NewsItem[0] : model.getItems(pageRequest.getUrl());
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/voda")
    public NewsItem[] getVoda() {
        Model model = NewsFabrika.FABRIKA.createNews("voda");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/tokoTebe")
    public NewsItem[] getTokoTebe() {
        Model model = NewsFabrika.FABRIKA.createNews("tokoTebe");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/shkola")
    public NewsItem[] getShkola() {
        Model model = NewsFabrika.FABRIKA.createNews("shkola");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/vodaSearch")
    public NewsItem[] findVoda(@RequestBody PageRequest pageRequest) {
        Model model = NewsFabrika.FABRIKA.createNews("voda");
        try {
            return model == null ? new NewsItem[0] : model.getItems(pageRequest.getUrl());
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/newsList")
    public NewsListItem[] getNewsList() {
        return new NewsListController().getNewsList();
    }

    @RequestMapping("/updateNewsListItem")
    public NewsListItem[] getNewsList(@RequestBody NewsListItem item) {
        if (item == null) new NewsListController().getNewsList();
        return new NewsListController().writeToBase(item);
    }

    @RequestMapping("/gibdd")
    public NewsItem[] getGibdd() {
        Model model = NewsFabrika.FABRIKA.createNews("gibdd");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/31tv")
    public NewsItem[] get31tv() {
        Model model = NewsFabrika.FABRIKA.createNews("31tv");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/mchs74")
    public NewsItem[] getMchsOperational() {
        Model model = NewsFabrika.FABRIKA.createNews("mchs74");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/popMech")
    public NewsItem[] getPopMech() {
        Model model = NewsFabrika.FABRIKA.createNews("popMech");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/cosmo")
    public NewsItem[] getCosmo() {
        Model model = NewsFabrika.FABRIKA.createNews("cosmo");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/vesti")
    public NewsItem[] getVesti() {
        Model model = NewsFabrika.FABRIKA.createNews("vesti");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/newsPage")
    public NewsPage getNewsPage(@RequestBody PageRequest pageRequest) {
        Model model = NewsFabrika.FABRIKA.createNews(pageRequest.getNewsName());
        try {
            return model == null ?
                    new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, null, null, null, null, null) :
                    model.getNewsPage(pageRequest);
        } catch (Exception e) {
            return new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, null, null, null, null, null, null);
        }
    }

    @RequestMapping("/Chelyabinsk74ru")
    public NewsItem[] getChelyabinsk74ru() {
        Model model = NewsFabrika.FABRIKA.createNews("Chelyabinsk74ru");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @RequestMapping("/BashOrg")
    public NewsItem[] getBashOrg() {
        Model model = NewsFabrika.FABRIKA.createNews("BashOrg");
        try {
            return model == null ? new NewsItem[0] : model.getItems("");
        } catch (Exception e) {
            return new NewsItem[0];
        }
    }

    @ResponseBody
    @RequestMapping("/torPage")
    public byte[] gettorPage() {
        RuTorSearch ruTorSearch = new RuTorSearch();
        try {
            return ruTorSearch.getDocumentPage().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return Jsoup.parse("<html></html>").outerHtml().getBytes();
        }
    }
}
