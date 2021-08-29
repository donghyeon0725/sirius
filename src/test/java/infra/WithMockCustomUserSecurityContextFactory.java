package infra;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import shop.sirius.global.security.jwt.model.UserPrincipal;
import shop.sirius.global.security.jwt.token.JwtAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal =
                new UserPrincipal(customUser.id(), customUser.username(), customUser.username(), Arrays.asList(new SimpleGrantedAuthority(customUser.role())));
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(customUser.token(), "test");
        context.setAuthentication(authRequest);
        return context;
    }
}
