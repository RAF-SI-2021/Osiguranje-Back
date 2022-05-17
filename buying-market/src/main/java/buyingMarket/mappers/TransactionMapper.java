package buyingMarket.mappers;

import buyingMarket.model.Transaction;
import buyingMarket.model.dto.TransactionDto;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDto transactionToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .time(transaction.getTime())
                .price(transaction.getPrice())
                .volume(transaction.getVolume())
                .orderId(transaction.getOrder().getOrderId())
                .build();
    }

    public Set<TransactionDto> transactionsToTransactionDtos(Set<Transaction> transactions) {
        Set<TransactionDto> transactionDtos = new HashSet<>();
        transactions.stream().forEach(transaction -> {
            transactionDtos.add(transactionToTransactionDto(transaction));
        });
        return transactionDtos;
    }
}
