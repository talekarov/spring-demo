package com.tkarov.demo.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequestBody implements Serializable {

    static final long serialVersionUID = 1L;

    @NotEmpty
    @Size(max = 20, message = "Username should not be longer than 20 characters")
    private String username;

    @NotEmpty
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    private String password;

    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

}
