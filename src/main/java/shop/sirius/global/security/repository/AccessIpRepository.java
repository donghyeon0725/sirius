package shop.sirius.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.global.security.domain.AccessIp;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {
}
