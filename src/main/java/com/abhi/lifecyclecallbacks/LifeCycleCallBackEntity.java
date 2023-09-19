package com.abhi.lifecyclecallbacks;


import com.abhi.utitlity.HibernateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "call_back_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(value = {CallbackEntityListener.class})
public class LifeCycleCallBackEntity {

    @Id
    @Column(name = "id")
//    @GeneratedValue(generator = "custom")
//    @GenericGenerator(name = "custom", strategy = HibernateUtil.CUSTOM_GENERATOR_CLASS,
//            parameters = {@Parameter(name = "prefix_value",value = "callback")})
    private String id;

    @Column(name = "text")
    private String text;
}
