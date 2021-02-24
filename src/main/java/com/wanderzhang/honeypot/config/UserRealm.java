package com.wanderzhang.honeypot.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author 78445
 */
public class UserRealm extends AuthorizingRealm {
    private static final String NAME = "wanderzhang";
    private static final String PASSWORD = "bupt2017211210";

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        if (!username.equals(NAME)){
            return null;
        }
        return new SimpleAuthenticationInfo("",PASSWORD,"");
    }
}
