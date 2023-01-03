package com.assignment.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.assignment.beans.Admin;
import com.assignment.beans.Role;
import com.assignment.beans.User;
import com.assignment.beans.UserRole;
import com.assignment.exceptions.UserException;
import com.assignment.payload.request.AdminRegisterReq;
import com.assignment.repository.AdminRepo;
import com.assignment.repository.UserRepo;
import com.assignment.repository.UserRoleRepo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepo aRepo;

	@Autowired
	private UserRepo uRepo;

	@Autowired
	private UserRoleRepo urRepo;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public String registerNewAdmin(AdminRegisterReq adminReq) throws UserException {

		Optional<Admin> admin0 = aRepo.findByEmailId(adminReq.getEmailId());

		if (admin0.isEmpty()) {
			
			Set<String> strRoles = adminReq.getRoles();

			Set<UserRole> roles = new HashSet<>();

			if (strRoles == null) {
				UserRole adminRole = urRepo.findByUserRole(Role.ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Admin Role is not found..!"));
				roles.add(adminRole);
			} else {

				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						UserRole adminRole = urRepo.findByUserRole(Role.ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
						roles.add(adminRole);

						break;
					case "student":
						UserRole studentRole = urRepo.findByUserRole(Role.STUDENT)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(studentRole);
						break;
					}
				});
			}
			
			User user = new User();
			user.setName(adminReq.getName());
			user.setEmailId(adminReq.getEmailId());
			user.setPassword(encoder.encode(adminReq.getPassword()));
			user.setRoles(roles);
			uRepo.save(user);
			
			Admin admin1 = new Admin();
			admin1.setName(adminReq.getName());
			admin1.setEmailId(adminReq.getEmailId());
			admin1.setPassword(encoder.encode(adminReq.getPassword()));
			admin1.setRoles(roles);
			aRepo.save(admin1);

			return adminReq.getName() + " you are registered successfully...";
			
		} else
			throw new UserException("User already exist with this emailId..!");
	}

	@Override
	public String getCurrentLoggedInUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String userName = authentication.getName();
		
		Optional<User> user = uRepo.findByEmailId(userName);
		
		return user.get().getName();
		
	}

}
