package app.controller;

import app.model.Company;
import app.model.dto.CompanyDTO;
import app.model.dto.ListMapper;
import app.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
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
    public ResponseEntity<HttpStatus> createCompany(@Valid @RequestBody CompanyDTO companyDTO, @RequestHeader("Authorization") String authorization) {
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


}
