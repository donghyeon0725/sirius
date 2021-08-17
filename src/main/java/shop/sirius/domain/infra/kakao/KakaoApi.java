package shop.sirius.domain.infra.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.sirius.domain.infra.common.HttpRequest;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/kakao")
public class KakaoApi {

    @Value("${api.kakao.client_id}")
    private String client_id;

    @Value("${api.kakao.response_type}")
    private String response_type;

    @Value("${api.kakao.login_request_uri}")
    private String loginRequestUrl;

    @Value("${api.kakao.join_request_uri}")
    private String joinRequestUrl;

    @Value("${api.kakao.logout_redirect_uri}")
    private String logoutRedirectUrl;

    // 카카오 인가 코드 발급
    @Value("${api.kakao.kakao_authorize_request_uri}")
    private String kakaoAuthorizeRequestUrl;

    // 카카오 토큰 발급 (로그인)
    @Value("${api.kakao.kakao_login_request_uri}")
    private String kakaoLoginRequestUrl;

    // 카카오 개인 정보 요청
    @Value("${api.kakao.kakao_privacy_request_uri}")
    private String kakaoPrivacyRequestUrl;

    // 카카오 로그아웃
    @Value("${api.kakao.kakao_logout_request_uri}")
    private String kakaoLogoutRequestUrl;

    @GetMapping("/joinUrl")
    public String joinUrl() {
        return kakaoAuthorizeRequestUrl + "?"
                + "client_id=" + client_id
                + "&redirect_uri=" + joinRequestUrl
                + "&response_type=" + response_type;
    }

    @GetMapping("/loginUrl")
    public String loginUrl() {
        return kakaoAuthorizeRequestUrl + "?"
                + "client_id=" + client_id
                + "&redirect_uri=" + loginRequestUrl
                + "&response_type=" + response_type;
    }

    @GetMapping("/join")
    public String join(@RequestParam(value = "code", required = false) String code) {
        HttpRequest httpRequest = new HttpRequest.Builder()
                .domain(kakaoLoginRequestUrl)
                .method(HttpMethod.POST)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .body("client_id", client_id)
                .body("grant_type", "authorization_code")
                .body("response_type", "code")
                .body("redirect_uri", joinRequestUrl)
                .body("code", code)
                .build();

        LinkedHashMap<String, Object> fetch = httpRequest.fetch().getContent();

        HttpRequest privacyRequest = new HttpRequest.Builder()
                .domain(kakaoPrivacyRequestUrl)
                .method(HttpMethod.POST)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + fetch.get("access_token")).build();

        return privacyRequest.fetch().getContent().toString();
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "code", required = false) String code) {
        HttpRequest httpRequest = new HttpRequest.Builder()
                .domain(kakaoLoginRequestUrl)
                .method(HttpMethod.POST)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .body("client_id", client_id)
                .body("grant_type", "authorization_code")
                .body("response_type", "code")
                .body("redirect_uri", loginRequestUrl)
                .body("code", code)
                .build();

        LinkedHashMap<String, Object> fetch = httpRequest.fetch().getContent();

        return fetch.toString();
    }


    @GetMapping("/logoutUrl")
    public String logout() {
        return kakaoLogoutRequestUrl + "?"
                + "client_id=" + client_id
                + "&logout_redirect_uri=" + logoutRedirectUrl;
    }

}
