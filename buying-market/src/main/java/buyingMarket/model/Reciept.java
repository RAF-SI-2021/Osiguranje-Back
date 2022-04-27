package buyingMarket.model;

import lombok.*;

import javax.persistence.Entity;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reciept {
    public long userId;

    public List<Transaction> transactions;
}
