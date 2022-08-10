package co.edu.umb.guide1mobileengineering.application.mappers;

import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userRequestToUser(UserRequest userRequest);
  UserRequest userToUserRequest(User user);

  List<UserRequest> userListToUserRequestList(List<User> userList);
  List<User> userRequestListToUserList(List<UserRequest> userRequestList);
}
