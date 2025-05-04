package ru.practicum.payment.service;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException() {
        super("Not enough funds");
    }
}
