package co.edu.umb.guide1mobileengineering.application.util;

import co.edu.umb.guide1mobileengineering.application.mappers.UserMapper;
import co.edu.umb.guide1mobileengineering.application.mappers.UserMapperImpl;
import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenUtil {

  private static final Long EXPIRE_DURATION = 1l; //24hrs

  @Value("${app.jwt.secret}")
  private String secretKey;

  public String generateJwtToken(User user){
    UserRequest userRequest = UserMapper.INSTANCE.userToUserRequest(user);
    return Jwts.builder()
      .setSubject(userRequest.id()+","+userRequest.email())
      .setIssuer("DevManuel")
      .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
      .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(EXPIRE_DURATION)))
      .signWith(SignatureAlgorithm.HS512,secretKey)
      .compact();
  }

}
