package shop.sirius.domain.member.application;

import shop.sirius.domain.member.domain.Member;
import shop.sirius.domain.member.dto.ApiMemberDto;

import java.util.Optional;

public interface MemberService {

    // api 회원 가입
    Member apiJoin(ApiMemberDto memberDto);

    Optional<Member> findMemberByEmail(String email);
}
