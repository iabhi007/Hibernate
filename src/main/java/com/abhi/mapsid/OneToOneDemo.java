package com.abhi.mapsid;

import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.ZonedDateTime;

public class OneToOneDemo {

    public static void main(String[] args) {


        Customer customer = Customer.builder()
                .name("Abhishek sharma")
                .build();

        Passport passport = Passport.builder()
                .date(ZonedDateTime.now())
                .customer(customer)
                .build();

        //customer.setPassport(passport);

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.getCurrentSession();

        session.beginTransaction();

        //session.persist(passport);
        //session.persist(customer);


//        Passport passport1 = session.get(Passport.class, "customer__91532");
//        System.out.println(passport1);
//        Customer customer1 = passport1.getCustomer();
//        customer1.setName("Automatic dirty check test");

        Customer customer1 = session.get(Customer.class, "customer__84193");
        Passport passport2 = session.get(Passport.class, customer1.getId());

        System.out.println(customer1 +"   "+ passport2);
        session.getTransaction().commit();
    }

}
