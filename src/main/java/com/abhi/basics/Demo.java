package com.abhi.basics;

import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionFactory creates Session which is used to begin a transaction, all the work is performed
 * in the transaction and as soon as its committed it is reflected in DataBase.
 * https://stackoverflow.com/questions/8046662/hibernate-opensession-vs-getcurrentsession
 *
 * https://stackoverflow.com/questions/3220336/whats-the-use-of-session-flush-in-hibernate
 *
 * SessionFactory.openSession() always opens a new session that you have to close once you are done
 * with the operations.
 * SessionFactory.getCurrentSession() returns a session bound to a context - you don't need to close this.
 * A session is opened whenever getCurrentSession() is called for the first time
 * and closed when the transaction ends.
 */
public class Demo {

    private static final Logger LOG = LoggerFactory.getLogger(Demo.class);
    public static void main(String[] args) {
        // creating hello world object

        HelloWorld helloWorld = HelloWorld.builder()
                .textValue("hello world !!")
                .build();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        LOG.info("Hibernate session built successfully.. {}", sessionFactory);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //session.persist(helloWorld);
        session.getTransaction().commit();
        LOG.info("The persisted message is {}", helloWorld);

        LOG.info("Starting a new transaction and fetching and updating the object");
        session.beginTransaction();

        HelloWorld fetched = session.get(HelloWorld.class, 2);
        fetched.setTextValue("new new hello world");

        // without commit the changes won't reflect
        session.getTransaction().commit();
        session.close();


        //the above can be done via another way as well: https://stackoverflow.com/questions/8046662/hibernate-opensession-vs-getcurrentsession

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.beginTransaction();
        HelloWorld helloWorld1 = currentSession.get(HelloWorld.class, "4");
        LOG.info("Fetched via getCurrentSession is {}", helloWorld1);
        helloWorld1.setTextValue("Fetched via getCurrentSession is");

        // commenting out due to error : attempt to create delete event with null entity
        //currentSession.remove(currentSession.get(HelloWorld.class, "5"));


        // without commit the changes won't reflect
        currentSession.getTransaction().commit();
    }
}
