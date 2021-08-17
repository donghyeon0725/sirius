package shop.sirius.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.sirius.domain.common.values.ContactNumber;
import shop.sirius.domain.member.dao.MemberRepository;
import shop.sirius.domain.member.domain.Member;
import shop.sirius.domain.member.domain.MemberRole;
import shop.sirius.domain.member.dto.ApiMemberDto;
import shop.sirius.global.security.common.token.JwtTokenCreator;
import shop.sirius.global.security.domain.Role;
import shop.sirius.global.security.repository.RoleRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenCreator tokenCreator;

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public Member apiJoin(ApiMemberDto memberDto) {
        ContactNumber contactNumber = ContactNumber.builder()
                .areaCode(memberDto.getAreaCode())
                .phoneNumber(memberDto.getPhoneNumber())
                .dialingCode(memberDto.getDialingCode())
                .build();

        Member member = Member.builder()
                .phoneNumber(contactNumber)
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                .name(memberDto.getName())
                .lastAccessToken(memberDto.getLastAccessToken())
                .authProvider(memberDto.getAuthProvider())
                .authProviderId(memberDto.getAuthProviderId())
                .build();


        Role role_user = roleRepository.findByRoleName("ROLE_USER");


        MemberRole memberRole = MemberRole.builder()
                .role(role_user)
                .build();

        member.addRole(memberRole);

        memberRepository.save(member);

        return member;
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
