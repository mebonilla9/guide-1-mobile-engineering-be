package co.edu.umb.guide1mobileengineering.application.request;

public record UserRequest(
  Integer id,
  String email,
  String fullName,
  String password) {
}
