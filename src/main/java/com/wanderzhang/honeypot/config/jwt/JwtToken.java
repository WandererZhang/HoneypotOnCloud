package com.wanderzhang.honeypot.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 78445
 */
public class JwtToken implements AuthenticationToken {
    private final String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
