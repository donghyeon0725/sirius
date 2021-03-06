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
    @DisplayName("????????? ??????????????? ??????????????? ??????")
    void test1() throws Exception {
        // TODO ?????? ?????? root ??? ???????????? ??????
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("????????? ??????????????? ??????????????? ??????")
    @WithMockUser(username = "user1", roles = {"USER"})
    void test2(@Mock WebAuthenticationDetails authenticationDetails, @Mock Authentication authentication) throws Exception {

        // ip voter ??? ????????? ??? ?????????, ????????? ????????? ??????
//        when(authentication.getDetails()).thenReturn(authenticationDetails);
//        when(authenticationDetails.getRemoteAddress()).thenReturn("127.0.0.1");

        // TODO ?????? ??? ????????? root ??? ???????????? ???????????? => ????????? root ???????????? ????????? ?????????  status().isNotFound() ??? status().isOk() ??? ??????
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user(admin()))).andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("google ????????? ???????????? OAuth????????? ????????????")
//    @WithMockUser(username = "user1", roles = {"USER"})
    public void test3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth2/authorize/google")).andDo(MockMvcResultHandlers.print()).andExpect(status().is3xxRedirection());
    }
}

