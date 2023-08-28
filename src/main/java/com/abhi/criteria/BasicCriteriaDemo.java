package com.abhi.criteria;

import com.abhi.basics.HelloWorld;
import com.abhi.onetomany.Guide;
import com.abhi.onetomany.Student;
import com.abhi.utitlity.HibernateUtil;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;

//https://www.baeldung.com/hibernate-criteria-queries
public class BasicCriteriaDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        CriteriaQuery<HelloWorld> criteriaQuery = criteriaBuilder.createQuery(HelloWorld.class);
        Root<HelloWorld> root = criteriaQuery.from(HelloWorld.class);
        criteriaQuery.select(root);

        TypedQuery<HelloWorld> typedQuery = session.createQuery(criteriaQuery);

        List<HelloWorld> helloWorlds = typedQuery.getResultList();
        for (HelloWorld helloWorld : helloWorlds) {
            System.out.println(helloWorld);
        }

        session.getTransaction().commit();
        session.close();

        /*
            Example 2 : Select a particular field.
         */

        Session session1 = sessionFactory.getCurrentSession();
        session1.beginTransaction();

        // way to build criteriaQuery
        CriteriaBuilder stringCriteriaBuilder = session1.getCriteriaBuilder();

        //What the query will return
        CriteriaQuery<String> stringCriteriaQuery = stringCriteriaBuilder.createQuery(String.class);
        //What is the table/Entity name to select the data from
        Root<HelloWorld> root1 = stringCriteriaQuery.from(HelloWorld.class);
        //what column to select the data.
        Path<String> path = root1.get("textValue");
        stringCriteriaQuery = stringCriteriaQuery.select(path);

        //covert to Query
        TypedQuery<String> typedQuery1 = session1.createQuery(stringCriteriaQuery);
        List<String> textValues = typedQuery1.getResultList();
        System.out.println("2nd Example START");
        textValues.forEach(System.out::println);
        System.out.println("2nd Example END");
        session1.getTransaction().commit();
        session1.close();

        /*
         * Example 3 : selecting multiple fields with filter query.
         */

        Session session2 = sessionFactory.getCurrentSession();
        // explicity fetching transaction object and using it
        Transaction multiSelectTransaction = session2.getTransaction();
        multiSelectTransaction.begin();
        CriteriaBuilder criteriaBuilder1 = session2.getCriteriaBuilder();
        // what to fetch
        CriteriaQuery<Object[]> multiSelectCriteria = criteriaBuilder1.createQuery(Object[].class);
        Root<Guide> guideRoot = multiSelectCriteria.from(Guide.class);
        Path<String> namePath = guideRoot.get("name");
        Path<Integer> salarPath = guideRoot.get("salaryValue");
        // multiple by below or
        //multiSelectCriteria = multiSelectCriteria.select(criteriaBuilder1.array(namePath, salarPath));
        // multiselect can be used as well
        multiSelectCriteria = multiSelectCriteria.multiselect(namePath, salarPath);

        // putting where clause
        multiSelectCriteria = multiSelectCriteria.where(criteriaBuilder1.equal(salarPath, 2000));

        TypedQuery<Object[]> typedQuery2 = session2.createQuery(multiSelectCriteria);
        List<Object[]> resultList = typedQuery2.getResultList();
        System.out.println("2nd Example with multiselect START");
        resultList.forEach(obj -> System.out.println(Arrays.toString(obj)));
        System.out.println("2nd Example with multiselect END");
        multiSelectTransaction.commit();

        // Using sessionFactory.openSession() doesn't close the session when the transaction is committed,
        // below will print true,
        // but using getCurrentSession will close the session once the transaction is committed,
        // below will print false.
        System.out.println(session2.isOpen());
        session2.close();

        /*
         * Example 4: Aggregate functions
         */

        Session aggSession = sessionFactory.getCurrentSession();
        aggSession.beginTransaction();
        CriteriaBuilder aggBuilder = aggSession.getCriteriaBuilder();
        CriteriaQuery<Long> longCriteriaQuery = aggBuilder.createQuery(Long.class);
        Root<Guide> guideRoot1 = longCriteriaQuery.from(Guide.class);
        Path<Long> salaryPath = guideRoot1.get("salaryValue");
        longCriteriaQuery.select(aggBuilder.max(salaryPath));
        TypedQuery<Long> aggQuery = aggSession.createQuery(longCriteriaQuery);
        Long maxSalry = aggQuery.getSingleResult();
        System.out.printf("Max slary is %s\n", maxSalry);
        aggSession.getTransaction().commit();

        /*
        Example 5 : Join Using criteria and checking behavior for LazyOrEager loading
        and difference via find() method
        The below will generate two query to first to guide and since the student is EAGER fetch
        a separate query will be issued to fetch students.
        So total two queries will be generated.
         */

        System.out.println("\n\nExample 5 : Join Using criteria and checking behavior for LazyOrEager loading");
        Session joinSession = sessionFactory.getCurrentSession();
        joinSession.beginTransaction();

        CriteriaBuilder joinBuilder = joinSession.getCriteriaBuilder();
        CriteriaQuery<Guide> guideCriteriaQuery = joinBuilder.createQuery(Guide.class);
        Root<Guide> guideRoot2 = guideCriteriaQuery.from(Guide.class);
        Path<Long> salPath = guideRoot2.get("salaryValue");
        guideCriteriaQuery.select(guideRoot2);
        guideCriteriaQuery.where(joinBuilder.equal(salPath, 2000));
        TypedQuery<Guide> guideTypedQuery = joinSession.createQuery(guideCriteriaQuery);

        List<Guide> guideList = guideTypedQuery.getResultList();
        System.out.println("Fetched guide size is "+guideList.size());
        joinSession.getTransaction().commit();

        /*
        The below will issue only one query with left join since its EAGER fetch.
            select
        g1_0.id,
        g1_0.name,
        g1_0.salary,
        s1_0.guide_id,
        s1_0.id,
        s1_0.name
    from
        guide g1_0
    left join
        student s1_0
            on g1_0.id=s1_0.guide_id
    where
        g1_0.id=?
         */
        System.out.println("\nThe below will issue only one query with left join since its EAGER fetch(confirm it by checking code) , via find() method");
        Session session3 = sessionFactory.getCurrentSession();
        session3.beginTransaction();
        Guide guide = session3.find(Guide.class, "guide_82939");
        session3.getTransaction().commit();

        /*
        Join query using Criteria
         */

        System.out.println("\nJoin query using Criteria, should behave as find() method");
        Session joinDemoSession = sessionFactory.getCurrentSession();
        joinDemoSession.beginTransaction();
        CriteriaBuilder builder = joinDemoSession.getCriteriaBuilder();
        // the below will remove duplicate entities
        CriteriaQuery<Guide> joinCriteriaQuery = builder.createQuery(Guide.class).distinct(true);
        Root<Guide> guideRoot3 = joinCriteriaQuery.from(Guide.class);
        Path<Long> saler = guideRoot3.get("salaryValue");

        // The below will
        // guideRoot3.join("students", JoinType.LEFT);
        // just join with the student table but will not fetch the student data
        /*
        Query used is:
         /* <criteria>  select
        distinct g1_0.id,
                g1_0.name,
                g1_0.salary
        from
        guide g1_0
        left join
        student s1_0
        on g1_0.id=s1_0.guide_id
        where
        g1_0.salary=?
         */
        // inorder to fetch students as well , join() needs to be replaced with fetch() method
        // This will behave as EAGER fetch.
        /*
            query would be
            /* <criteria> select
        distinct g1_0.id,
                g1_0.name,
                g1_0.salary,
                s1_0.guide_id,
                s1_0.id,
                s1_0.name
        from
        guide g1_0
        left join
        student s1_0
        on g1_0.id=s1_0.guide_id
        where
        g1_0.salary=?
         */
        // The above comments is not for current implemented code , the above code will hold true when
        // ***The relation between Guide and student is EAGER
        // ***and condition should be only applicable on the Guide and not on any attribute for student

        // The current code just fetches the Guide by joining with student filtered by its name, query is below
        //https://stackoverflow.com/questions/36496158/get-all-columns-of-two-table-joined-with-criteria-query-in-jpa
        /* <criteria>  select
        distinct g1_0.id,
                g1_0.name,
                g1_0.salary
        from
        guide g1_0
        join
        student s1_0
        on g1_0.id=s1_0.guide_id
        where
        s1_0.name=?
        */
        Join<Guide, Student> guideStudentJoin = guideRoot3.join("students", JoinType.INNER);
        Path<String> studentName = guideStudentJoin.get("name");
        joinCriteriaQuery.select(guideRoot3).where(builder.equal(studentName, "abhishek2"));
        TypedQuery<Guide> guideTypedQuery1 = joinDemoSession.createQuery(joinCriteriaQuery);
        List<Guide> guideList1 = guideTypedQuery1.getResultList();
        System.out.println(guideList1);
        joinDemoSession.getTransaction().commit();
    }
}
