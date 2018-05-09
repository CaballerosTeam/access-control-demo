package com.example.access.control.components.auth.evaluators;

import com.example.access.control.components.person.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DomainBasedPermissionEvaluatorTest {

    @Mock
    private Authentication authentication;

    @Mock
    private Serializable serializableMock;

    private DomainBasedPermissionEvaluator permissionEvaluator;
    private final Object targetType = new Person();
    private final String targetTypeName = targetType.getClass().getSimpleName();
    private final String updatePermission = "update";


    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        permissionEvaluator = new DomainBasedPermissionEvaluator();
        permissionEvaluator = spy(permissionEvaluator);
    }

    @Test
    public void hasPermission_byDomainObject_allClear() {

        doReturn(true).when(permissionEvaluator).hasPrivilege(authentication, targetTypeName, updatePermission);

        boolean actual = permissionEvaluator.hasPermission(authentication, targetType, updatePermission);

        verify(permissionEvaluator).hasPrivilege(authentication, targetTypeName, updatePermission);
        assertTrue(actual);
    }

    @Test
    public void hasPermission_byDomainObjectName_allClear() {

        doReturn(true).when(permissionEvaluator).hasPrivilege(authentication, targetTypeName, updatePermission);

        boolean actual = permissionEvaluator.hasPermission(authentication, serializableMock, targetTypeName, updatePermission);

        verify(permissionEvaluator).hasPrivilege(authentication, targetTypeName, updatePermission);
        assertTrue(actual);
    }

    @Test
    public void hasPrivilege_granted() {

        String authority = updatePermission + "_" + targetTypeName;
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        when(grantedAuthority.getAuthority()).thenReturn(authority);

        doReturn(Collections.singleton(grantedAuthority)).when(authentication).getAuthorities();

        boolean actual = permissionEvaluator.hasPrivilege(authentication, targetTypeName, updatePermission);

        assertTrue("Permission was not granted for authority: " + authority, actual);
    }

    @Test
    public void hasPrivilege_notGranted() {

        String authority = "delete_" + targetTypeName;
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        when(grantedAuthority.getAuthority()).thenReturn(authority);

        doReturn(Collections.singleton(grantedAuthority)).when(authentication).getAuthorities();

        boolean actual = permissionEvaluator.hasPrivilege(authentication, targetTypeName, updatePermission);

        assertFalse("Permission was granted for authority: " + authority, actual);
    }
}