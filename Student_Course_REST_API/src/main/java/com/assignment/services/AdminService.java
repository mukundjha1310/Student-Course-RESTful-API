package com.assignment.services;

import com.assignment.exceptions.UserException;
import com.assignment.payload.request.AdminRegisterReq;

public interface AdminService {
	
	public String registerNewAdmin(AdminRegisterReq admin) throws UserException;
	
	public String getCurrentLoggedInUser();
}
