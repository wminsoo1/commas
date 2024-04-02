package tight.commas.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${application.security.jwt.key}")
    private String jwtKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.refresh.expiration}")
    private long refreshExpiration;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        jwtKey = Base64.getEncoder().encodeToString(jwtKey.getBytes());
    }

    public String createAccessToken(UserDetails userDetails) {

        return buildToken(userDetails, jwtExpiration);
    }

    public String createRefreshToken(UserDetails userDetails) {

        return buildToken(userDetails, refreshExpiration);
    }

    public String buildToken(UserDetails userDetails, long expiration) {

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUserPk(String token) {

        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {

        return request.getHeader("Authorization");
    }


    public boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}