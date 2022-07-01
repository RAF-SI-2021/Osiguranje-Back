package app.model.dto;

import app.model.Company;

public class SimpleCompanyDTO {

    private Long id;
    private Long registrationID;
    private String name;
    private Long taxID;
    private Long industrialClassificationID;
    private String address;

    public SimpleCompanyDTO(Company company) {
        this.id = company.getId();
        this.registrationID = company.getRegistrationID();
        this.name = company.getName();
        this.taxID = company.getTaxID();
        this.industrialClassificationID = company.getIndustrialClassificationID();
        this.address = company.getAddress();
    }
}
