package com.desafionimble.resource;

import com.desafionimble.model.dtos.DepositoDTO;
import com.desafionimble.model.user.User;
import com.desafionimble.model.user.UserDTO;
import com.desafionimble.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(
            UserService userService
    ){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/deposito")
    public ResponseEntity<Void> deposito(@RequestBody DepositoDTO depositoDTO){
        userService.deposito(depositoDTO.getCpf(), depositoDTO.getValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
