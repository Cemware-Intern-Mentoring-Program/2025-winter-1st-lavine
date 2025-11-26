package com.cemware.lavine.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private boolean done = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Task() { }

    public Task(String title, Group group, User user) {
        this.title = title;
        this.group = group;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isDone() { return done; }
    public Group getGroup() { return group; }
    public User getUser() { return user; }
}

