package crudApp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

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
