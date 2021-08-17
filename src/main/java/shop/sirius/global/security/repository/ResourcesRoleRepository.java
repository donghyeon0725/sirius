package shop.sirius.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.global.security.domain.Resources;
import shop.sirius.global.security.domain.ResourcesRole;
import shop.sirius.global.security.domain.Role;

public interface ResourcesRoleRepository extends JpaRepository<ResourcesRole, Long> {
    ResourcesRole findByResourcesAndRole(Resources resources, Role role);

    void deleteByResources(Resources resources);
}
