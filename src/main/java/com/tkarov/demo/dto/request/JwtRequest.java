package com.tkarov.demo.dto.request;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 4657983748263732L;

    private String username;

    private String password;
}
