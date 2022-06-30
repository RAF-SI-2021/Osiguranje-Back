package app.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String telNumber;
    @NotNull
    private String email;
    @NotNull
    private String companyPosition;
    @NotNull
    private String description;
    private Company employer;
}
