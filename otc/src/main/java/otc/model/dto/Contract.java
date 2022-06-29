package otc.model.dto;

import javax.validation.constraints.NotNull;

public class Contract {

    @NotNull
    private Long contractID;
    private Company employer;
    @NotNull
    private String status;
    @NotNull
    private String creationDate;
    private String lastEdit;
    @NotNull
    private String description;
}
