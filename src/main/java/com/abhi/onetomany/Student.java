package com.abhi.onetomany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Objects;


/**
 * In mysql this is the table which has FK, hence it is owner entity.
 * Also, @JoinColumn makes it Owner entity.
 * so this is ManyToOne association to Guide class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(generator = "generic_generator")
    @GenericGenerator(name = "generic_generator",
            parameters = {@Parameter(name = "prefix_value", value = "student")},
            strategy = "com.abhi.utitlity.IdentityUtil")
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "guide_id", referencedColumnName ="id", nullable = false)
    private Guide guide;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (!Objects.equals(id, student.id)) return false;
        if (!Objects.equals(name, student.name)) return false;
        return Objects.equals(guide, student.guide);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (guide != null ? guide.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
