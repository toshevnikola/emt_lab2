package com.example.emtlab2.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CartItem {

    @Id
    private Long id;
    @ManyToOne
    private Book book;

    @ManyToOne
    private ShoppingCart shoppingCart;
    private Integer quantity;

}
