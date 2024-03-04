package com.letseat.stock.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long quantity;

    @Version
    private Long version;

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Stock(Long id, Long productId, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        this.quantity -= quantity;
    }
}
