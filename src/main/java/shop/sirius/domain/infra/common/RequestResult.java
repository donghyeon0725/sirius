package shop.sirius.domain.infra.common;

import lombok.Getter;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.util.LinkedHashMap;

@Getter
public class RequestResult {
    private Integer responseCode;
    private LinkedHashMap<String, Object> content;
    private String responseStr;

    public RequestResult(Integer responseCode, String responseStr) {
        this.responseCode = responseCode;
        this.responseStr = responseStr;
    }

    public LinkedHashMap<String, Object> getContent() {

        try {
            JSONParser parser = new JSONParser(responseStr);
            return parser.object();
        } catch (ParseException e) {

            return null;
        }
    }
}
