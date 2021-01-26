package com.tkarov.demo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class RoleAuth implements GrantedAuthority {

    @NotEmpty
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
