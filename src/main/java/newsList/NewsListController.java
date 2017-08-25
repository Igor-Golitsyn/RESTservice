package newsList;

import javax.persistence.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by golit on 19.06.2017.
 */
public enum NewsListController {
    CONTROLLER;
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewsList");
    private EntityManager em = factory.createEntityManager();

    public NewsListItem[] getNewsList() {
        em.getTransaction().begin();
        TypedQuery<NewsListItem> namedQuery = em.createNamedQuery("NewsListItem.getAll", NewsListItem.class);
        List<NewsListItem> newsListItems = namedQuery.getResultList();
        em.getTransaction().commit();
        ArrayList<NewsListItem> clearList=new ArrayList<>();
        for (NewsListItem item:newsListItems){
            if (item.isShowNewsList()) clearList.add(item);
        }
        // em.close();
        // factory.close();

        return clearList.toArray(new NewsListItem[clearList.size()]);
    }

    public NewsListItem[] writeToBase(NewsListItem newsListItem) {
        //EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewsList");
        //EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NewsListItem> namedQuery = em.createNamedQuery("NewsListItem.find", NewsListItem.class).setParameter("name", newsListItem.getName());
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
        //NewsListController.CONTROLLER.writeToBase("ГИБДД", new NewsListItem("ГИБДД","/gibdd",true,false));
        //NewsListController.CONTROLLER.writeToBase("RUTOR.info", new NewsListItem("RUTOR.info","/torrent",true,true));
        NewsListItem itemNew = new NewsListItem("74.mchs.gov.ru","/mchs74",true,false);
        NewsListController.CONTROLLER.writeToBase(itemNew.getName(),itemNew);
        NewsListItem[] news = NewsListController.CONTROLLER.getNewsList();
        for (NewsListItem item : news) {
            System.out.println(item);
        }
    }*/
}
