package shop.sirius.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.global.security.domain.RoleHierarchy;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
}
