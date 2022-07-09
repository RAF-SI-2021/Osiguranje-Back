package app.model.dto;

import lombok.Getter;

@Getter
public class CreateContractDTO {
    private Long companyID;
    private String refNumber;
    private String description;
}
