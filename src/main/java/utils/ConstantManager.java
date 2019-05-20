package utils;

import news.NewsPage;

import java.util.HashSet;

/**
 * Created by golit on 05.06.2017.
 */
public interface ConstantManager {
    String VODA_UU_RU = "http://voda.uu.ru/otklyucheniya/";
    String TV31NEWSURL = "https://31tv.ru/glavnye-novosti/page/";
    //String CHELYABINSK74RU = "http://chelyabinsk.74.ru/text/newsline/?p=";
    //String CHELYABINSK74RU = "https://74.ru/text/gorod/?p=";
    String CHELYABINSK74RU = "https://74.ru/text";
    String BASHORG = "http://www.bashorg.org/";
    String GIBDDPAGE1 = "http://www.gibdd.ru/r/74/accident/?PAGEN_1=1";
    String GIBDDPAGE2 = "http://www.gibdd.ru/r/74/accident/?PAGEN_1=2";
    String POPMECH = "https://www.popmech.ru/news";
    String COSMO = "https://www.cosmo.ru/news";
    String VESTI = "https://www.vesti.ru/news";
    String MCHSOPERATIONAL = "http://74.mchs.gov.ru/operationalpage/operational";
    String ERRORDOWNLOADPAGE = "Ошибка загрузки";
    String OPENINBRAUZER = "Открыть в браузере";
    String SAVELINK = "Сохранить torrent ссылку";
    String MAGNET = "Magnet ссылка";
    String TOKO_TEBE = "http://toko-tebe.ru/day/new-year/good";
    NewsPage ERROR_PAGE = new NewsPage(ConstantManager.ERRORDOWNLOADPAGE, new HashSet<>(), "", "", "", "", "");
    String PROXY_ADR = "192.168.0.1";
    int PROXY_PORT = 9050;
    int PROXY_HTTP_PORT = 8118;
    String NNMCLUB = "http://nnmclub5toro7u65.onion";
    String NNMCLUBRSS = NNMCLUB + "/forum/rssp.xml";

    int FtpPort = 21;
    String FtpHost = "192.168.0.1";
    String FtpTorrentUser = "torrent";
    String FtpTorrentPassword = "torrent";
    String FtpTorrentFileName = "rutorPage.html";
    String FtpTorrentDir = "/";

