package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionDTO {

    private Long id;

    private Long accountId;

    private LocalDateTime timestamp;

    private OrderDto orderDto;

    private Long userId;

    private Long currencyId;

    private Long securityId;

    private SecurityType securityType;

    private String text;

    private int payment;

    private int payout;

    private int reserve;

    private int usedReserve;

    private TransactionType transactionType;
}
