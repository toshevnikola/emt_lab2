package com.example.emtlab2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {
    public String currency;
    private String description;
    private int amount;
    private String stripeToken;
}