package co.edu.umb.guide1mobileengineering.application.controller;

import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public record UserController(UserService userService) {

  @PostMapping
  public ResponseEntity<?> registerNewUser(@RequestBody UserRequest request) {
    userService.registerUser(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserRequest request){
    userService.loginUser(request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> restorePassword(@RequestBody UserRequest request){
    userService.restorePassword(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
