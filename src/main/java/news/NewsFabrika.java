package news;

import news.bashOrg.BashOrg;
import news.cosmo.Cosmo;
import news.gibdd.Gibdd;
import news.mchs.Mchs74;
import news.popMech.PopMech;
import news.ru74.Chelyabinsk74ru;
import news.ruTor.RuTorSearch;
import news.shkola.ShkolaOtmena;
import news.tokoTebe.TokoTebe;
import news.tv31.Channel31;
import news.vesti.Vesti;
import news.vodaUUru.VodaUU;

/**
 * Created by golit on 03.07.2017.
 */
public enum  NewsFabrika {
    FABRIKA;

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
            case "Chelyabinsk74ru":
                return new Chelyabinsk74ru();
            case "BashOrg":
                return new BashOrg();
            case "voda":
                return new VodaUU();
            case "tokoTebe":
                return new TokoTebe();
            case "popMech":
                return new PopMech();
            case "cosmo":
                return new Cosmo();
            case "vesti":
                return new Vesti();
            case "shkola":
                return new ShkolaOtmena();
            default:
                return null;
        }
    }
}
