package com.prueba.gatewaycarvajal.security;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouterValidator {
	
	public static final List<String> openApiEndpoints = List.of("/api/api-auth/auth/forgot-password", "/api/api-auth/auth/register");

	public static final Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
