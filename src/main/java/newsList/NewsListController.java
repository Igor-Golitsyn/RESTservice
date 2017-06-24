package newsList;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by golit on 19.06.2017.
 */
public enum NewsListController {
    ENUM;
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewsList");
    private EntityManager em = factory.createEntityManager();

    public NewsListItem[] getNewsList() {
        em.getTransaction().begin();
        TypedQuery<NewsListItem> namedQuery = em.createNamedQuery(NewsListItem.class.getSimpleName() + ".getAll", NewsListItem.class);
        List<NewsListItem> newsListItems = namedQuery.getResultList();
        em.getTransaction().commit();
        // em.close();
        // factory.close();
        return newsListItems.toArray(new NewsListItem[newsListItems.size()]);
    }

    public NewsListItem[] writeToBase(String name, NewsListItem newsListItem) {
        //EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewsList");
        //EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NewsListItem> namedQuery = em.createNamedQuery("NewsListItem.find", NewsListItem.class).setParameter("name", name);
        NewsListItem item;
        try {
            item = namedQuery.getSingleResult();
        } catch (NoResultException e) {
            item = null;
        }
        if (item != null) {
            item.setRestLink(newsListItem.getRestLink());
            item.setShowNewsList(newsListItem.isShowNewsList());
            item.setTypeFinder(newsListItem.isTypeFinder());
            item.setName(newsListItem.getName());
            em.merge(item);
        } else {
            em.merge(newsListItem);
        }
        em.getTransaction().commit();
        //em.close();
        //factory.close();
        return getNewsList();
    }

   /* public static void main(String[] args) {
        //NewsListController.ENUM.writeToBase("ГИБДД", new NewsListItem("ГИБДД","/gibdd",true,false));
        //NewsListController.ENUM.writeToBase("RUTOR.info", new NewsListItem("RUTOR.info","/torrent",true,true));
        NewsListController.ENUM.writeToBase("31TV.ru", new NewsListItem("31TV.ru","/31tv",true,false));
        NewsListItem[] news = NewsListController.ENUM.getNewsList();
        for (NewsListItem item : news) {
            System.out.println(item);
        }
    }*/
}
