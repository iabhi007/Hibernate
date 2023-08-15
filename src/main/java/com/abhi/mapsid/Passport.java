package com.abhi.mapsid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

// OWNER entity

/**
 * For MapsId annotation can be applied on owner entity where the column name is defined with field with
 * MapIds annotation. Also, only @Id value is required for mapping purpose, no actual column in database for this table.
 * But hibernate will map value of ID field with value of customer id.
 *
 * Here SQL would be:
 * CREATE TABLE `passport` (
 *   `created_date` datetime(6) DEFAULT NULL,
 *   `customer_id` varchar(255) NOT NULL,
 *   PRIMARY KEY (`customer_id`),
 *   UNIQUE KEY `customer_id` (`customer_id`),
 *   CONSTRAINT `passport_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Entity
@Table(name = "passport")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passport {

    @Id
//    @GeneratedValue(generator = "custom_generator")
//    @GenericGenerator(name = "custom_generator", strategy = "com.abhi.utitlity.IdentityUtil",
//    parameters = {@Parameter(name = "prefix_value", value = "passport")})
    private String id;

    @Column(name = "created_date")
    private ZonedDateTime date;

    // Concept of Lazy and Eager fetching is same as defined in AssociationsAndCascadeDemo class

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @MapsId
    private Customer customer;

    @Override
    public String toString() {
        return "Passport{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }
}
