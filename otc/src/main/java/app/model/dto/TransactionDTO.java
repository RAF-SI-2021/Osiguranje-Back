package app.model.dto;

import javax.persistence.Column;

public class TransactionDTO {

    private Long transactionID;
    private Action action;
    private String currency;
    private Long amount;
    private double pricePerShare;

    private enum Action{
        BUY, SELL
    }
}
