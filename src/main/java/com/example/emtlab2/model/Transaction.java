package com.example.emtlab2.model;

import com.stripe.model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    @ManyToOne
    private User senderUser;
    private String object;
    private Long amount;
    private Long amountRefunded;
    private String application;
    private String applicationFee;
    private Boolean captured;
    private Long created;
    private String currency;
    private String description;
    private Boolean paid;
    private Boolean refunded;
    private String status;
    private String disputed;

}
