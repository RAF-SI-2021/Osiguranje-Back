package app.services;

import app.model.TransactionItem;
import app.model.dto.TransactionItemDTO;
import app.repositories.TransactionItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionItemServiceTest {

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private TransactionItemService underTest;

    @Test
    void findByID() {
        /* Given. */
        TransactionItem transaction = transactionItemRepository.save( new TransactionItem() );
        final long ID = transaction.getId();

        /* When. */
        Optional<TransactionItem> result = underTest.findByID( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        transaction = result.get();
        assertEquals( transaction.getId(), ID );
    }

    @Test
    void deleteByID() {
        /* Given. */
        TransactionItem transaction = transactionItemRepository.save( new TransactionItem() );
        final long ID = transaction.getId();

        /* When. */
        underTest.deleteByID( ID );
        Optional<TransactionItem> result = underTest.findByID( ID );

        /* Then. */
        assertFalse( result.isPresent() );
    }

    @Test
    void update() {
        /* Given. */
        int amount = 100;
        TransactionItem transaction = new TransactionItem();
        transaction.setAmount( amount );
        transaction = transactionItemRepository.save( transaction );

        amount = 200;
        transaction.setAmount( amount );
        TransactionItemDTO dto = new TransactionItemDTO( transaction );

        /* When. */
        underTest.update( dto );
        Optional<TransactionItem> result = transactionItemRepository.findById( transaction.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        assertEquals( result.get().getAmount(), amount );
    }
}