package com.assignment.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthJwtEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		Map<String, Object> body = new HashMap<>();
		body.put("Status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("Error", "Unauthorised");
		body.put("Message", authException.getMessage());
		body.put("Path", request.getServletPath());
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue(response.getOutputStream(), body);
		
		
	}

}
