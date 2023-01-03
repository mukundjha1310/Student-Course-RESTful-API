package com.assignment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assignment.beans.Role;
import com.assignment.beans.UserRole;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Integer>{
	
	Optional<UserRole> findByUserRole(Role userRole);

}
