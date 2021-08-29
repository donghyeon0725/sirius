package infra;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private String ipAddress;
    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public CustomWebAuthenticationDetails setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
}
