package com.prueba.gatewaycarvajal.security;

import org.springframework.http.HttpStatus;

import com.prueba.utils.response.ResponseError;

public class JwtTokenExceptionHandler {
	
	public final static String ORIGIN = "Gateway-Carvajal";
	
	public final static ResponseError BAD_REQUEST = build("BAD_REQUEST", "Solicitud incorrecta", HttpStatus.BAD_REQUEST);
	
	public final static ResponseError NOT_FOUND = build("NOT_FOUND", "No encontrado", HttpStatus.NOT_FOUND);
	
	public final static ResponseError UNKNOWN_ERROR = build("UNKNOWN_ERROR", "Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
	
	public final static ResponseError JWT_NO_SIGNATURE = build("JWT_NO_SIGNATURE", "El token JWT, no esta firmado", HttpStatus.BAD_REQUEST);
	
	public final static ResponseError JWT_NO_VALID = build("JWT_NO_VALID", "El token JWT es invalido", HttpStatus.BAD_REQUEST);
	
	public final static ResponseError JWT_EXPIRED = build("JWT_EXPIRED", "El token JWT se encuentra expirado", HttpStatus.BAD_REQUEST);
	
	public final static ResponseError JWT_NOT_SUPPORTED = build("JWT_NOT_SUPPORTED", "Token JWT no soportado", HttpStatus.BAD_REQUEST);
	
	public final static ResponseError AUTHORIZATION_MISSING = build("AUTHORIZATION_MISSING", "Falta autorización", HttpStatus.BAD_REQUEST);
	
	public static ResponseError build(String title, String description, HttpStatus status) {
		if (description != null && status != null) {
			ResponseError respError = new ResponseError();
			respError.setTitle(title);
			respError.setDescription(description);
			respError.setTypeError(status);
			respError.setOrigin(ORIGIN);
			return respError;
		}
		return null;		
	}

}
