package otc.model.dto;

import lombok.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @NotNull
    private Long registrationID;
    @NotNull
    private String name;
    @NotNull
    private Long taxID;
    @NotNull
    private Long industrialClassificationID;
    @NotNull
    private String address;
    private Set<Employee> employees;
}
