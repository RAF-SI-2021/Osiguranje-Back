package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;
    private String email;
    private String position;
    private String phoneNumber;
}

