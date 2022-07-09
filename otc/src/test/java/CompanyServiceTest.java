import app.model.Company;
import app.model.dto.BankAccountDTO;
import app.model.dto.CompanyDTO;
import app.model.dto.EmployeeDTO;
import app.repositories.CompanyRepository;
import app.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {
    private CompanyRepository companyRepository = mock(CompanyRepository.class);

    private CompanyService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CompanyService(companyRepository);
    }

    @Test
    public void save_CallsCompanyRepositorySave_Once() {
        Company company = new Company(new CompanyDTO(1L, 1L, "NAME",
                                                1L, 1L, "ADDRESS",
                                                    new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>()));
        underTest.save(company);
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    public void findAll_CallsCompanyRepositoryFindAll_Once() {
        underTest.findAll();
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    public void findById_CallsCompanyRepositoryFindById_Once() {
        Long id = 1L;
        underTest.findByID(id);
        verify(companyRepository, times(1)).findById(id);
    }

    @Test
    public void findByRegistrationID_CallsCompanyRepositoryFindAllByRegistrationID_Once() {
        Long id = 1L;
        underTest.findByRegistrationID(id);
        verify(companyRepository, times(1)).findAllByRegistrationID(id);
    }

    @Test
    public void findByTaxID_CallsCompanyRepositoryFindAllByTaxID_Once() {
        Long id = 1L;
        underTest.findByTaxID(id);
        verify(companyRepository, times(1)).findAllByTaxID(id);
    }

    @Test
    public void deleteByID_CallsCompanyRepositoryDeleteById_Once() {
        Long id = 1L;
        underTest.deleteByID(id);
        verify(companyRepository, times(1)).deleteById(id);
    }

    @Test
    public void update_ReturnsNullIfCompanyIsNull() {
        CompanyDTO companyDTO = new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>());

        when(companyRepository.findById(companyDTO.getId())).thenReturn(Optional.empty());

        CompanyDTO actual = underTest.update(companyDTO);

        assertNull(actual);
        verify(companyRepository, times(1)).findById(companyDTO.getId());
    }

    @Test
    public void update_CallsCompanyRepositorySave_Once() {
        CompanyDTO companyDTO = new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>());
        CompanyDTO companyDTO2 = new CompanyDTO(2L, 2L, "NAME2",
                2L, 2L, "ADDRESS2",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>());

        Company company = new Company(companyDTO2);

        Company expectedSavedCompany = new Company(companyDTO2);
        expectedSavedCompany.setName(companyDTO.getName());
        expectedSavedCompany.setAddress(companyDTO.getAddress());
        expectedSavedCompany.setIndustrialClassificationID(companyDTO.getIndustrialClassificationID());

        when(companyRepository.findById(companyDTO.getId())).thenReturn(Optional.of(company));
        CompanyDTO expected = new CompanyDTO(expectedSavedCompany);
        CompanyDTO actual = underTest.update(companyDTO);

        verify(companyRepository, times(1)).findById(companyDTO.getId());
        verify(companyRepository, times(1)).save(company);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getTaxID(), actual.getTaxID());
        assertEquals(expected.getIndustrialClassificationID(), actual.getIndustrialClassificationID());
    }
}
