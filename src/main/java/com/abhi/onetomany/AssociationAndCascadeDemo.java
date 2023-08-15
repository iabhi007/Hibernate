package com.abhi.onetomany;

import com.abhi.utitlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * This is used to demo below :
 * ManyToOne and OneToMany associations as well as cascadeTypes on the same.
 * Also, shows the uni-directional as well as bi-directional associations.
 * ( not included is OneToMany uni-direction association which is not recommended, check lecture for explanation)
 * Below are few good reads.
 * Cascades types: https://www.baeldung.com/jpa-cascade-types
 * Nice Read : https://thorben-janssen.com/avoid-cascadetype-delete-many-assocations/
 */
public class AssociationAndCascadeDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
//
//        Guide guide = Guide.builder()
//                .name("chad darby")
//                .salary(1000L)
//                .build();
//
//        Student student = Student.builder()
//                .name("Abhishek Sharma")
//                .guide(guide)
//                .build();
//
//        session.beginTransaction();
//        /**
//         * This is unidirection ManyToOne association
//         * If the below is commented out and we don't have any cascade type defined on the
//         * owner ie student entity then an exception
//         * Not-null property references a transient value - transient instance must be saved before current operation
//         * is thrown.
//         * So, either define the cascade type or persist the guide entity before student entity as below
//         */
//        //session.persist(guide);
//        session.persist(student);
//        session.getTransaction().commit();
//
//
//
//        // BoDirection OneToMany association
//        Guide guide1 = Guide.builder()
//                .salary(2000L)
//                .name("Thor AllMighty")
//                .build();
//
//        Student student1 = Student.builder()
//                .name("abhishek1")
//                .guide(guide1)
//                .build();
//
//        Student student2 = Student.builder()
//                .name("abhishek2")
//                .guide(guide1)
//                .build();
//
//        Student student3 = Student.builder()
//                .name("abhishek3")
//                .guide(guide1)
//                .build();
//
//        session = sessionFactory.getCurrentSession();
//        session.beginTransaction();
//
//        session.persist(student1);
//        session.persist(student2);
//        session.persist(student3);
//        session.getTransaction().commit();


        /**
         *  from OwnerEntity to Owner association (OneToMany)
         * Here if we fetch the guide in fetchType as Lazy then only guide query is made, unless we
         * ask explicity the students via getStudent. Two select statements are made independently.
         *
         * Here if we fetch the guide in fetchType as EAGER, the query is like below:
         *     select
         *         g1_0.id,
         *         g1_0.name,
         *         g1_0.salary,
         *         s1_0.guide_id,
         *         s1_0.id,
         *         s1_0.name
         *     from
         *         guide g1_0
         *     left join
         *         student s1_0
         *             on g1_0.id=s1_0.guide_id
         *     where
         *         g1_0.id=?
         *
         *
         * From ManyToOne association
         * If the fetchType is LAZY, then two select statements are called if we try to fetch Guide entity from Student.
         * Else only one query is called as below
         * Hibernate:
         *     select
         *         s1_0.id,
         *         s1_0.guide_id,
         *         s1_0.name
         *     from
         *         student s1_0
         *     where
         *         s1_0.id=?
         * 00:16:56.664 [org.hibernate.type.descriptor.JdbcBindingLogging] [main] TRACE org.hibernate.orm.jdbc.bind - binding parameter [1] as [VARCHAR] - [student_24549]
         * ************  Student{id='student_24549', name='abhishek1'}
         * Hibernate:
         *     select
         *         g1_0.id,
         *         g1_0.name,
         *         g1_0.salary
         *     from
         *         guide g1_0
         *     where
         *         g1_0.id=?
         *
         * If fetchType is EAGER, only one query is hit which is:
         * Hibernate:
         *     select
         *         s1_0.id,
         *         g1_0.id,
         *         g1_0.name,
         *         g1_0.salary,
         *         s1_0.name
         *     from
         *         student s1_0
         *     join
         *         guide g1_0
         *             on g1_0.id=s1_0.guide_id
         *     where
         *         s1_0.id=?
         *
         *
         * So, it can be seen that In EAGER fetch JOINs are used in single query,
         * In LAZY fetch different select statements are used based on requirement of data.
         *
         */
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        /**
         * VVIP
         * This below Logs show that in Lazy fetching, only when we invoke any operation on the
         * collection then only select statement is executed.
         * Here at line 156 there won't be any select statement, but at 158 a select query will be issued to DB.
         */
        Guide fetchGuide = session.get(Guide.class, "guide_82939");
        System.out.println("Only guide fetched ()()))()()()()()(()()(");
        List<Student> list = fetchGuide.getStudents();
        System.out.println("Only list fetched ()()))()()()()()(()()(");
        System.out.println(list.size());
        System.out.println(list.get(0).getGuide().getStudents().size());




        Student fetchStudent = session.get(Student.class, "student_24549");
        System.out.println("************  "+fetchStudent);
        System.out.println("************  "+fetchStudent.getGuide());








        // this is example of automatic dirty checking
        // even though we are not updating the entity via session.save() still changes reflects,
        // as soon as the transaction commits.
        // nice read : https://stackoverflow.com/questions/14581865/hibernate-flush-and-commit
        fetchStudent.setName("changed namem 3");
        session.persist(fetchStudent);
        session.flush();
        fetchStudent.setName("changed namem 4");


        //System.out.println(fetchStudent);
        session.getTransaction().commit();


    }
}
