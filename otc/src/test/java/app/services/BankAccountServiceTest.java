package app.services;

import app.model.BankAccount;
import app.model.dto.BankAccountDTO;
import app.repositories.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService underTest;

    @Test
    void save() {
        /* Given. */
        BankAccount bankAccount = new BankAccount();

        /* When. */
        BankAccount saved = underTest.save( bankAccount );
        Optional<BankAccount> result = bankAccountRepository.findById( saved.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        bankAccount = result.get();
        assertEquals( bankAccount.getId(), saved.getId() );
    }

    @Test
    void deleteByID() {
        /* Given. */
        BankAccount company = bankAccountRepository.save( new BankAccount() );
        final long ID = company.getId();

        /* When. */
        underTest.deleteByID( ID );
        Optional<BankAccount> result = underTest.findByID( ID );

        /* Then. */
        assertFalse( result.isPresent() );
    }

    @Test
    void findByID() {
        /* Given. */
        BankAccount bankAccount = bankAccountRepository.save( new BankAccount() );
        final long ID = bankAccount.getId();


        /* When. */
        Optional<BankAccount> result = underTest.findByID( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        bankAccount = result.get();
        assertEquals( bankAccount.getId(), ID );
    }

    @Test
    void update() {
        /* Given. */
        String name = "TEST_NAME";
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBankName( name );

        bankAccount = bankAccountRepository.save( bankAccount );

        name = "UPDATED_NAME";
        BankAccountDTO accountDTO = new BankAccountDTO( bankAccount );
        accountDTO.setBankName( name );

        /* When. */
        BankAccount result = underTest.update( bankAccount, accountDTO );

        /* Then. */
        assertEquals( result.getBankName(), name );
    }
}