package com.example.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.security.ApplicationUserPermission.SAMPLE_READ;
import static com.example.demo.security.ApplicationUserPermission.SAMPLE_WRITE;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(SAMPLE_READ, SAMPLE_WRITE)),
    USER(Sets.newHashSet(SAMPLE_READ));

    private final Set<ApplicationUserPermission> rolePermissions;

    ApplicationUserRole(Set<ApplicationUserPermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Set<ApplicationUserPermission> getRolePermissions() {
        return rolePermissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthoroties() {
        final Set<SimpleGrantedAuthority> authorities = rolePermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getUserPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_"));
        return authorities;
    }
}
