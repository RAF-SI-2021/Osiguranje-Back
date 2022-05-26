package app.model;

public enum OptionType {
    CALL("calls"), PUT("puts");

    private String str;
    private OptionType(String str) {
        this.str = str;
    }

    @Override
    public String toString(){
        return str;
    }
}
