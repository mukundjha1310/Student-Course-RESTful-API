package com.assignment.payload.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterReq {
	
	private String name;
	private String emailId;
	private String password;
	
	private Set<String> roles;

}
