package app.services;

import app.model.Employee;
import app.model.dto.EmployeeDTO;
import app.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService underTest;

    @Test
    void save() {
        /* Given. */
        Employee employee = new Employee();

        /* When. */
        Employee saved = underTest.save( employee );
        Optional<Employee> result = employeeRepository.findById( saved.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        employee = result.get();
        assertEquals( employee.getId(), saved.getId() );
    }

    @Test
    void deleteByID() {
        /* Given. */
        Employee employee = employeeRepository.save( new Employee() );
        final long ID = employee.getId();

        /* When. */
        underTest.deleteByID( ID );
        Optional<Employee> result = underTest.findByID( ID );

        /* Then. */
        assertFalse( result.isPresent() );
    }

    @Test
    void findByID() {
        /* Given. */
        Employee employee = employeeRepository.save( new Employee() );
        final long ID = employee.getId();

        /* When. */
        Optional<Employee> result = underTest.findByID( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        employee = result.get();
        assertEquals( employee.getId(), ID );
    }

    @Test
    void update() {
        /* Given. */
        String name = "TEST_NAME";
        Employee employee = new Employee();
        employee.setName( name );
        employee = employeeRepository.save( employee );

        name = "UPDATED_NAME";
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName( name );

        /* When. */
        underTest.update( employee, dto );

        Optional<Employee> result = employeeRepository.findById( employee.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        assertEquals( result.get().getName(), name );
    }
}