package com.abhi.manytomany;


import com.abhi.embedded.Address;
import com.abhi.utitlity.HibernateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This is the Owner entity since it uses @JoinTable annotation ( same as @JoinColumn)
 */
@Entity
@Table(name = "actor_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Actor {

    @Id
    @Column(name = "actor_id")
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", strategy = HibernateUtil.CUSTOM_GENERATOR_CLASS,
    parameters = {@Parameter(name = "prefix_value", value = "actor")})
    private String id;

    @Column(name = "actor_name")
    private String name;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "city", column = @Column(name = "actor_city")),
            @AttributeOverride(name = "state", column = @Column(name = "actor_state"))
    })
    private Address address;


    @ManyToMany
    @JoinTable(name = "actor_movie",
    joinColumns = {@JoinColumn(name = "actor_id_fk", referencedColumnName = "actor_id")} ,
    inverseJoinColumns = {@JoinColumn(name = "movie_id_fk", referencedColumnName = "movie_id")})
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;

        Actor actor = (Actor) o;

        if (!Objects.equals(id, actor.id)) return false;
        if (!Objects.equals(name, actor.name)) return false;
        return Objects.equals(address, actor.address);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
