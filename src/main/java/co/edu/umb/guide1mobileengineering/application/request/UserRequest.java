package co.edu.umb.guide1mobileengineering.application.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UserRequest(

  Integer id,
  @Email @Length(min = 10, max = 50) @NotNull String email,
  String fullName,
  @Length(min = 5, max = 100) @NotNull String password) {
}
