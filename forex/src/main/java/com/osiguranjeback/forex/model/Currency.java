package com.osiguranjeback.forex.model;

import lombok.Getter;

@Getter
public enum Currency {
    EURO("EUR"), DOLLAR("USD"), BRITISH_POUND("GBP");

    private String code;

    Currency(String code) {
        this.code = code;
    }
}
