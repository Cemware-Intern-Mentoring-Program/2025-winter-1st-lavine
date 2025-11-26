package com.cemware.lavine.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    protected Group() { }

    public Group(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public List<Task> getTasks() { return tasks; }
}
