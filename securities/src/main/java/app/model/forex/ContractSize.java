package app.model.forex;

import lombok.Getter;

@Getter
public enum ContractSize {
    MICRO(1000), MINI(10000), STANDARD(100000);

    private final int size;

    ContractSize(int size) {
        this.size = size;
    }
}
