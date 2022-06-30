package app.services;

import app.model.Employee;
import app.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void save(Employee employee){
        employeeRepository.save(employee);
    }

    public void deleteByID(Long id){
        employeeRepository.deleteById(id);
    }

}
