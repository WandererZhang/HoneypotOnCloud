package com.wanderzhang.honeypot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Jwt工具类
 *
 * @author 78445
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    private String secret;
    private long expire;
    private String header;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String generateToken(long userId) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId + "")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("validate is token error ", e);
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
