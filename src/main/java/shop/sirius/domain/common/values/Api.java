package shop.sirius.domain.common.values;

public enum Api {
    KAKAO;

    public static Api stringToApi(String str) {
        if (KAKAO.name().toLowerCase().equals(str.toLowerCase()))
            return KAKAO;

        return null;
    }
}
