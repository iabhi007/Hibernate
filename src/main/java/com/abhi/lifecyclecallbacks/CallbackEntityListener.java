package com.abhi.lifecyclecallbacks;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * Since the below listener class is outside the entity,
 * we need to create a method that accepts Object parameter and must return void.
 *
 * If this was inside the entity class, then callback method should:
 * 1. Must have void return.
 * 2. Must not have any method parameters.
 * 3. Can't be static or final.
 *
 * As we can see here that the Visibility of the method isn't an issue.
 */
@NoArgsConstructor
public class CallbackEntityListener {

    @PrePersist
    private void prePersistMethod(Object o) {
        System.out.println("*******Pre persist*********");
    }

    @PostPersist
    private void postPersist(Object o){
        System.out.println("******** POST Persist **************");
    }

    @PreUpdate
    public void preUpdateMethod(Object o){
        System.out.println("****** Pre Update ***********");
    }
    @PostUpdate
    protected void postUpdate(Object o) {
        System.out.println("**********  POST update ************");
    }

    @PreRemove
    public void preRemove(Object o) {
        System.out.println("********* pre remove **********");
    }

    @PostRemove
    public void postRemove(Object o) {
        System.out.println("****** post remove *********");
    }

    @PostLoad
    public void postLoad(Object o) {
        System.out.println("******** POST LOAD **********");
    }
}
