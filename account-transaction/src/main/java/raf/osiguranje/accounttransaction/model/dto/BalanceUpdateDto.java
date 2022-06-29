package raf.osiguranje.accounttransaction.model.dto;


public class BalanceUpdateDto {

    private Long accountId;
    private Long securityId;
    private int amount;


    public BalanceUpdateDto(Long accountId, Long securityId, int amount) {
        this.accountId = accountId;
        this.securityId = securityId;
        this.amount = amount;
    }

    public BalanceUpdateDto() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
