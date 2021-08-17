package shop.sirius.domain.member.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;

    private String email;

    private String password;

    private String name;

    private Integer areaCode;

    private Integer phoneNumber;

    private Integer dialingCode;

    private String gender;
}
