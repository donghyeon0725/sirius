package shop.sirius.global.security.jwt.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import shop.sirius.global.security.common.entry.RestAuthenticationEntryPoint;
import shop.sirius.global.security.common.hierarchy.SecurityRoleHierarchy;
import shop.sirius.global.security.jwt.factory.UrlResourcesMapFactoryBean;
import shop.sirius.global.security.jwt.filter.JwtAuthenticationFilter;
import shop.sirius.global.security.jwt.handler.JwtAccessDeniedHandler;
import shop.sirius.global.security.jwt.handler.OAuth2AuthenticationFailureHandler;
import shop.sirius.global.security.jwt.handler.OAuth2AuthenticationSuccessHandler;
import shop.sirius.global.security.jwt.interceptor.UrlFilterSecurityInterceptor;
import shop.sirius.global.security.jwt.metadata.UrlFilterInvocationSecurityMetadataSource;
import shop.sirius.global.security.jwt.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import shop.sirius.global.security.jwt.service.CustomOAuth2UserService;
import shop.sirius.global.security.service.RoleHierarchyService;
import shop.sirius.global.security.service.SecurityResourceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile({"local"})
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.secretKey}")
    private String secretKey;

    private String all = "/**";

    private final SecurityResourceService securityResourceService;

    private final RoleHierarchyService roleHierarchyService;

    private final RequestMatcher[] permitAllResources = {
            new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name())
            , new AntPathRequestMatcher("/h2-console")
            , new AntPathRequestMatcher("/h2-console/**")
            , new AntPathRequestMatcher("/oauth2/**")
//            , new AntPathRequestMatcher("/**")
    };

    private static List<String> clients = Arrays.asList("google", "facebook");

    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private final Environment env;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        List<ClientRegistration> registrations = clients.stream()
//                .map(c -> getRegistration(c))
//                .filter(registration -> registration != null)
//                .collect(Collectors.toList());
//
//        return new InMemoryClientRegistrationRepository(registrations);
//    }
//
//    private ClientRegistration getRegistration(String client) {
//        String clientId = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-id");
//
//        if (clientId == null) {
//            return null;
//        }
//
//        String clientSecret = env.getProperty(
//                CLIENT_PROPERTY_KEY + client + ".client-secret");
//
//        String[] scope = env.getProperty(CLIENT_PROPERTY_KEY + client + ".scope", String[].class);
//
//        String redirectUri = env.getProperty(CLIENT_PROPERTY_KEY + client + ".redirectUri");
//
//        if (client.equals("google")) {
//            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
//                    .clientId(clientId).clientSecret(clientSecret).scope(scope).redirectUri(redirectUri).build();
//        }
//        if (client.equals("facebook")) {
//            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
//                    .clientId(clientId).clientSecret(clientSecret).scope(scope).redirectUri(redirectUri).build();
//        }
//        return null;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher(all)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(abstractAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class)


                .authorizeRequests()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                // 이 주소는 사용자가 인증을 마치고, 호출될 끝 점입니다.
                // 이 url 에 요청이 올 때에는 인증 정보를 모두 가지고 있다.
                // success 핸들러에서 rediection 해버리면 이 끝점은 호출되지 않는 것 같음
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                // 사용자 정보를 저장하는 용도로 사용
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);


        http
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .rememberMe().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();



        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }




    private AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        UrlFilterSecurityInterceptor interceptor = new UrlFilterSecurityInterceptor(permitAllResources);

        interceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        interceptor.setAccessDecisionManager(affirmativeBased());
        interceptor.setAuthenticationManager(authenticationManagerBean());

        return interceptor;
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);

        return urlResourcesMapFactoryBean;
    }



    private GenericFilterBean abstractAuthenticationProcessingFilter() {
        return new JwtAuthenticationFilter(secretKey, permitAllResources);
    }

    @Bean
    public AffirmativeBased affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        voters.add(roleVoter());
        return voters;
    }

    @Bean
    public AccessDecisionVoter<?> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    /**
     * 권한 계층
     * */
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new SecurityRoleHierarchy(roleHierarchyService);
    }

}
