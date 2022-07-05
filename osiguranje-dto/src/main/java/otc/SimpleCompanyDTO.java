package otc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCompanyDTO {

    private Long id;
    private Long registrationID;
    private String name;
    private Long taxID;
    private Long industrialClassificationID;
    private String address;


}
