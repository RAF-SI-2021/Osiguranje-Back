package app.controller;

import app.model.BankAccount;
import app.model.Company;
import app.model.Employee;
import app.model.dto.BankAccountDTO;
import app.model.dto.CompanyDTO;
import app.model.dto.EmployeeDTO;
import app.model.dto.ListMapper;
import app.services.BankAccountService;
import app.services.CompanyService;
import app.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;

    @Autowired
    public CompanyController(CompanyService companyService, EmployeeService employeeService, BankAccountService bankAccountService) {
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.bankAccountService = bankAccountService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCompanies(@RequestParam(required = false) String name, @RequestParam(required = false) Long registrationID, @RequestParam(required = false) Long taxID){
        List<CompanyDTO> data;

        if(registrationID != null)
            data = ListMapper.companyToCompanyDTO(companyService.findByRegistrationID(registrationID));
        else if(taxID != null)
            data = ListMapper.companyToCompanyDTO(companyService.findByTaxID(taxID));
        else if(name != null)
            data = ListMapper.companyToCompanyDTO(companyService.findByName(name));
        else
            data = ListMapper.companyToCompanyDTO(companyService.findAll());

        return ResponseEntity.ok(data);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCompanyByID(@NotNull @PathVariable Long id){
        Optional<Company> optionalCompany = companyService.findByID(id);

        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();

        Company company = optionalCompany.get();
        return ResponseEntity.ok(company);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.save(new Company(companyDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.update(companyDTO));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@NotNull @PathVariable Long id) {
        Optional<Company> optionalCompany = companyService.findByID(id);
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();

        Company company = optionalCompany.get();
        if(!company.getContracts().isEmpty())
            return ResponseEntity.badRequest().build();

        companyService.deleteByID(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createEmployee(@NotNull @PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        Optional<Company> optionalCompany = companyService.findByID(id);
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();
        Company company = optionalCompany.get();

        Employee employee = new Employee(employeeDTO);
        employee.setCompany(company);
        employeeService.save(employee);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/bank-accounts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createBankAccount(@NotNull @PathVariable Long id, @RequestBody BankAccountDTO bankAccountDTO) {
        Optional<Company> optionalCompany = companyService.findByID(id);

        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();
        Company company = optionalCompany.get();

        BankAccount bankAccount = new BankAccount(bankAccountDTO);
        bankAccount.setCompany(company);
        bankAccountService.save(bankAccount);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/{id}/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateEmployee(@NotNull @PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        Optional<Company> optionalCompany = companyService.findByID(id);
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();

        Optional<Employee> optionalEmployee = employeeService.findByID(employeeDTO.getId());
        if(optionalEmployee.isEmpty())
            return ResponseEntity.badRequest().build();

        Employee employee = optionalEmployee.get();
        employeeService.update(employee, employeeDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/{id}/bank-accounts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateBankAccount(@NotNull @PathVariable Long id, @RequestBody BankAccountDTO bankAccountDTO) {
        Optional<Company> optionalCompany = companyService.findByID(id);
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();

        Optional<BankAccount> optionalBankAccount = bankAccountService.findByID(bankAccountDTO.getId());
        if(optionalBankAccount.isEmpty())
            return ResponseEntity.badRequest().build();

        BankAccount bankAccount = optionalBankAccount.get();

        if(!bankAccountDTO.getAccountNumber().equals(bankAccount.getAccontNumber()))
            return ResponseEntity.badRequest().build();

        bankAccountService.update(bankAccount, bankAccountDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{companyID}/employees/{employeeID}")
    public ResponseEntity<?> deleteEmployee(@NotNull @PathVariable Long companyID, @NotNull @PathVariable Long employeeID) {
        employeeService.deleteByID(employeeID);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{companyID}/bank-accounts/{accountID}")
    public ResponseEntity<?> deleteBankAccount(@NotNull @PathVariable Long companyID, @NotNull @PathVariable Long accountID) {
        bankAccountService.deleteByID(accountID);
        return ResponseEntity.ok().build();
    }

}
