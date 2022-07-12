package raf.osiguranje.accounttransaction.controllers;

import io.swagger.models.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.dto.BalanceDTO;
import raf.osiguranje.accounttransaction.model.dto.BalanceUpdateDto;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.services.BalanceService;
import raf.osiguranje.accounttransaction.services.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
class BalanceRestControllerTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private TransactionService transactionService;

    private BalanceRestController underTest;

    @BeforeEach
    void setUp() {
        underTest = new BalanceRestController( balanceService, transactionService );
    }

    private List<Balance> generateDummyBalances( Account account ) {
        List<Balance> balances = new ArrayList<>();

        for( int i = 0; i < 10; i++ )
        {
            Balance balance = new Balance( account, ( long ) i, SecurityType.CURRENCY, i );
            balances.add( balance );
        }

        return balances;
    }

    @Test
    void getAllBalance() {
        /* Given. */
        List<Balance> balances = generateDummyBalances( new Account() );
        when( balanceService.getAllBalances() ).thenReturn( balances );

        /* When. */
        ResponseEntity<List<BalanceDTO>> response = underTest.getAllBalance( "" );

        /* Then. */
        int i = 0;
        for( Balance b: balances )
        {
            assertEquals( b.getSecurityId(), response.getBody().get( i++ ).getSecurityId() );
        }
    }

    @Test
    void getBalance_WithoutAccountIdAndWithoutSecurityId() {
        /* When. */
        ResponseEntity<?> response = underTest.getBalance( "", Optional.empty(), Optional.empty() );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void getBalance_WithAccountIdAndWithoutSecurityId() {
        /* Given. */
        List<Balance> balances = generateDummyBalances( new Account( 1L, AccountType.CASH ) );
        when( balanceService.getBalancesByAccount( 1L ) ).thenReturn( balances );

        /* When. */
        ResponseEntity<List<BalanceDTO>> response
                = underTest.getBalance( "", Optional.of( 1L ), Optional.empty() );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        int i = 0;
        for( Balance b: balances )
        {
            assertEquals( b.getAccountId(), response.getBody().get( i++ ).getAccountId() );
        }
    }

    @Test
    void getBalance_WithoutAccountIdAndWithSecurityId() {
        /* Given. */
        List<Balance> balances = generateDummyBalances( new Account(1L, AccountType.CASH ) );
        when(balanceService.getBalancesBySecurity(1L)).thenReturn(balances);

        /* When. */
        ResponseEntity<List<BalanceDTO>> response
                = underTest.getBalance( "", Optional.empty(), Optional.of(1L ) );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        int i = 0;
        for( Balance b : balances )
        {
            assertEquals( b.getSecurityId(), response.getBody().get( i++ ).getSecurityId() );
        }
    }

    @Test
    void getBalance_WithAccountIdAndWithSecurityId() {
        /* Given. */
        List<Balance> balances = generateDummyBalances( new Account( 1L, AccountType.CASH ) );
        when(balanceService.getBalancesByAccountAndSecurity( 1L, 1L ) ).thenReturn( balances );

        /* When. */
        ResponseEntity<List<BalanceDTO>> response
                = underTest.getBalance("", Optional.of( 1L ), Optional.of( 1L ) );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        int i = 0;
        for( Balance b : balances )
        {
            assertEquals( b.getSecurityId(), response.getBody().get( i ).getSecurityId() );
            assertEquals( b.getAccountId(), response.getBody().get( i++ ).getAccountId() );
        }
    }

    @Test
    void saveBalance_WithError() throws Exception {
        /* Given. */
        BalanceDTO balanceDTO = new BalanceDTO( 1L, 1L, SecurityType.CURRENCY, 1, 1, 1 );
        when( balanceService.createBalance(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                balanceDTO.getAmount(),
                ""
        ) ).thenThrow( new Exception() );

        /* When. */
        ResponseEntity<?> response = underTest.saveBalance( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void saveBalance() throws Exception {
        /* Given. */
        BalanceDTO balanceDTO = new BalanceDTO( 1L, 1L, SecurityType.CURRENCY, 1, 1, 1 );
        when( balanceService.createBalance(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                balanceDTO.getAmount(),
                ""
        ) ).thenReturn( new Balance() );

        /* When. */
        ResponseEntity<?> response = underTest.saveBalance( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
    }

    @Test
    void deleteBalance_WithError() throws Exception {
        /* Given. */
        BalanceDTO balanceDTO = new BalanceDTO( 1L, 1L, SecurityType.CURRENCY, 1, 1, 1 );
        when( balanceService.deleteBalance(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                ""
        ) ).thenThrow( new Exception() );

        /* When. */
        ResponseEntity<?> response = underTest.deleteBalance( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void deleteBalance() throws Exception {
        /* Given. */
        BalanceDTO balanceDTO = new BalanceDTO( 1L, 1L, SecurityType.CURRENCY, 1, 1, 1 );
        when( balanceService.deleteBalance(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                ""
        ) ).thenReturn( true );

        /* When. */
        ResponseEntity<?> response = underTest.deleteBalance( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
    }

    @Test
    void updateAmount_Error() throws Exception {
        /* Given. */
        BalanceUpdateDto balanceDTO = new BalanceUpdateDto( 1L, 1L, SecurityType.CURRENCY, 1 );
        doThrow( Exception.class ).when( transactionService ).updateBalanceTransaction(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                balanceDTO.getAmount(),
                ""
        );

        /* When. */
        ResponseEntity<?> response = underTest.updateAmount( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void updateAmount() throws Exception {
        /* Given. */
        BalanceUpdateDto balanceDTO = new BalanceUpdateDto( 1L, 1L, SecurityType.CURRENCY, 1 );
        doNothing().when( transactionService ).updateBalanceTransaction(
                balanceDTO.getAccountId(),
                balanceDTO.getSecurityId(),
                balanceDTO.getSecurityType(),
                balanceDTO.getAmount(),
                ""
        );

        /* When. */
        ResponseEntity<?> response = underTest.updateAmount( balanceDTO, "" );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
    }
}