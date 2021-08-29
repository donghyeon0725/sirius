package shop.sirius.global.security.jwt.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDetails normal() {
        return User.builder().username("admin").password(passwordEncoder.encode("1234")).roles("USER").build();
    }

    private UserDetails admin() {
        return User.builder().username("user1").password(passwordEncoder.encode("1234")).roles("ADMIN").build();
    }


    private MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("스프링 시큐리티가 동작하는지 확인")
    void test1() throws Exception {
        // TODO 인증 없이 root 에 접근하면 예외
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("스프링 시큐리티가 동작하는지 확인")
    @WithMockUser(username = "user1", roles = {"USER"})
    void test2(@Mock WebAuthenticationDetails authenticationDetails, @Mock Authentication authentication) throws Exception {

        // ip voter 만 통과할 수 있도록, 별도로 목객체 생성
//        when(authentication.getDetails()).thenReturn(authenticationDetails);
//        when(authenticationDetails.getRemoteAddress()).thenReturn("127.0.0.1");

        // TODO 인증 된 유저로 root 에 접근하면 정상반환 => 현재는 root 페이지가 없지만 생기면  status().isNotFound() 를 status().isOk() 로 변경
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user(admin()))).andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("google 로그인 시도하면 OAuth인증창 등장한다")
//    @WithMockUser(username = "user1", roles = {"USER"})
    public void test3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth2/authorize/google")).andDo(MockMvcResultHandlers.print()).andExpect(status().is3xxRedirection());
    }
}

