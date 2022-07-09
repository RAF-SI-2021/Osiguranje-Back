package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateContractDTO {
    private Long companyID;
    private String refNumber;
    private String description;
}
