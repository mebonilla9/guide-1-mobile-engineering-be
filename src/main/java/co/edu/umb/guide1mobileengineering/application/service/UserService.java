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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record UserService(
  UserRepository userRepository,
  AuthenticationManager manager,
  JwtTokenUtil tokenUtil) {

  public void registerUser(UserRequest userRequest) {
    User user = UserMapper.INSTANCE.userRequestToUser(userRequest);
    user.setPassword(new BCryptPasswordEncoder().encode(userRequest.password()));
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

}
