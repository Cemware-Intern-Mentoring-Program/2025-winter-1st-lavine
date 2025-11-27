package com.cemware.lavine.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public User(String name) {
        changeName(name);
    }

    public void changeName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
        this.name = name;
    }

}
