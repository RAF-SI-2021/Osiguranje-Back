package security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Getter
@ToString
public class JwtAuthenticationConfig {

    @Value("${gateway-app.security.jwt.url:/login}")
    private String url;

    @Value("${gateway-app.security.jwt.header:Authorization}")
    private String header;

    @Value("${gateway-app.security.jwt.prefix:Bearer}")
    private String prefix;

    @Value("${gateway-app.security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${gateway-app.security.jwt.secret}")
    private String secret;
}