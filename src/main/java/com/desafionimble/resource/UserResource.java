package com.desafionimble.resource;

import com.desafionimble.model.dtos.DepositoDTO;
import com.desafionimble.model.user.User;
import com.desafionimble.model.user.UserDTO;
import com.desafionimble.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserResource {

    private final UserService userService;

    public UserResource(
            UserService userService
    ){
        this.userService = userService;
    }

    @Operation(summary = "Criar um novo usuário", description = "Cria um novo uusário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO){
        User user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Realizar um novo depósito", description = "Realizar um depósito")
    @PutMapping("/deposito")
    public ResponseEntity<Void> deposito(@RequestBody DepositoDTO depositoDTO){
        userService.deposito(depositoDTO.getCpf(), depositoDTO.getValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
