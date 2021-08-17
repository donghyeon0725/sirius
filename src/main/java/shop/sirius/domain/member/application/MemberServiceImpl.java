package shop.sirius.domain.member.application;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.sirius.domain.common.values.Api;
import shop.sirius.domain.common.values.ContactNumber;
import shop.sirius.domain.common.values.Gender;
import shop.sirius.domain.member.dao.MemberRepository;
import shop.sirius.domain.member.domain.Member;
import shop.sirius.domain.member.domain.MemberRole;
import shop.sirius.domain.member.dto.ApiMemberDto;
import shop.sirius.domain.member.dto.MemberDto;
import shop.sirius.global.error.ErrorCode;
import shop.sirius.global.error.exception.InvalidPasswordException;
import shop.sirius.global.security.common.token.JwtTokenCreator;
import shop.sirius.global.security.domain.Role;
import shop.sirius.global.security.repository.RoleRepository;

public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenCreator tokenCreator;

    private RoleRepository roleRepository;

    @Override
    public String login(String email, String password) {

        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        if (!passwordEncoder.matches(password, findMember.getPassword()))
            throw new InvalidPasswordException(ErrorCode.WRONG_PASSWORD);

        return tokenCreator.createToken(findMember.getEmail(), findMember.simpleGrantedAuthorities());
    }

    @Override
    public String apiLogin(String apiKey) {
        Member findMember = memberRepository.findByApiKey(apiKey).orElseThrow(() -> new UsernameNotFoundException(ErrorCode.LOGIN_INPUT_INVALID.name()));

        return tokenCreator.createToken(findMember.getEmail(), findMember.simpleGrantedAuthorities());
    }

    @Override
    public Long join(MemberDto memberDto) {


        ContactNumber contactNumber = ContactNumber.builder()
                .areaCode(memberDto.getAreaCode())
                .phoneNumber(memberDto.getPhoneNumber())
                .dialingCode(memberDto.getDialingCode())
                .build();

        Member member = Member.builder()
                .phoneNumber(contactNumber)
                .email(memberDto.getEmail())
                .gender(Gender.stringToGender(memberDto.getGender()))
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .build();

        Role role_user = roleRepository.findByRoleName("ROLE_USER");

        MemberRole memberRole = MemberRole.builder()
                .role(role_user)
                .build();

        member.addRole(memberRole);

        return member.getId();
    }

    @Override
    public Long join(ApiMemberDto memberDto) {
        ContactNumber contactNumber = ContactNumber.builder()
                .areaCode(memberDto.getAreaCode())
                .phoneNumber(memberDto.getPhoneNumber())
                .dialingCode(memberDto.getDialingCode())
                .build();

        Member member = Member.builder()
                .phoneNumber(contactNumber)
                .email(memberDto.getEmail())
                .gender(Gender.stringToGender(memberDto.getGender()))
                .name(memberDto.getName())
                .lastAccessToken(memberDto.getLastAccessToken())
                .apiType(Api.stringToApi(memberDto.getApiType()))
                .build();


        Role role_user = roleRepository.findByRoleName("ROLE_USER");


        MemberRole memberRole = MemberRole.builder()
                .role(role_user)
                .build();

        member.addRole(memberRole);

        return member.getId();
    }
}
