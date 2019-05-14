package com.example.demo.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private int id;
    @Column(name = "c_name")
    private String name;
}
