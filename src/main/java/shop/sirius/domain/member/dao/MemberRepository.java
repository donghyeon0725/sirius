package shop.sirius.domain.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.sirius.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByApiKey(String apiKey);

    Optional<Member> findByEmail(String email);
}
