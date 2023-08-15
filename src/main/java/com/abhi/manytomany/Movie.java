package com.abhi.manytomany;


import com.abhi.utitlity.HibernateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

/**
 * This below is the inverse end of the relationship or its is owned entity as
 * it has "mappedBy" property on @ManyToMany annotation.
 */
@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  Movie {

    @Id
    @Column(name = "movie_id")
    @GeneratedValue(generator = "cus")
    @GenericGenerator(name = "cus", strategy = HibernateUtil.CUSTOM_GENERATOR_CLASS,
    parameters = {@org.hibernate.annotations.Parameter(name = "prefix_value", value = "movie_")})
    private String id;

    @Column(name = "movie_name")
    private String movieName;


    @ManyToMany(mappedBy = "movies")
    @Builder.Default
    private Set<Actor> actors = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "movie_genre", joinColumns = {@JoinColumn(name = "movie_id", referencedColumnName = "movie_id")})
    @Column(name = "genre")
    @Enumerated(value = EnumType.STRING)
    List<MovieGenre> movieGenreList = new ArrayList<>();

    enum MovieGenre {
        HORROR, ADULT, PG_13, PG18;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (!Objects.equals(id, movie.id)) return false;
        return Objects.equals(movieName, movie.movieName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (movieName != null ? movieName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", movieName='" + movieName + '\'' +
                '}';
    }

    //For updating the owner entity via inverse end.
    public void addActor(Actor actor) {
        actors.add(actor);
        actor.getMovies().add(this);
    }
}
