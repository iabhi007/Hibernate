package com.abhi.embedded;

import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedDemo {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDemo.class);
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Address homeAddress = new Address("Patna", "Bihar");
        Address officeAddress = new Address("Bangalore", "karnataka");

        User abhiUser = new User("Test name", homeAddress, officeAddress);
        logger.info("Trying to persist user {}", abhiUser);

        // Exception in thread "main" org.hibernate.HibernateException: No CurrentSessionContext configured
        // if this property is not enabled (hibernate.current_session_context_class=thread)
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(abhiUser);
        session.getTransaction().commit();
    }
}
