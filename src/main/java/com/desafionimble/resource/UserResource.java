package com.desafionimble.resource;

import com.desafionimble.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    private final UserService userService;

    public UserResource(
            UserService userService
    ){
        this.userService = userService;
    }
}
