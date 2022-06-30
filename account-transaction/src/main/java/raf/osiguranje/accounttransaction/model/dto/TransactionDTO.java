package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;
;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionDTO {

    private Long accountId;

    private LocalDateTime timestamp;

    private Long orderId;

    private Long userId;

    private Long forexId;

    private String text;

    private int payment;

    private int payout;

    private int reserve;

    private int usedReserve;

}
