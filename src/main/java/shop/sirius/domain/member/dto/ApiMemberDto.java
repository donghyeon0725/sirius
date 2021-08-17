package shop.sirius.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ApiMemberDto extends MemberDto {

    private String lastAccessToken;

    @NotNull
    @NotEmpty
    private String apiKey;

    private String apiType;
}
