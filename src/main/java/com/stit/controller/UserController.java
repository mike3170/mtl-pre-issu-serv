package com.stit.controller;

import com.stit.common.ApiResponse;
import com.stit.dto.UserDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    public ApiResponse getLoginUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> authList = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            authList.add(authority.getAuthority());
        }

        UserDto dto = new UserDto();
        dto.setUserName(username);
        dto.setAuthotity(authList);

        return ApiResponse.ok(dto);

//                {"status":"OK","data":{"username":"MICHAEL","authorities":["OPERATOR"]},"error":null}
    }
} // end class
