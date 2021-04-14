package com.wanderzhang.honeypot.config.shiro;

import com.wanderzhang.honeypot.config.jwt.JwtToken;
import com.wanderzhang.honeypot.utils.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 78445
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        if (userId == null) {
            throw new UnknownAccountException("用户名不存在");
        }
        return new SimpleAuthenticationInfo("", jwtToken.getCredentials(), getName());
    }
}
