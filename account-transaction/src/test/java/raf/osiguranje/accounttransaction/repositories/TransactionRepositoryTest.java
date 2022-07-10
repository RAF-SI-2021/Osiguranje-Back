package raf.osiguranje.accounttransaction.repositories;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository underTest;


    private List<Transaction> generateDummyTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        for( int i = 0; i < 10; i++ )
        {
            Transaction transaction = new Transaction(
                    ( long ) i,
                    ( long ) i,
                    ( long ) i,
                    ( long ) i,
                    i, i, i, i,
                    "", TransactionType.SELL );
            transactions.add( transaction );

        }

        return transactions;
    }


    @Test
    @Disabled
    void findAllByAccountId() {
        /* Given. */
        final long ACCOUNT_ID = 1L;
        List<Transaction> transactions = underTest.saveAll( generateDummyTransactions() );

        /* When. */
        List<Transaction> search = underTest.findAllByAccountId( ACCOUNT_ID );

        /* Then. */
        for( Transaction t: search )
        {
            assertEquals( ACCOUNT_ID, t.getAccountId() );
        }
    }

    @Test
    void findAllByCurrencyId() {
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void findAllByOrderId() {
    }
}