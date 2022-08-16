package co.edu.umb.guide1mobileengineering.application.config;

import co.edu.umb.guide1mobileengineering.application.util.JwtTokenUtil;
import co.edu.umb.guide1mobileengineering.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private JwtTokenUtil tokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    if (!hasAuthorizationHeader(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = getAccessToken(request);

    if (!tokenUtil.validateAccessToken(accessToken)) {
      filterChain.doFilter(request, response);
      return;
    }

    setAuthenticationContext(accessToken, request);
    filterChain.doFilter(request, response);
  }

  private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
    UserDetails userDetails = getUserDetails(accessToken);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      userDetails, null, null
    );

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UserDetails getUserDetails(String accessToken) {
    User user = new User();
    String[] subjectArray = tokenUtil.getSubject(accessToken).split(Pattern.quote(","));
    user.setId(Integer.parseInt(subjectArray[0]));
    user.setEmail(subjectArray[1]);
    return user;
  }

  private boolean hasAuthorizationHeader(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    System.out.println("Authorization Header: " + authorization);
    return !ObjectUtils.isEmpty(authorization) && authorization.startsWith("Bearer");
  }

  private String getAccessToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    String token = authorization.split(" ")[1].trim();
    System.out.println("Access Token: " + token);
    return token;
  }

}
