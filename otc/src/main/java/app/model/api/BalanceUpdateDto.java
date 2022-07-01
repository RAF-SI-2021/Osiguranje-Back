package app.model.api;


public class BalanceUpdateDto {

    private Long accountId;

    private Long securityId;

    private SecurityType securityType;

    private int amount;

    public BalanceUpdateDto(Long accountId, Long securityId, SecurityType securityType, int amount) {
        this.accountId = accountId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.amount = amount;
    }
}
