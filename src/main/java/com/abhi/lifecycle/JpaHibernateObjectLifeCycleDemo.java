package com.abhi.lifecycle;

import com.abhi.basics.HelloWorld;
import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * https://stackoverflow.com/questions/53976845/hibernate-difference-between-update-and-merge-method
 */
public class JpaHibernateObjectLifeCycleDemo {

    public static void main(String[] args) {

        //transient state
        HelloWorld helloWorld = new HelloWorld(100,"lifecycle");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Nice read about transaction commit and session being closed : https://stackoverflow.com/questions/2793877/reuse-hibernate-session-in-thread
        Session session1 = sessionFactory.openSession();
        session1.beginTransaction();
        session1.persist(helloWorld); // object becomes in persistent state
        session1.getTransaction().commit();


        session1.beginTransaction();
        helloWorld.setTextValue("changed !!!"); // still in persistent context hence applicable for Automatic Dirty Check.
        // as the session isn't closed yet
        session1.getTransaction().commit();
        session1.close();


        // since session1 is closed its in detached state
        // merge() will bring the object back to persistent context;
        helloWorld.setTextValue("changed in detached state");

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        // this brings the obj back to persistent context hence applicable for
        // Automatic Dirty Check and value will be changed.
        session2.merge(helloWorld);
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        session3.beginTransaction();

        // the reason for below is that merge() operation issues a fresh select query and then copy the changes from the
        // passed object on the fetched entity object and then return the reference of the same. So basically it creates a
        //new entity itself and not modifies the reference one which is passed. check the sout statements
        System.out.println("before "+helloWorld);
        HelloWorld helloWorld2 = session3.merge(helloWorld);
        System.out.println("after "+helloWorld2);

        System.out.println("Equality " + (helloWorld2 == helloWorld));

        // in removed state, this is allowed only for object in persistent state, if we try to remove()
        // a detached object it will throw illegal state exception. ( only in JPA , Hibernate allows it)
        //Note here we have passed the updated reference after the merge() method
       System.out.println(session3.contains(helloWorld) + "  " + session3.contains(helloWorld2));
        session3.remove(helloWorld2);
        session3.getTransaction().commit();
        session3.close();



        HelloWorld test = new HelloWorld(101,"persiste and detached at the same time");
        Session sesion4 = sessionFactory.openSession();
        sesion4.beginTransaction();
        sesion4.persist(test);
        sesion4.detach(test);
        // so nothing will happen because at line 70 test object moves to persistent state
        // but at line 71 it becomes detached, hence no changes are tracked and test object becomes
        // eligible for garbage collection and nothing happens.
        sesion4.getTransaction().commit();
        sesion4.close();


        HelloWorld lab = new HelloWorld(102, "lab 43 test");
        Session session5 = sessionFactory.openSession();
        session5.beginTransaction();
        session5.persist(lab);
        session5.getTransaction().commit();
        session5.close();



        Session session6 = sessionFactory.openSession();
        session6.beginTransaction();
        HelloWorld obj = session6.get(HelloWorld.class, 102);
        //obj = session6.merge(obj);
        session6.evict(obj);
        session6.remove(obj);
        System.out.println(session6.contains(obj));
        session6.getTransaction().commit();
        session6.close();
    }
}
