package shop.sirius.domain.common.values;

import lombok.*;

import javax.persistence.Embeddable;

@Builder
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactNumber {
    private Integer areaCode;
    private Integer phoneNumber;
    private Integer dialingCode;
    @Builder.Default
    private String delimiter = "-";


    public String fullContactNumber() {
        StringBuffer sb = new StringBuffer();

        sb.append(areaCode);
        sb.append(delimiter);
        sb.append(phoneNumber);
        sb.append(delimiter);
        sb.append(dialingCode);

        return sb.toString();
    }
}
