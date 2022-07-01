package app.model.dto;

import app.model.Company;
import app.model.Contract;

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

    public static List<ContractDTO> contractToContractDTO(List<Contract> contracts){
        List<ContractDTO> contractDTOS = new ArrayList<>();
        for (Contract contract : contracts){
            contractDTOS.add(new ContractDTO(contract));
        }
        return contractDTOS;
    }

}
