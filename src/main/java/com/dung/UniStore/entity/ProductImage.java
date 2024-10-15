package com.dung.UniStore.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    public static  final int Maximum_Images_Per_Product=5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="product_id" ,referencedColumnName = "id")
    private Product product;
    @Column(name="image_url")
    private String imageUrl;
}
