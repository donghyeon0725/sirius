package shop.sirius.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.global.security.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String name);
}

