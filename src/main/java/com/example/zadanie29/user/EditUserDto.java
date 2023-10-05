package com.example.zadanie29.user;

import java.util.Set;

public class EditUserDto {
    private Long id;
    private Set<Role> roles;

    public EditUserDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
