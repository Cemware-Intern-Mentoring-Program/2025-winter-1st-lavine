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

    @Column(nullable = false, unique = true, length = 100)
    private String email;  // username으로 사용

    @Column(nullable = false)
    private String password;  // 암호화된 비밀번호

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    // 기본 생성자 (테스트용, 기존 코드 호환)
    public User(String name) {
        changeName(name);
        this.email = name + "@example.com";  // 임시 이메일
        this.password = "temp";  // 임시 비밀번호
    }

    // JWT 인증용 생성자
    public User(String name, String email, String password) {
        changeName(name);
        this.email = email;
        this.password = password;
    }

    public void changeName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
        this.name = name;
    }

}
