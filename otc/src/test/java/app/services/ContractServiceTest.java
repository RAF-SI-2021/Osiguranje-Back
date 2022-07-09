package app.services;

import app.model.Contract;
import app.model.Status;
import app.model.TransactionItem;
import app.model.dto.TransactionItemDTO;
import app.repositories.ContractRepository;
import app.repositories.TransactionItemRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContractServiceTest {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private ContractService underTest;

    private TransactionItemDTO transactionItemDTOMock = Mockito.mock( TransactionItemDTO.class );

    @Test
    void save() {
        /* Given. */
        Contract contract = new Contract();

        /* When. */
        Contract saved = underTest.save( contract );
        Optional<Contract> result = contractRepository.findById( saved.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        contract = result.get();
        assertEquals( contract.getId(), saved.getId() );
    }

    @Test
    void findAll() {
        /* Given. */
        final int size = contractRepository.findAll().size();
        contractRepository.save( new Contract() );

        /* When. */
        final int resultSize = underTest.findAll().size();

        /* Then. */
        assertEquals( resultSize, size + 1 );
    }

    @Test
    void findByID() {
        /* Given. */
        Contract contract = contractRepository.save( new Contract() );
        final long ID = contract.getId();


        /* When. */
        Optional<Contract> result = underTest.findByID( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        contract = result.get();
        assertEquals( contract.getId(), ID );
    }

    @Test
    @Transactional
    void createTransactionItem() {
        /* Given. */
        Contract contract = new Contract();
        contract.setTransactionItems( new ArrayList<>() );
        contract.setLastUpdated();
        contract = contractRepository.save( contract );

        final String FIRST_UPDATED = contract.getLastUpdated();

        TransactionItem transaction = new TransactionItem();

        /* Spin for two seconds so we can see the change in the lastUpdated variable. */
        for( long start = System.currentTimeMillis(); System.currentTimeMillis() - start < 2000; );

        /* When. */
        underTest.createTransactionItem( contract, transaction );
        Optional<Contract> changedContract = contractRepository.findById( contract.getId() );

        /* Then. */
        assertTrue( changedContract.isPresent() );
        assertNotEquals( changedContract.get().getLastUpdated(),  FIRST_UPDATED );
        assertEquals(
                changedContract.get().getTransactionItems().size(), 1
        );
    }

    @Test
    @Transactional
    @Disabled
    /* This test is disabled because it needs to be @Transactional,
    however JPA doesn't allow deleting and saving in the same transaction. */
    void deleteTransactionItem() {
        /* Given. */
        Contract contract = new Contract();
        contract.setTransactionItems( new ArrayList<>() );
        contract = contractRepository.save( contract );

        TransactionItem transaction = new TransactionItem();
        underTest.createTransactionItem( contract, transaction );

        contract = contractRepository.findById( contract.getId() ).get();
        final long idToRemove = contract.getTransactionItems().get( 0 ).getId();

        /* When. */
        underTest.deleteTransactionItem( contract, idToRemove );
        Optional<Contract> changedContract = contractRepository.findById( contract.getId() );

        /* Then. */
        assertTrue( changedContract.isPresent() );
        assertTrue( contract.getTransactionItems().isEmpty() );
    }

    @Test
    @Transactional
    void updateTransactionItem() {
        /* Given. */
        Contract contract = new Contract();
        contract.setTransactionItems( new ArrayList<>() );
        contract = contractRepository.save( contract );

        final int AMOUNT = 100, CHANGED_AMOUNT = 200;

        TransactionItem transaction = new TransactionItem();
        transaction.setAmount( AMOUNT );
        underTest.createTransactionItem( contract, transaction );
        contract = contractRepository.findById( contract.getId() ).get();

        when( transactionItemDTOMock.getId() ).thenReturn( contract.getTransactionItems().get( 0 ).getId() );
        when( transactionItemDTOMock.getAmount() ).thenReturn( CHANGED_AMOUNT );

        /* When. */
        underTest.updateTransactionItem( contract, transactionItemDTOMock );
        Optional<Contract> changedContract = contractRepository.findById( contract.getId() );

        /* Then. */
        assertTrue( changedContract.isPresent() );
        assertEquals( contract.getTransactionItems().get( 0 ).getAmount(), CHANGED_AMOUNT );
    }

    @Test
    void finalizeContract() {
        /* Given. */
        Contract contract = new Contract();
        contract.setStatus( Status.DRAFT );
        contract = contractRepository.save( contract );

        /* When. */
        underTest.finalizeContract( contract );
        Optional<Contract> changedContract = contractRepository.findById( contract.getId() );

        /* Then. */
        assertTrue( changedContract.isPresent() );
        assertEquals( contract.getStatus(), Status.FINALIZED );
    }
}