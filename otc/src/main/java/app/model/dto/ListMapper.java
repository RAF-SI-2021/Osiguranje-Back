package app.model.dto;

import app.model.Company;

import java.util.ArrayList;
import java.util.List;

public class ListMapper {

    private ListMapper() {}

    public static List<CompanyDTO> companyToCompanyDTO(List<Company> companies){
        List<CompanyDTO> companyDTOS = new ArrayList<>();
        for (Company company : companies){
            companyDTOS.add(new CompanyDTO(company));
        }
        return companyDTOS;
    }
}
