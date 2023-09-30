package com.abhi.basics;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@Table(name = "hello_world")
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HelloWorld {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text_value")
    private String textValue;

    public HelloWorld(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public String toString() {
        return "HelloWorld{" +
                "id=" + id +
                ", textValue='" + textValue + '\'' +
                '}';
    }
}
