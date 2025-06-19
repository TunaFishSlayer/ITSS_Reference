package com.darian.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {
    // Primary key
    @Id
    @Column(name =  "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Name of the category
    @Column(name = "category_name", nullable = false)
    private String name;

    // Description of the category
    @Column(name = "category_description")
    private String description;
}
