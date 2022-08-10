package co.edu.umb.guide1mobileengineering.application.service;

import co.edu.umb.guide1mobileengineering.application.mappers.UserMapper;
import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import co.edu.umb.guide1mobileengineering.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public record UserService(UserRepository userRepository) {

  public void registerUser(UserRequest userRequest) {
    User user = UserMapper.INSTANCE.userRequestToUser(userRequest);
    userRepository.save(user);
  }

  public User loginUser(UserRequest userRequest) {
    return userRepository.findUserByEmailAndPassword(
      userRequest.email(), userRequest.password()
    ).orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  public void restorePassword(UserRequest userRequest){
    User user = userRepository.findUserByEmail(userRequest.email())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));
    user.setPassword(userRequest.password());
    userRepository.save(user);
  }

}
