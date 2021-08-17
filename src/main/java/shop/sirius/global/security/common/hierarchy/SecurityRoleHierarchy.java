package shop.sirius.global.security.common.hierarchy;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import shop.sirius.global.security.service.RoleHierarchyService;

@RequiredArgsConstructor
public class SecurityRoleHierarchy extends RoleHierarchyImpl {
    private final RoleHierarchyService roleHierarchyService;
    public void reload() {
        super.setHierarchy(roleHierarchyService.findAllHierarchy());
    }
}
