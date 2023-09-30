package com.abhi.secondlevelcache;

import com.abhi.basics.HelloWorld;
import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

/**
 * https://howtodoinjava.com/hibernate/configuring-ehcache-3-with-hibernate-6/
 */
public class EhcacheDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        //Capturing the stats
        Statistics statistics = sessionFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        HelloWorld helloWorld = session.get(HelloWorld.class, "1");

        System.out.println("Session statictics" + session.getStatistics()+"\n");
        session.getTransaction().commit();


        Session session1 = sessionFactory.getCurrentSession();
        session1.beginTransaction();
        // This will be fetched from the cache and no select statement will be issued.
        HelloWorld helloWorld1 = session1.get(HelloWorld.class, "1");
        session1.getTransaction().commit();
        System.out.println("\n\nStats:" + statistics.getEntityStatistics("com.abhi.basics.HelloWorld"));
    }
}
