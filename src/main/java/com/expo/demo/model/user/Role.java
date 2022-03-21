package com.expo.demo.model.user;

import com.expo.demo.constants.Diciton;

import javax.persistence.*;

@Entity
@Table(name = Diciton.TABLE_ROLE, schema = Diciton.SCHEMA)
public class Role {
    private Long id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
