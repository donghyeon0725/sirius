package shop.sirius.domain.member.dto;

import lombok.Data;
import shop.sirius.global.security.jwt.oauth2.provider.AuthProvider;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ApiMemberDto extends MemberDto {

    private String lastAccessToken;

    @NotNull
    @NotEmpty
    private String authProviderId;

    private AuthProvider authProvider;
}
