package com.abhi.utitlity;

import com.abhi.lifecyclecallbacks.LifeCycleCallBackEntity;
import com.abhi.manytomany.Actor;
import com.abhi.manytomany.Movie;
import com.abhi.onetomany.Guide;
import com.abhi.onetomany.Student;
import com.abhi.basics.HelloWorld;
import com.abhi.embedded.User;
import com.abhi.mapsid.Customer;
import com.abhi.mapsid.Passport;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * https://howtodoinjava.com/hibernate/hibarnate-build-sessionfactory/
 * https://www.baeldung.com/hibernate-5-bootstrapping-api
 *
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = initSessionFactory();
    public static final String CUSTOM_GENERATOR_CLASS = "com.abhi.utitlity.IdentityUtil";
    private static SessionFactory initSessionFactory() {
        Configuration configuration = new Configuration();
        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();

        // adding the annotated class
        configuration.addAnnotatedClass(HelloWorld.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Guide.class);
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Passport.class);
        configuration.addAnnotatedClass(Actor.class);
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(LifeCycleCallBackEntity.class);

        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        return configuration.buildSessionFactory(serviceRegistry);
        //return metadata.getSessionFactoryBuilder().build();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
