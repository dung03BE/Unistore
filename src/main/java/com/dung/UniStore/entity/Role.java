package com.dung.UniStore.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="`name`",length = 100, nullable = false)
    private String name;
    public static String ADMIN="ADMIN";
    public static String USER="USER";
    @Column(name="`description`")
    private String description;
    @ManyToMany
    Set<Permission> permissions;
}
