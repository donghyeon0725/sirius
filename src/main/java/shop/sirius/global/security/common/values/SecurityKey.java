package shop.sirius.global.security.common.values;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecurityKey {
    ROLES("roles"), MESSAGE("message");

    private String key;
}
