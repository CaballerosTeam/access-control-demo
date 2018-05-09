package com.example.access.control.components.auth.evaluators;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class DomainBasedPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        String targetType = targetDomainObject.getClass().getSimpleName();

        return hasPrivilege(authentication, targetType, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {

        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(authentication, targetType, permission);
    }

    boolean hasPrivilege(Authentication authentication, String targetType, Object permission) {

        String format = "%s_%s";
        String expectedAuthority = String.format(format, permission, targetType);
        expectedAuthority = expectedAuthority.toLowerCase();

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String actualAuthority = grantedAuthority.getAuthority();

            if (actualAuthority != null && expectedAuthority.equals(actualAuthority.toLowerCase())) return true;
        }

        return false;
    }
}
