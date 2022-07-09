package raf.osiguranje.accounttransaction.model.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionOtcDto {

    private Long id;

    private Long accountId;

    private Long securityId;
    private SecurityType securityType;

    private Long userId;

    private Long currencyId;

    private String text;

    private int payment;

    private int payout;

    private int reserve;

    private int usedReserve;

    private TransactionType transactionType;
}
