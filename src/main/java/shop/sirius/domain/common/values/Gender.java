package shop.sirius.domain.common.values;

public enum Gender {
    MAIL, FEMAIL;

    public static Gender stringToGender(String str) {
        if (MAIL.name().toLowerCase().equals(str.toLowerCase()))
            return MAIL;
        else
            return FEMAIL;
    }
}
