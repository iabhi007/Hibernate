package com.abhi.mapsid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", strategy = "com.abhi.utitlity.IdentityUtil",
    parameters = {@Parameter(name = "prefix_value", value = "customer_")})
    private String id;

    @Column(name = "name")
    private String name;


    /**
     *      making this bi-directional,
     *      but with MapsId we dont need this bi-directional mapping.. we can remove the below field along
     *      with annotation.
     *      and we can fetch the Passport from Customer by below code which uses same primary key
     *         Customer customer1 = session.get(Customer.class, "customer__84193");
     *         Passport passport2 = session.get(Passport.class, customer1.getId());
     *
     *         instead of doing customer1.getPassport() .
     * @return
     */
//    @OneToOne(mappedBy = "customer")
//    private Passport passport;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
