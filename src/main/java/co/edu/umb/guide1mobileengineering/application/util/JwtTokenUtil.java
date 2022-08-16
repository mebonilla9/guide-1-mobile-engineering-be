package co.edu.umb.guide1mobileengineering.application.util;

import co.edu.umb.guide1mobileengineering.application.mappers.UserMapper;
import co.edu.umb.guide1mobileengineering.application.request.UserRequest;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtTokenUtil {

  private static final Long EXPIRE_DURATION = 1L; //24hrs

  @Value("${app.jwt.secret}")
  private String secretKey;

  public String generateJwtToken(User user){
    UserRequest userRequest = UserMapper.INSTANCE.userToUserRequest(user);
    return "Bearer "+ Jwts.builder()
      .setSubject(userRequest.id()+","+userRequest.email())
      .setIssuer("DevManuel")
      .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
      .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(EXPIRE_DURATION)))
      .signWith(SignatureAlgorithm.HS512,secretKey)
      .compact();
  }

  public boolean validateAccessToken(String token)
    throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, SignatureException {
    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    return true;
  }

  public String getSubject(String token){
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token){
    return Jwts.parser().setSigningKey(secretKey)
      .parseClaimsJws(token)
      .getBody();
  }
}
