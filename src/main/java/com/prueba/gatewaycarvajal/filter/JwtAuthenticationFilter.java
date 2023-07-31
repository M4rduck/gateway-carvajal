package com.prueba.gatewaycarvajal.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.gatewaycarvajal.security.JwtTokenExceptionHandler;
import com.prueba.gatewaycarvajal.security.JwtUtil;
import com.prueba.gatewaycarvajal.security.RouterValidator;
import com.prueba.utils.response.ResponseGeneral;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private ObjectMapper objectMapper;
	
	public JwtAuthenticationFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ResponseGeneral respGrl = new ResponseGeneral();
			ServerHttpRequest request = exchange.getRequest();
			if (RouterValidator.isSecured.test(request)) {
				if (this.isAuthMissing(request)) {
					respGrl.addError(JwtTokenExceptionHandler.AUTHORIZATION_MISSING);
					return this.onError(exchange, this.responseToString(respGrl), HttpStatus.UNAUTHORIZED);										
				}
				
				ResponseGeneral respValidate = jwtUtil.validateToken(this.getAuthHeader(request));
				if (!respValidate.isSuccess()) {
					return this.onError(exchange, this.responseToString(respValidate), HttpStatus.UNAUTHORIZED);
				}
			}
			return chain.filter(exchange);
		};
	}
	
	private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return response.writeWith(Flux.just(buffer));
	}
	
	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");		
	}
	
	private String getAuthHeader(ServerHttpRequest request) {
		return request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
	}
	
	private ObjectMapper getObjectMapper() {
		this.objectMapper = this.objectMapper == null ? new ObjectMapper() : this.objectMapper;
		return this.objectMapper;
	}
	
	private <T extends ResponseGeneral> String responseToString(T responseGeneral) {
		try {
			return this.getObjectMapper().writeValueAsString(responseGeneral);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static class Config {

		public Config(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
