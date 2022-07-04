package app.model;

public enum TransactionType {
    BUY, SELL;

    @Override
    public String toString(){
        return name();
    }
}
