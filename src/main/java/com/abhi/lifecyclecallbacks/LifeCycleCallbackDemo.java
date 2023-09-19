package com.abhi.lifecyclecallbacks;

import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Class for demonstrating Callbacks which are
 * There are 7 Life cycle callback annotation in JPA:
 * 1. @PostLoad
 * 2. @PrePersist
 * 3. @PostPersist
 * 4.  @PreUpdate
 * 5. @PostUpdate
 * 6. @PreRemove
 * 7. @PostRemove
 *
 * These methods are called only when a database query is issued. If there is no query to be
 * executed then there won't be any call back method be invoked.
 */
public class LifeCycleCallbackDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // executing pre and post persist
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        LifeCycleCallBackEntity entity =
                LifeCycleCallBackEntity.builder()
                        .id("001")
                        .text("Hello World").build();
        session.persist(entity);
        session.getTransaction().commit();

        // post load amd pre and post update
        Session session1 = sessionFactory.getCurrentSession();
        session1.beginTransaction();
        LifeCycleCallBackEntity entity1 =
                session1.find(LifeCycleCallBackEntity.class, "001");
        //If the Entity is already in the persistent context,
        // the PostLoad callback wil not be called
        LifeCycleCallBackEntity entity3 =
                session1.find(LifeCycleCallBackEntity.class, "001");
        System.out.println("Entity 3 " + entity3.getText());
        entity1.setText("Updated text");
        session1.getTransaction().commit();


        // post load and pre and post remove
        Session session2 = sessionFactory.getCurrentSession();
        session2.beginTransaction();
        LifeCycleCallBackEntity entity2 =
                session2.find(LifeCycleCallBackEntity.class, "001");
        session2.remove(entity2);
        session2.getTransaction().commit();
        session2.close();
    }
}
