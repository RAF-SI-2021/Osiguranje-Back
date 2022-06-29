package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;


public class BalanceDTO {

    private Long accountId;

    private Long securityId;
    private SecurityType securityType;

    private int amount;

    /*
    Ove dve promenljive se ne koriste pri kreiranju objekta u sistemu,
    ali se koriste kada se vraca na frontend
     */
    private int reserved;
    private int available;

    public BalanceDTO(Long accountId, Long securityId, SecurityType securityType, int amount, int reserved, int available) {
        this.accountId = accountId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.amount = amount;
        this.reserved = reserved;
        this.available = available;
    }

    public BalanceDTO() {
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

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "BalanceDTO{" +
                "accountId=" + accountId +
                ", securityId=" + securityId +
                ", securityType=" + securityType +
                ", amount=" + amount +
                ", reserved=" + reserved +
                ", available=" + available +
                '}';
    }
}
