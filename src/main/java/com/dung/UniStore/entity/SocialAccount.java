package com.dung.UniStore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="social_accounts")
@Getter
@Setter
@NoArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String provider;
    @Column(name="provider_id",length = 20, nullable = false)
    private String providerId;
    @Column(name="email",length = 150, nullable = false)
    private String email;
    @Column(name="`name`",length = 200, nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name="user_id" , referencedColumnName = "id")
    private User user;
}
