package raf.osiguranje.accounttransaction.repositories;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import raf.osiguranje.accounttransaction.model.Transaction;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Disabled
class TransactionRepositoryTest {

    @Autowired TransactionRepository underTest;

    @Test
    void findAllByAccountId() {
        underTest.save( new Transaction() );
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