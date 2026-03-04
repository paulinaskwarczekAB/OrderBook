package com.trade.model;

import jakarta.persistence.Entity;

@Entity
public class ExchangeBook extends Sort {
    public ExchangeBook() { super(); }
    public ExchangeBook(String region) { super(region); }
}