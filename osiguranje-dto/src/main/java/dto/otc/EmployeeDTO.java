package dto.otc;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String companyPosition;
    private String description;
}
