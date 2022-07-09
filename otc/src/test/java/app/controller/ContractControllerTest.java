package app.controller;

import app.model.*;
import app.model.dto.ContractDTO;
import app.model.dto.CreateContractDTO;
import app.model.dto.TransactionItemDTO;
import app.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class ContractControllerTest {

    @Mock
    private ContractService contractService;
    @Mock
    private CompanyService companyService;
    @Mock
    private TransactionItemService transactionItemService;

    /* These models don't have setters so I have to mock them. */
    @Mock
    private CreateContractDTO createContractDTOMock;
    @Mock
    private TransactionItemDTO transactionItemDTOMock;


    private ContractController underTest;

    @BeforeEach
    void setUp() {
        underTest = new ContractController( contractService, companyService, transactionItemService );
    }

    private List<Contract> generateDummyContracts() {
        List<Contract> contracts = new ArrayList<>();
        Company company = new Company();
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );

        for( long i = 0; i < 10; i++ )
        {
            Contract contract = new Contract();
            contract.setId( i );
            contract.setCompany( company );
            contract.setTransactionItems( new ArrayList<>() );

            contracts.add( contract );
        }

        return contracts;
    }

    @Test
    void getContracts() {
        /* Given. */
        List<Contract> contracts = generateDummyContracts();
        when( contractService.findAll() ).thenReturn( contracts );

        /* When. */
        ResponseEntity<?> response = underTest.getContracts( null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
        assertEquals( ( ( List<ContractDTO> ) response.getBody() ).size(), contracts.size() );
    }

    @Test
    void getContract_WhenNotFound() {
        /* Given. */
        final long ID = 123;
        when( contractService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.getContract( ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void getContract() {
        /* Given. */
        final long ID = 123;

        Company company = new Company();
        company.setId( ID );
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );
        company.setContracts( new ArrayList<>() );

        Contract contract = new Contract();
        contract.setId( ID );
        contract.setCompany( company );
        contract.setTransactionItems( new ArrayList<>() );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );

        /* When. */
        ResponseEntity<?> response = underTest.getContract( ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void createContract_WhenCompanyNotFound() {
        /* Given. */
        final long ID = 123;


        when( createContractDTOMock.getCompanyID() ).thenReturn( ID );
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.createContract( createContractDTOMock, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void createContract() {
        /* Given. */
        final long ID = 123;

        Company company = new Company();
        company.setId( ID );
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );
        company.setContracts( new ArrayList<>() );


        when( createContractDTOMock.getCompanyID() ).thenReturn( ID );
        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        /* When. */
        ResponseEntity<?> response = underTest.createContract( createContractDTOMock, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void finalizeContract_WhenContractNotFound() {
        /* Given. */
        final long ID = 123;

        when( contractService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.finalizeContract( ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void finalizeContract() {
        /* Given. */
        final long ID = 123;

        Contract contract = new Contract();
        contract.setId( ID );
        contract.setTransactionItems( new ArrayList<>() );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );

        /* When. */
        ResponseEntity<?> response = underTest.finalizeContract( ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void createTransactionItem_WhenContractNotFound() {
        /* Given. */
        final long ID = 123;

        when( contractService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.createTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void createTransactionItem_WhenContractIsAlreadyFinalized() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.FINALIZED );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );

        /* When. */
        ResponseEntity<?> response = underTest.createTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    @Disabled
    void createTransactionItem() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.DRAFT );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );
        when( transactionItemDTOMock.getTransactionType() ).thenReturn( TransactionType.SELL );

        /* When. */
        ResponseEntity<?> response = underTest.createTransactionItem( ID, transactionItemDTOMock, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void deleteTransactionItem_WhenContractNotFound() {
        /* Given. */
        final long ID = 123;

        when( contractService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.deleteTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void deleteTransactionItem_WhenContractIsAlreadyFinalized() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.FINALIZED );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );

        /* When. */
        ResponseEntity<?> response = underTest.deleteTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void deleteTransactionItem_WhenTransactionIsNotFound() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.DRAFT );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );
        when( transactionItemService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.deleteTransactionItem( ID, ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    @Disabled
    void deleteTransactionItem() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.DRAFT );

        TransactionItem transaction = new TransactionItem();
        transaction.setId( ID );
        transaction.setTransactionType( TransactionType.SELL );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );
        when( transactionItemService.findByID( ID ) ).thenReturn( Optional.of( transaction ) );

        /* When. */
        ResponseEntity<?> response = underTest.deleteTransactionItem( ID, ID, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void updateTransactionItem_WhenContractNotFound() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );

        when( contractService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> response = underTest.updateTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateTransactionItem_WhenContractIsAlreadyFinalized() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.FINALIZED );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );

        /* When. */
        ResponseEntity<?> response = underTest.updateTransactionItem( ID, null, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateTransactionItem_WhenTransactionNotFound() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.DRAFT );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );
        when( transactionItemService.findByID( ID ) ).thenReturn( Optional.empty() );
        when( transactionItemDTOMock.getId() ).thenReturn( ID );

        /* When. */
        ResponseEntity<?> response = underTest.updateTransactionItem( ID, transactionItemDTOMock, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    @Disabled
    void updateTransactionItem() {
        /* Given. */
        final long ID = 123;
        Contract contract = new Contract();
        contract.setId( ID );
        contract.setStatus( Status.DRAFT );

        TransactionItem transaction = new TransactionItem();
        transaction.setTransactionType( TransactionType.SELL );

        System.out.println( transaction.getAmount() );

        when( contractService.findByID( ID ) ).thenReturn( Optional.of( contract ) );
        when( transactionItemService.findByID( ID ) ).thenReturn( Optional.of( transaction ) );
        when( transactionItemDTOMock.getId() ).thenReturn( ID );

        /* When. */
        ResponseEntity<?> response = underTest.updateTransactionItem( ID, transactionItemDTOMock, null );

        /* Then. */
        assertEquals( response.getStatusCode(), HttpStatus.OK );
    }

}