package com.ftn.sbnz.service.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!isPermitted(request)) {
            System.out.println("####" + request.getMethod() + ":" + request.getRequestURL());
            System.out.println("#### Authorization: " + request.getHeader("Authorization"));
            String requestTokenHeader = request.getHeader("Authorization");
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Replace with your frontend origin
                response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE"); // Add the HTTP methods your API supports
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, skip, X-Message"); // Add the headers your API supports
                return;
            }
            String username = null;
            String jwtToken = null;
            if (requestTokenHeader != null && requestTokenHeader.contains("Bearer")) {
                jwtToken = requestTokenHeader.substring(requestTokenHeader.indexOf("Bearer ") + 7);
                System.out.println(">>>>>JWT TOKEN: " + jwtToken);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        System.out.println("Username: " + userDetails.getUsername() + ", role: " + userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token.");
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired.");
                } catch (io.jsonwebtoken.MalformedJwtException e) {
                    System.out.println("Bad JWT Token.");
                }
            } else {
                logger.warn("JWT Token does not exist.");
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isPermitted(HttpServletRequest request){
        String rq=request.getMethod()+","+request.getRequestURL().toString();
        String[] permitted={"POST,api/auth/register","POST,api/auth/login"};
        for(String s : permitted){
            String[] sentRequest=rq.split(",");
            String[] permittedURL=s.split(",");
            if(sentRequest[0].equals(permittedURL[0]) && sentRequest[1].contains(permittedURL[1])){
                return true;
            }
        }
        return false;
    }

}
