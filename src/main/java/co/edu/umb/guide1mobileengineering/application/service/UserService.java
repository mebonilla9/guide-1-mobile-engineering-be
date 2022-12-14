package co.edu.umb.guide1mobileengineering.application.service;

import co.edu.umb.guide1mobileengineering.application.mappers.UserMapper;
import co.edu.umb.guide1mobileengineering.application.request.TokenRequest;
import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.application.util.JwtTokenUtil;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import co.edu.umb.guide1mobileengineering.domain.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public record UserService(
  UserRepository userRepository,
  AuthenticationManager manager,
  JwtTokenUtil tokenUtil,
  PasswordEncoder passwordEncoder
) {

  public void registerUser(UserRequest userRequest) {
    User user = UserMapper.INSTANCE.userRequestToUser(userRequest);
    user.setActive(Boolean.TRUE);
    user.setPassword(passwordEncoder.encode(userRequest.password()));
    userRepository.save(user);
  }

  public TokenRequest loginUser(UserRequest userRequest) {
    Authentication authentication = manager.authenticate(
      new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password())
    );
    User user = (User) authentication.getPrincipal();
    String token = tokenUtil.generateJwtToken(user);
    return new TokenRequest(token);
  }

  public void restorePassword(UserRequest userRequest){
    User user = userRepository.findUserByEmail(userRequest.email())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));
    user.setPassword(userRequest.password());
    userRepository.save(user);
  }

  public List<UserRequest> getActiveUsers(){
    List<User> users = userRepository.findAll().stream().filter(User::getActive).toList();
    users.forEach(user -> user.setPassword(""));
    return UserMapper.INSTANCE.userListToUserRequestList(users);
  }

  public void deactivateUser(Integer id) {
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    user.setActive(Boolean.FALSE);
    userRepository.save(user);
  }
}
