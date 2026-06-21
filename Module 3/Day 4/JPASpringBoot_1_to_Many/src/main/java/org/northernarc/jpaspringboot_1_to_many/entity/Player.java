package org.northernarc.jpaspringboot_1_to_many.entity;

import jakarta.persistence.*;

@Entity
public class Player {
    @Id
    @GeneratedValue()
    private Long id;
    private String name;
    private int age;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Team team;
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
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
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}