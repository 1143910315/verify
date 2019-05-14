package com.example.demo.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private int userId;
    @Column(name = "c_name")
    private String userName;
    @Column(name = "c_password")
    private String password;
    @OneToMany
    @JoinColumn
    private List<Role> roles;
}
