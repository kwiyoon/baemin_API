package kuit.server.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import kuit.server.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import kuit.server.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import kuit.server.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import kuit.server.common.exception.jwt.unauthorized.JwtUnauthorizedTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static kuit.server.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${secret.jwt-secret-key}")
    private String SECRET_KEY;

    @Value("${secret.at-expired-in}")
    private long AT_EXPIRED_IN;

    @Value("${secret.rt-expired-in}")
    private long RT_EXPIRED_IN;

    public String createToken(Claims claims, long EXPIRED_TIME) {
        log.info("JWT key={}", SECRET_KEY);

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String createAccessToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        return createToken(claims, AT_EXPIRED_IN);
    }

    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        return createToken(claims, RT_EXPIRED_IN);

    }

    public boolean isExpiredToken(String token) throws JwtInvalidTokenException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY).build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (SignatureException e){
            throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
        } catch (IllegalArgumentException e){
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.isExpiredToken]", e);
            throw e;
        }
    }

    public String getPrincipal(String token) {
        String jwt = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token)
                .getBody().get("email", String.class);
        return jwt;
    }

}
