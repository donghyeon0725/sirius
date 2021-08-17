package shop.sirius.domain.member.application;

import shop.sirius.domain.member.dto.ApiMemberDto;
import shop.sirius.domain.member.dto.MemberDto;

public interface MemberService {

    // 단순 토큰 발급 => api로 가입된 계정인지 확인 필요
    String login(String email, String password);

    // api access key 업데이트 후 토큰 발급
    String apiLogin(String apiKey);

    // 회원가입
    Long join(MemberDto memberDto);

    // api 회원 가입
    Long  join(ApiMemberDto memberDto);
}
