package app.model.api;

public enum SecurityType {
    STOCKS, FOREX, FUTURES, OPTIONS, CURRENCY;

    @Override
    public String toString(){
        return name();
    }
}

