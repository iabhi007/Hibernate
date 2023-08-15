package com.abhi.onetomany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(generator = "custom_generator")
    @GenericGenerator(name = "custom_generator", strategy = "com.abhi.utitlity.IdentityUtil",
        parameters = {@Parameter(name = "prefix_value", value = "guide")})
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private Long salaryValue;

    @OneToMany(mappedBy = "guide", fetch = FetchType.LAZY)
    private List<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide)) return false;

        Guide guide = (Guide) o;

        if (!Objects.equals(id, guide.id)) return false;
        if (!Objects.equals(name, guide.name)) return false;
        return Objects.equals(salaryValue, guide.salaryValue);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (salaryValue != null ? salaryValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salaryValue +
                '}';
    }
}
