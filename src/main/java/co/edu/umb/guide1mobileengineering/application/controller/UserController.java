package co.edu.umb.guide1mobileengineering.application.controller;

import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.application.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public record UserController(UserService userService) {

  @GetMapping
  public ResponseEntity<?> getUsers(){
    List<UserRequest> activeUsers = userService.getActiveUsers();
    return new ResponseEntity<>(activeUsers, HttpStatus.OK);
  }

  @PutMapping("/deactivate/{id}")
  public ResponseEntity<?> deactivateUser(@PathVariable("id") Integer id) {
    userService.deactivateUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
