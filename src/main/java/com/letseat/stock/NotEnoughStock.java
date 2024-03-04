package com.letseat.stock;

public class NotEnoughStock extends RuntimeException{

    public NotEnoughStock(String message) {
        super(message);
    }
}
