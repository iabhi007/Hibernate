package com.abhi.lockandisolationlevels;

import com.abhi.utitlity.HibernateUtil;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Nice Read
 * https://vladmihalcea.com/optimistic-vs-pessimistic-locking/
 * https://arnoldgalovics.com/jpa-pessimistic-locking/
 *
 * There are two ways to deal with concurrency conflicts, either you detect the issue and react to
 * it or you are not letting the system to get into such situations.
 * The first approach – detecting the anomaly – can be solved by using optimistic locking,
 * the latter one can be done by using pessimistic locking.
 * With using optimistic locking, when the anomaly would happen, the application just throws an exception
 * (in case of JPA) and it can react by showing an error to the user to try again or immediately try to retry
 * the operation in the code.
 *
 * In case of pessimistic locking, you are utilizing database locks
 * to prevent concurrent readers/writers from interleaving.
 *
 */
public class OptimisticLockDemo {

    public static void main(String[] args) throws InterruptedException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        LockEntity lock = LockEntity.builder()
                .lockKey("001")
                .lockedTill(LocalDateTime.now().plus(300, ChronoUnit.SECONDS))
                .build();

        session.persist(lock);

        VersionLessLockEntity versionLessLock = VersionLessLockEntity.builder()
                .lockKey("002")
                .lockedTill(LocalDateTime.now().plusSeconds(1000))
                .name("nag")
                .build();
        session.persist(versionLessLock);
        session.getTransaction().commit();


        try {
            // Below code simulates same object being read from 2 different session
            // and both try to update a single row which can lead to Lost Update anomaly.
            // Using optimistic locking we have avoided the situation.
            Session session1 = sessionFactory.getCurrentSession();
            session1.getTransaction().begin();
            LockEntity lock1 = session1.get(LockEntity.class, "001");
            new Thread(() -> {
                System.out.println("Async code");
                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                LockEntity lock2 = session2.get(LockEntity.class, "001");
                lock2.setLockedTill(LocalDateTime.now().plusSeconds(200));
                session2.persist(lock2);
                session2.getTransaction().commit();
            }).start();
            // reason for sleep is so that the above update gets completed in the separate thread
            Thread.sleep(1000);
            lock1.setLockedTill(LocalDateTime.now().plusSeconds(1000));
            session1.persist(lock1);
            session1.getTransaction().commit();
        } catch (OptimisticLockException exp) {
            System.out.println(exp.getLocalizedMessage());
            exp.printStackTrace();
        }

        /**
         * Version less updates for non overlapping updates
         */

        Thread.sleep(1000);
        try {
            // Below code simulates same object being read from 2 different session
            // and both try to update a single row which can lead to Lost Update anomaly.
            // Using optimistic locking we have avoided the situation.
            Session session1 = sessionFactory.getCurrentSession();
            session1.getTransaction().begin();
            VersionLessLockEntity versionLessLock1 = session1.get(VersionLessLockEntity.class, "002");
            new Thread(() -> {
                System.out.println("\n\nAsync code for version less optimistic locking\n\n");
                Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
                session2.beginTransaction();
                VersionLessLockEntity versionLessLock2 = session2.get(VersionLessLockEntity.class, "002");
                versionLessLock2.setLockedTill(LocalDateTime.now().plusYears(1));

                // to check if the Optimistic Lock exception is thrown

                new Thread(() -> {
                    System.out.println("[NESTED] Started Async code for version-less optimistic locking\n");
                    Session session3 = HibernateUtil.getSessionFactory().getCurrentSession();
                    session3.beginTransaction();
                    VersionLessLockEntity versionLessLock3 = session3.get(VersionLessLockEntity.class, "002");
                    versionLessLock3.setLockedTill(LocalDateTime.now().plusYears(1));
                    session3.persist(versionLessLock3);
                    session3.getTransaction().commit();
                    System.out.println("[NESTED] Ended Async code for version-less optimistic locking\n");
                }).start();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                session2.persist(versionLessLock2);
                session2.getTransaction().commit();
            }).start();
            // reason for sleep is so that the above update gets completed in the separate thread
            Thread.sleep(1000);
            versionLessLock1.setName("Abhishek");
            session1.persist(versionLessLock1);
            session1.getTransaction().commit();
        } catch (OptimisticLockException exp) {
            // below won't be printed as the expection is thrown in separate thread
            System.out.println("****************%^&*(^&*(");
            exp.printStackTrace();
        }

        // deleting the entity so that we can test the flow

        Thread.sleep(3000);
        System.out.println("DELETING the entities ***********");
        Session session1 = sessionFactory.getCurrentSession();
        session1.beginTransaction();
        session1.remove(session1.find(LockEntity.class, "001"));
        session1.remove(session1.find(VersionLessLockEntity.class, "002"));
        session1.getTransaction().commit();


    }
}
