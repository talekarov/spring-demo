package com.tkarov.demo.utils;

import com.tkarov.demo.dto.RoleAuth;
import com.tkarov.demo.dto.request.UserRequestBody;
import com.tkarov.demo.dto.response.UserResponseBody;
import com.tkarov.demo.model.persistance.Role;
import com.tkarov.demo.model.persistance.User;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ConvertUtils {

    private ConvertUtils(){}

public static User convertToUserEntity(UserRequestBody userRequestBody){
    return User.builder()
            .email(userRequestBody.getEmail())
            .password(userRequestBody.getPassword())
            .username(userRequestBody.getUsername())
            .build();
}

public  static UserResponseBody convertToUserResponseBody(User user) {
        return UserResponseBody.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();

}

public static List<UserResponseBody> convertToUserResponseBodies(List<User> users) {
        if (users != null){
            return users.stream()
                    .map(ConvertUtils::convertToUserResponseBody)
                    .collect(Collectors.toList());
        }
        else return Collections.emptyList();
}

public static Set<RoleAuth> convertToRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> RoleAuth.builder().name(role.getName()).build())
                .collect(Collectors.toSet());
}
}
