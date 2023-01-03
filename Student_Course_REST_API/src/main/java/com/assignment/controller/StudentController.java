package com.assignment.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.beans.Student;
import com.assignment.exceptions.CourseException;
import com.assignment.exceptions.StudentException;
import com.assignment.payload.request.LoginRequest;
import com.assignment.payload.request.StudentRegisterReq;
import com.assignment.security.jwt.JWTUtils;
import com.assignment.security.services.UserDetailsServiceImpl;
import com.assignment.services.StudentService;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	@Autowired
	private StudentService sService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@PostMapping("/student/register-student")
	public ResponseEntity<String> registerNewStudent(@RequestBody StudentRegisterReq studentReq) throws StudentException
	{
		String message = sService.registerNewStudent(studentReq);
		
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/student/login-student")
	public ResponseEntity<?> loginStudent(@RequestBody LoginRequest loginDetails)
	{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getEmailId(), loginDetails.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginDetails.getEmailId());
		
		if(userDetails != null)
			return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
		
		return new ResponseEntity<>("Invalid user log in details..!", HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/student/update-student")
	public ResponseEntity<Student> updateStudent(@RequestBody Student student) throws StudentException
	{
		Student student1 = sService.updateStudent(student);
		
		return new ResponseEntity<Student>(student1, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/admin/delete-student/{roll}")
	public ResponseEntity<String> deleteStudent(@PathVariable("roll") Integer roll) throws StudentException
	{
		sService.deleteStudent(roll);
		
		return new ResponseEntity<String>("Student deleted...", HttpStatus.OK);
	}
	
	@GetMapping("/admin/all-students")
	public ResponseEntity<List<Student>> viewAllStudents() throws StudentException
	{
		List<Student> students = sService.viewAllStudents();
		
		return new ResponseEntity<List<Student>>(students, HttpStatus.FOUND);
	}
	
	@PostMapping("/admin/add-student-to-course/{courseId}/{roll}")
	public ResponseEntity<String> addStudentToCourse(@PathVariable("courseId") Integer courseId, @PathVariable("roll") Integer roll) throws StudentException, CourseException
	{
		String message = sService.addStudentToCourse(courseId, roll);
		
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
}
