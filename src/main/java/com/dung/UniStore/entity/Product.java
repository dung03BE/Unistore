package com.dung.UniStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="products")
@Data
@Getter
@Setter
@NoArgsConstructor

//listen exchance from PRODUCTlISTENER

public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name", length = 350)
    private String name;
    @Column(name="price",nullable = false)
    private float price;
    @Column(length = 350)
    private String thumbnail ;
    private String description;

    @ManyToOne
    @JoinColumn(name="category_id" ,referencedColumnName = "id")
    private Category category;


}
