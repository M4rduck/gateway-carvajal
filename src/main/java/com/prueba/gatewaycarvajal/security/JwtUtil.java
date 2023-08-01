package com.prueba.gatewaycarvajal.security;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.prueba.utils.response.ResponseGeneral;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtUtil {
	
	@Value("${spring.application.jwtSecret}")
	private String jwtSecret;
	
	private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    public ResponseGeneral validateToken(String jwtToken) {
    	ResponseGeneral respGnrl = new ResponseGeneral();
    	try {
    		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
    	} catch (SignatureException e) {
    		respGnrl.addError(JwtTokenExceptionHandler.JWT_NO_SIGNATURE);
    	} catch (MalformedJwtException e) {
    		respGnrl.addError(JwtTokenExceptionHandler.JWT_NO_VALID);
    	} catch (ExpiredJwtException e) {
    		respGnrl.addError(JwtTokenExceptionHandler.JWT_EXPIRED);
    	} catch (UnsupportedJwtException e) {
    		respGnrl.addError(JwtTokenExceptionHandler.JWT_NOT_SUPPORTED);
    	}
    	return respGnrl;
    }
	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}
