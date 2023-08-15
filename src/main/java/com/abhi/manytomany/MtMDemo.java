package com.abhi.manytomany;

import com.abhi.embedded.Address;
import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Arrays;

public class MtMDemo {

    public static void main(String[] args) {

        Address address = new Address("bangalor", "karnataka");
        Address address1 = new Address("darbhanga", "Bihar");


        Actor actor1 = Actor.builder()
                .address(address)
                .name("Hugh jackman")
                .build();

        Actor actor2 = Actor.builder()
                .address(address1)
                .name("Johnny Depp")
                .build();

        Movie movie1 = Movie.builder()
                .movieName("X men")
                .build();

        Movie movie2 = Movie.builder()
                .movieName("Pirates of Carribean")
                .build();

        Movie movie3 = Movie.builder()
                .movieName("Harry potter")
                .build();

        // Associating the entities
        actor1.getMovies().add(movie1);
        actor1.getMovies().add(movie2);
        actor2.getMovies().add(movie2);

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        // either we need to put cascade on the owner entity annotation or
        // save the inverse end before saving owned entity.
//        session.persist(movie1);
//        session.persist(movie2);
        //session.persist(movie3);
//
//        session.persist(actor1);
//        session.persist(actor2);

        Movie movie = session.get(Movie.class, "movie__13473");
        Actor actor = session.get(Actor.class, "actor_81362");
//        movie.setMovieName("hello test");
//        movie.addActor(actor);
        //actor.getMovies().add(movie3);

        session.getTransaction().commit();


        // Testing the collection types, Enumerated type is required as the database column is varchar(255)

//        Movie movie4 = Movie.builder()
//                .movieName("Phir hera pheri")
//                .movieGenreList(Arrays.asList(Movie.MovieGenre.ADULT, Movie.MovieGenre.PG_13))
//                .build();
//
//        session = sessionFactory.getCurrentSession();
//        session.beginTransaction();
//        session.persist(movie4);
//        session.getTransaction().commit();
    }
 }
