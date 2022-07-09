import app.model.Employee;
import app.model.dto.EmployeeDTO;
import app.repositories.EmployeeRepository;
import app.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    private EmployeeRepository employeeRepository = mock(EmployeeRepository.class);

    private EmployeeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmployeeService(employeeRepository);
    }

    @Test
    public void save_CallsEmployeeRepositorySave_Once() {
        Employee employee = new Employee(new EmployeeDTO(1L, "NAME", "SURNAME", "PHONE",
                                                    "EMAIL", "COMPANY_POSITION",
                                                "DESCRIPTION"));
        underTest.save(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void deleteByID_CallsEmployeeRepositoryDeleteById_Once() {
        Long id = 1L;
        underTest.deleteByID(id);
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    public void findById_CallsEmployeeRepositoryFindById_Once() {
        Long id = 1L;
        underTest.findByID(id);
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    public void update_CallsEmployeeServiceSave_Once() {
        EmployeeService spy = spy(underTest);
        Employee employee = new Employee(new EmployeeDTO(1L, "NAME", "SURNAME", "PHONE",
                "EMAIL", "COMPANY_POSITION",
                "DESCRIPTION"));
        EmployeeDTO employeeDTO = new EmployeeDTO(2L, "NAME2", "SURNAME2", "PHONE2",
                "EMAIL2", "COMPANY_POSITION2",
                "DESCRIPTION2");

        spy.update(employee, employeeDTO);

        verify(spy, times(1)).save(employee);
        assertEquals(employeeDTO.getName(), employee.getName());
        assertEquals(employeeDTO.getSurname(), employee.getSurname());
        assertEquals(employeeDTO.getPhone(), employee.getPhone());
        assertEquals(employeeDTO.getEmail(), employee.getEmail());
        assertEquals(employeeDTO.getCompanyPosition(), employee.getCompanyPosition());
        assertEquals(employeeDTO.getDescription(), employee.getDescription());
    }
}
