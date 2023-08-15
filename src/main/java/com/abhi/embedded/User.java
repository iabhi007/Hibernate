package com.abhi.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * create table user_information(
 * user_id integer not null auto_increment,
 * user_name varchar(255),
 * home_address_city varchar(255),
 * home_address_state varchar(255),
 * office_address_state varchar(255),
 * office_address_city varchar(255),
 * primary key (user_id)
 * )
 */
@Entity
@Table(name = "user_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_address_city")),
            @AttributeOverride(name = "state", column = @Column(name = "home_address_state"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "state", column = @Column(name = "office_address_state")),
            @AttributeOverride(name = "city", column = @Column(name = "office_address_city"))
    })
    private Address officeAddress;

    public User(String userName, Address homeAddress, Address officeAddress) {
        this.userName = userName;
        this.homeAddress = homeAddress;
        this.officeAddress = officeAddress;
    }
}
