package crudApp.dto;

public class PasswordDto {
    private Long id;
    private String currentPassword;
    private String newPassword;

    public PasswordDto() {}

    public PasswordDto(Long id, String currentPassword, String newPassword) {
        this.id = id;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
