package crudApp.mappers;

import crudApp.model.PermissionAuthority;
import crudApp.model.Permissions;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public PermissionAuthority permissionsToPermissionAuthority(Permissions permissions) {
        return new PermissionAuthority(permissions);
    }
}
