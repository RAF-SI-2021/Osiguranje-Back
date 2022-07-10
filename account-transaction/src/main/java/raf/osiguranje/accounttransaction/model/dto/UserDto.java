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
    private String email;
    private String position;
    private String phoneNumber;
    private Boolean active;
}

