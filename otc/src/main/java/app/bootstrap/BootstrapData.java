package app.bootstrap;

import app.model.BankAccount;
import app.model.Company;
import app.model.Employee;
import app.services.BankAccountService;
import app.services.CompanyService;
import app.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;

    @Autowired
    public BootstrapData(CompanyService companyService, EmployeeService employeeService, BankAccountService bankAccountService) {
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void run(String... args) throws Exception {
        Company company = new Company(111L, "Apple", 123L, 7L, "Palo Alto, CA");
        Employee employee = new Employee("Steve", "Jobs", "064213456", "stevejobs@apple.com", "CEO", "");
        BankAccount bankAccount = new BankAccount("25005845774890", "World Bank", "Cash");

        save(company, employee, bankAccount);

        company = new Company(222L, "Google", 456L, 5L, "London, UK");
        employee = new Employee("Bill", "Gates", "065288867", "billgates@google.com", "CTO", "");
        bankAccount = new BankAccount("111122223333", "UK Bank", "Cash");

        save(company, employee, bankAccount);

    }

    private void save(Company company, Employee employee, BankAccount bankAccount){
        companyService.save(company);

        employee.setCompany(company);
        employeeService.save(employee);

        bankAccount.setCompany(company);
        bankAccountService.save(bankAccount);

        company.getEmployees().add(employee);
        company.getBankAccounts().add(bankAccount);
        companyService.save(company);
    }

}
