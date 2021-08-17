package shop.sirius.domain.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.domain.member.domain.Member;
import shop.sirius.global.security.jwt.oauth2.provider.AuthProvider;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAuthProvider(AuthProvider authProvider);

    Optional<Member> findByEmail(String email);
}
