package com.tkarov.demo.dto.response;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -847328974920342L;

    private final String jwtToken;

    public JwtResponse(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return this.jwtToken;
    }
}
