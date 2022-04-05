package crudApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
    private Long id;
    private String currentPassword;
    private String newPassword;

    public PasswordDto(Long id, String currentPassword, String newPassword) {
        this.id = id;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}