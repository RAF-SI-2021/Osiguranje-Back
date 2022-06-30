package app.repositories;

import app.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository  extends JpaRepository<Company, Long> {
    List<Company> findAllByName(String name);
    List<Company> findAllByRegistrationID(Long registrationID);
    List<Company> findAllByTaxID(Long taxID);
}
