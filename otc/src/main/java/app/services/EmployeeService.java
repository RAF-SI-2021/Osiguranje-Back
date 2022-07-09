package app.services;

import app.model.Employee;
import app.model.dto.EmployeeDTO;
import app.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }

    public void deleteByID(Long id){
        employeeRepository.deleteById(id);
    }

    public Optional<Employee> findByID(Long id){
        return employeeRepository.findById(id);
    }

    public void update(Employee employee, EmployeeDTO employeeDTO) {
        employee.setName(employeeDTO.getName());
        employee.setSurname(employeeDTO.getSurname());
        employee.setPhone(employeeDTO.getPhone());
        employee.setEmail(employeeDTO.getEmail());
        employee.setCompanyPosition(employeeDTO.getCompanyPosition());
        employee.setDescription(employeeDTO.getDescription());
        save(employee);
    }
}
