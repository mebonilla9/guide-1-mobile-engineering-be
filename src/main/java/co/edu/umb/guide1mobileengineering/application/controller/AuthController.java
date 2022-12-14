package co.edu.umb.guide1mobileengineering.application.controller;

import co.edu.umb.guide1mobileengineering.application.request.TokenRequest;
import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(UserService userService) {

  @PostMapping("/register")
  public ResponseEntity<?> registerNewUser(@RequestBody @Valid UserRequest request) {
    userService.registerUser(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody @Valid UserRequest request){
    TokenRequest tokenResponse = userService.loginUser(request);
    return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
  }

  @PutMapping("/restore")
  public ResponseEntity<?> restorePassword(@RequestBody @Valid UserRequest request){
    userService.restorePassword(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
