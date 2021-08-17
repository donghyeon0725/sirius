package shop.sirius.domain.member.domain;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import shop.sirius.domain.common.values.Api;
import shop.sirius.domain.common.values.ContactNumber;
import shop.sirius.domain.common.values.Gender;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Embedded
    private ContactNumber phoneNumber;

    private String lastAccessToken;

    @Column(unique = true)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    private Api apiType;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRole> memberRoles = new HashSet<>();

    public void addRole(MemberRole role) {
        this.memberRoles.add(role);
        role.mappingMember(this);
    }

    public List<SimpleGrantedAuthority> simpleGrantedAuthorities() {
        return this.memberRoles.stream().map(s -> new SimpleGrantedAuthority(s.getRole().getRoleName())).collect(Collectors.toList());
    }
}
