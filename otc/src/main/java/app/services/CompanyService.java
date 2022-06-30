package app.services;

import app.model.Company;
import app.model.dto.CompanyDTO;
import app.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void save(Company company){
        companyRepository.save(company);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findByID(Long id){
        return companyRepository.findById(id);
    }

    public List<Company> findByRegistrationID(Long registrationID){
        return companyRepository.findAllByRegistrationID(registrationID);
    }

    public List<Company> findByName(String name){
        return companyRepository.findAllByName(name);
    }

    public List<Company> findByTaxID(Long taxID){
        return companyRepository.findAllByTaxID(taxID);
    }

    public void deleteByID(Long id){
        companyRepository.deleteById(id);
    }

    public CompanyDTO update(CompanyDTO companyDTO){
        Optional<Company> optionalCompany = companyRepository.findById(companyDTO.getID());
        if(optionalCompany.isEmpty())
            return null;
        Company company = optionalCompany.get();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setIndustrialClassificationID(companyDTO.getIndustrialClassificationID());
        companyRepository.save(company);

        return(new CompanyDTO(company));
    }

}