    String TITLE = "Новинки RUTOR";
    String STYLE = "html {\n" +
            "      background-color: #e6e6e6;\n" +
            "      min-width: 1024px;\n" +
            "      width: 100%;\n" +
            "      position: relative;\n" +
            "  }\n" +
            "\n" +
            "  body {\n" +
            "      background: #e6e6e6;\n" +
            "      color: #333;\n" +
            "      font-family: tahoma,verdana,arial;\n" +
            "      margin: 0;\n" +
            "      padding: 0 0 22px 0;\n" +
            "  }\n" +
            "\n" +
            "  * {\n" +
            "      outline: 0;\n" +
            "  }\n" +
            "\n" +
            "  .shadow {\n" +
            "      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.2);\n" +
            "      width: 850px;\n" +
            "      margin: 0 auto;\n" +
            "      position: relative;\n" +
            "      z-index: 1;\n" +
            "  }\n" +
            "\n" +
            "  .block1 {\n" +
            "      width: 850px;\n" +
            "      position: relative;\n" +
            "      margin: 0 auto;\n" +
            "  }\n" +
            "\n" +
            "  .block2 {\n" +
            "      position: relative;\n" +
            "      background-color: #f2f2f2;\n" +
            "      width: 100%;\n" +
            "  }\n" +
            "\n" +
            "  .block2::before, .block2::after {\n" +
            "      content: \"\";\n" +
            "      display: table;\n" +
            "  }\n" +
            "\n" +
            "  .block2::after, .photoInfoTable::after {\n" +
            "      clear: both;\n" +
            "  }\n" +
            "\n" +
            "  .photoInfoTable::before, .photoInfoTable::after {\n" +
            "      content: \"\";\n" +
            "      display: table;\n" +
            "  }\n" +
            "\n" +
            "  .photoInfoTable {\n" +
            "      width: 850px;\n" +
            "      float: left;\n" +
            "  }\n" +
            "\n" +
            "  .headerFilm h1 {\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "  }\n" +
            "\n" +
            "  .headerFilm {\n" +
            "      width: 620px;\n" +
            "      padding: 20px 20px 20px 15px;\n" +
            "      position: relative;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  H1.moviename {\n" +
            "      vertical-align: middle;\n" +
            "      padding-left: 0px;\n" +
            "      margin: 5px 0;\n" +
            "      font-size: 25px;\n" +
            "      font-weight: normal;\n" +
            "  }\n" +
            "\n" +
            "  H1 {\n" +
            "      font-size: 25px;\n" +
            "      font-weight: normal;\n" +
            "      color: #000;\n" +
            "  }\n" +
            "\n" +
            "  .headerFilm > span {\n" +
            "      color: #666;\n" +
            "      font-size: 13px;\n" +
            "  }\n" +
            "\n" +
            "  .film-img-box {\n" +
            "      margin-left: 0;\n" +
            "      position: relative;\n" +
            "      left: -12px;\n" +
            "      min-height: 205px;\n" +
            "      margin-bottom: 15px;\n" +
            "  }\n" +
            "\n" +
            "  .film-img-box img {\n" +
            "      border: 0;\n" +
            "  }\n" +
            "\n" +
            "  .photoBlock {\n" +
            "      width: 210px;\n" +
            "      padding: 0 0 0 0;\n" +
            "      float: left;\n" +
            "      position: relative;\n" +
            "      font-size: 11px;\n" +
            "  }\n" +
            "\n" +
            "  .movie-buttons-container {\n" +
            "      margin-bottom: 20px;\n" +
            "  }\n" +
            "\n" +
            "  .torrentbutton {\n" +
            "      cursor: pointer;\n" +
            "      border: none;\n" +
            "      -webkit-appearance: none;\n" +
            "      -moz-appearance: none;\n" +
            "      appearance: none;\n" +
            "      background-color: #f60;\n" +
            "      border-radius: 3px;\n" +
            "      color: #fff;\n" +
            "      display: block;\n" +
            "      font: 12px Arial, sans-serif;\n" +
            "      font-weight: normal;\n" +
            "      line-height: normal;\n" +
            "      font-weight: bold;\n" +
            "      height: 35px;\n" +
            "      line-height: 36px;\n" +
            "      -webkit-transition: background-color 0.1s, color 0.1s, border-color 0.1s;\n" +
            "      -moz-transition: background-color 0.1s, color 0.1s, border-color 0.1s;\n" +
            "      transition: background-color 0.1s, color 0.1s, border-color 0.1s;\n" +
            "      text-align: center;\n" +
            "      text-decoration: none;\n" +
            "      width: 160px;\n" +
            "      margin: 10px 0 10px 15px;\n" +
            "      display:inline-block;\n" +
            "  }\n" +
            "\n" +
            "  .infoTable {\n" +
            "      float: left;\n" +
            "      display: block;\n" +
            "  }\n" +
            "\n" +
            "  .infoTable .info {\n" +
            "      width: 465px;\n" +
            "  }\n" +
            "\n" +
            "  .info, .info * {\n" +
            "      border-collapse: collapse;\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "  }\n" +
            "\n" +
            "  .info tr {\n" +
            "      border-bottom: #DFDFDF solid 1px; \n" +
            "  }\n" +
            "\n" +
            "  .info .type {\n" +
            "      color: #f60;\n" +
            "      width: 119px;\n" +
            "      padding-left: 23px;\n" +
            "  }\n" +
            "\n" +
            "  .info td {\n" +
            "      min-height: 14px;\n" +
            "      vertical-align: top;\n" +
            "      padding-bottom: 9px;\n" +
            "      padding: 6px 0 6px 20px;\n" +
            "  }\n" +
            "\n" +
            "  td {\n" +
            "      font-family: tahoma,verdana,arial;\n" +
            "      font-size: 15px;\n" +//размер шрифта в описании
            "      color: #000;\n" +
            "  }\n" +
            "\n" +
            "  .film-rating {\n" +
            "      border-radius: 1px;\n" +
            "      position: absolute;\n" +
            "      left: 5px;\n" +
            "      top: 5px;\n" +
            "      z-index: 5;\n" +
            "      box-shadow: none;\n" +
            "      color: #fff;\n" +
            "      width: 32px;\n" +
            "      font-size: 11px;\n" +
            "      font-weight: 600;\n" +
            "      line-height: 13px;\n" +
            "      padding: 3px 0 2px;\n" +
            "      text-align: center;\n" +
            "      font-family: Arial,Tahoma,Verdana,sans-serif;\n" +
            "  }";
 }
