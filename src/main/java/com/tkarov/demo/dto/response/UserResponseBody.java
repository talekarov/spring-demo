package com.tkarov.demo.dto.response;

import com.tkarov.demo.model.persistance.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseBody implements Serializable {

    static final long serialVersionUID = 1L;

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotEmpty
    @Size(max = 20, message = "Username should not be longer than 20 characters")
    private String username;

    @NotEmpty
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    private String password;

    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty
    private Set<Role> roles;


}
