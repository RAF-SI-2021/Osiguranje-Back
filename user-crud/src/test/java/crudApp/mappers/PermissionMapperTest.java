package crudApp.mappers;

import crudApp.model.PermissionAuthority;
import crudApp.model.Permissions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PermissionMapperTest {
    @Autowired
    private PermissionMapper testPermissionMapper;

    @Test
    void permissionsToPermissionAuthorityTest() {
        //given
        Permissions permissions = new Permissions(true, false, true, false, true, false);
        //when
        PermissionAuthority permissionAuthority = testPermissionMapper.permissionsToPermissionAuthority(permissions);
        //then
        assertThat(permissionAuthority != null).isTrue();
        assertThat(permissionAuthority.getPermissions().equals(permissions)).isTrue();
    }
}