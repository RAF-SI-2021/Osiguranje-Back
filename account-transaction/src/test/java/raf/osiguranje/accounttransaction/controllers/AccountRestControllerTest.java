package raf.osiguranje.accounttransaction.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class AccountRestControllerTest {

    @Mock
    private AccountService accountService;

    @Autowired
    private ObjectMapper mapper;

    private AccountRestController underTest;



    @BeforeEach
    void setUp() {
        underTest = new AccountRestController( accountService );
    }

    private List<Account> generateDummyAccounts() {

        List<Account> accounts = new ArrayList<>();

        for( long i = 0; i < 10; i++ )
        {
            accounts.add( new Account( i, AccountType.CASH ) );
        }

        return accounts;
    }

    @Test
    void findAllAccounts() {
        /* Given. */
        List<Account> accounts = generateDummyAccounts();
        when( accountService.getAllAccounts() ).thenReturn( accounts );

        /* When. */
        ResponseEntity<List<Account>> result = underTest.findAllAccounts();

        /* Then. */
        assertTrue( result.getStatusCode().is2xxSuccessful() );
        assertEquals( accounts.size(), result.getBody().size() );

        int i = 0;
        for( Account a: accounts )
        {
            assertEquals( a, result.getBody().get( i++ ) );
        }
    }

    @Test
    void findAccount_WithIdArgumentAndAccountNotFound() {
        /* Given. */
        when( accountService.findAccountById( -1L ) ).thenReturn( null );

        /* When. */
        ResponseEntity<?> result = underTest.findAccount( Optional.of( -1L ), Optional.empty() );

        /* Then. */
        assertTrue( result.getStatusCode().is4xxClientError() );
    }

    @Test
    void findAccount_WithIdArgumentAndAccountExists() {
        /* Given. */
        Account account = new Account( 1L, AccountType.CASH );
        when( accountService.findAccountById( 1L ) ).thenReturn( account );

        /* When. */
        ResponseEntity<?> result = underTest.findAccount( Optional.of( 1L ), Optional.empty() );

        /* Then. */
        assertTrue( result.getStatusCode().is2xxSuccessful() );
    }

    @Test
    void findAccount_WithoutIdArgumentAndWrongType() {
        /* When. */
        ResponseEntity<?> result = underTest.findAccount( Optional.empty(), Optional.of( "WRONG_TYPE" ) );

        /* Then. */
        assertTrue( result.getStatusCode().is4xxClientError() );
    }

    @Test
    void findAccount_WithoutIdArgumentAndValidType() {
        /* When. */
        ResponseEntity<?> result = underTest.findAccount( Optional.empty(), Optional.of( "CASH" ) );

        /* Then. */
        assertTrue( result.getStatusCode().is2xxSuccessful() );
    }

    @Test
    void findAccount_WithoutIdArgumentAndWithoutAccountNumber() {
        /* When. */
        ResponseEntity<?> result = underTest.findAccount( Optional.empty(), Optional.empty() );

        /* Then. */
        assertTrue( result.getStatusCode().is2xxSuccessful() );
    }

    @Test
    void createAccount_WithoutType() {
        /* Given. */
        Account account = new Account();
        when( accountService.createNewAccount() ).thenReturn( account );

        /* When. */
        ResponseEntity<?> response = underTest.createAccount( Optional.empty() );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        Account created = ( Account ) response.getBody();
        assertEquals( AccountType.CASH, created.getAccountType() );
    }

    @Test
    void createAccount_WithWrongType() {
        /* When. */
        ResponseEntity<?> response = underTest.createAccount( Optional.of( "WRONG_TYPE" ) );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void createAccount_WithType() throws JsonProcessingException {
        /* Given. */
        Account account = new Account();
        when( accountService.createNewAccount( AccountType.CASH ) ).thenReturn( account );

        /* When. */
        ResponseEntity<?> response = underTest.createAccount( Optional.of( "CASH" ) );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );

        Account created = ( Account ) response.getBody();
        assertEquals( AccountType.CASH, created.getAccountType() );
    }

    @Test
    void deleteAccount_WrongId() {
        /* When. */
        ResponseEntity<?> response = underTest.deleteAccount( -1L );

        /* Then. */
        assertTrue( response.getStatusCode().is4xxClientError() );
    }

    @Test
    void deleteAccount() {
        /* Given. */
        when( accountService.findAccountById( 1L ) ).thenReturn( new Account() );

        /* When. */
        ResponseEntity<?> response = underTest.deleteAccount( 1L );

        /* Then. */
        assertTrue( response.getStatusCode().is2xxSuccessful() );
    }
}