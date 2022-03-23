package crudApp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class PermissionAuthority implements GrantedAuthority {
    private Permissions permissions;

    @Autowired
    public PermissionAuthority(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
