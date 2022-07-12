package raf.osiguranje.accounttransaction.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.TransactionDTO;
import raf.osiguranje.accounttransaction.model.dto.TransactionOtcDto;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;
import raf.osiguranje.accounttransaction.services.TransactionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    private TransactionController underTest;

    @BeforeEach
    void setUp() {
        underTest = new TransactionController( transactionService );
    }

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
    void createTransaction() throws Exception{
        /* Given. */
        Transaction transaction = new Transaction( 1L, 2L, 3L, 4L, 5, 6, 7, 8, "", TransactionType.SELL );
        TransactionDTO saveDto = transaction.getDto();
        when( transactionService.createTransaction( saveDto, "" ) ).thenReturn( transaction );

        /* When. */
        ResponseEntity<?> response = underTest.createTransaction( saveDto, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        TransactionDTO dto = ( TransactionDTO ) response.getBody();
        assertEquals( transaction.getId(), dto.getId() );
    }

    @Test
    void createTransaction_Error() throws Exception{
        /* Given. */
        Transaction transaction = new Transaction( 1L, 2L, 3L, 4L, 5, 6, 7, 8, "", TransactionType.SELL );
        TransactionDTO saveDto = transaction.getDto();
        when( transactionService.createTransaction( saveDto, "" ) ).thenThrow( Exception.class );

        /* When. */
        ResponseEntity<?> response = underTest.createTransaction( saveDto, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void createTransactionOtc_Error() throws Exception{
        /* Given. */
        TransactionOtcDto saveDto = new TransactionOtcDto();
        when( transactionService.createTransactionOtc( saveDto, "" ) ).thenThrow( Exception.class );

        /* When. */
        ResponseEntity<?> response = underTest.createTransactionOtc( saveDto, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void createTransactionOtc() throws Exception{
        /* Given. */
        Transaction transaction = new Transaction( 1L, 2L, 3L, 4L, 5, 6, 7, 8, "", TransactionType.SELL );
        TransactionOtcDto saveDto = new TransactionOtcDto();
        when( transactionService.createTransactionOtc( saveDto, "" ) ).thenReturn( transaction );

        /* When. */
        ResponseEntity<?> response = underTest.createTransactionOtc( saveDto, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        TransactionOtcDto dto = ( TransactionOtcDto ) response.getBody();
        assertEquals( saveDto.getId(), dto.getId() );
    }

    @Test
    void findAllTransactions() {
        /* Given. */
        List<Transaction> transactions = generateDummyTransactions();
        when( transactionService.getAllTransactions() ).thenReturn( transactions );

        /* When. */
        ResponseEntity<List<TransactionDTO>> response = underTest.findAllTransactions( "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( transactions.size(), response.getBody().size() );
    }

    @Test
    void findAllTransactionsByAccount() {
        /* Given. */
        final long ACCOUNT_ID = 1L;
        List<Transaction> transactions = generateDummyTransactions();
        when( transactionService.getTransactionsByAccount( ACCOUNT_ID ) ).thenReturn( transactions );

        /* When. */
        ResponseEntity<List<TransactionDTO>> response = underTest.findAllTransactionsByAccount( ACCOUNT_ID,"" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( transactions.size(), response.getBody().size() );
    }

    @Test
    void findAllTransactionsByForex() {
        /* Given. */
        final long FOREX_ID = 1L;
        List<Transaction> transactions = generateDummyTransactions();
        when( transactionService.getTransactionsByCurrency( FOREX_ID ) ).thenReturn( transactions );

        /* When. */
        ResponseEntity<List<TransactionDTO>> response = underTest.findAllTransactionsByForex( FOREX_ID,"" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( transactions.size(), response.getBody().size() );
    }

    @Test
    void findAllTransactionsByUser() {
        /* Given. */
        final long USER_ID = 1L;
        List<Transaction> transactions = generateDummyTransactions();
        when( transactionService.getTransactionsByUser( USER_ID ) ).thenReturn( transactions );

        /* When. */
        ResponseEntity<List<TransactionDTO>> response = underTest.findAllTransactionsByUser( USER_ID,"" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( transactions.size(), response.getBody().size() );
    }

    @Test
    void findAllTransactionsByOrder() {
        /* Given. */
        final long ORDER_ID = 1L;
        List<Transaction> transactions = generateDummyTransactions();
        when( transactionService.getTransactionsByOrderId( ORDER_ID ) ).thenReturn( transactions );

        /* When. */
        ResponseEntity<List<TransactionDTO>> response = underTest.findAllTransactionsByOrder( ORDER_ID,"" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( transactions.size(), response.getBody().size() );
    }
}