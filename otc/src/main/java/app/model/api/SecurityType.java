package app.model.api;

public enum SecurityType {
    STOCK, FOREX, FUTURE, OPTION, CURRENCY;

    @Override
    public String toString(){
        return name();
    }
}

