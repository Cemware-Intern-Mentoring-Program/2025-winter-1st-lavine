package com.cemware.lavine.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    protected User() { }    // JPA용 기본 생성자

    public User(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Group> getGroups() { return groups; }
    public List<Task> getTasks() { return tasks; }
}
