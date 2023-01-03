package com.assignment.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.assignment.beans.Course;
import com.assignment.beans.Role;
import com.assignment.beans.Student;
import com.assignment.beans.User;
import com.assignment.beans.UserRole;
import com.assignment.exceptions.CourseException;
import com.assignment.exceptions.StudentException;
import com.assignment.payload.request.StudentRegisterReq;
import com.assignment.repository.CourseRepo;
import com.assignment.repository.StudentRepo;
import com.assignment.repository.UserRepo;
import com.assignment.repository.UserRoleRepo;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepo sRepo;
	
	@Autowired
	private CourseRepo cRepo;
	
	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private UserRoleRepo urRepo;

	@Autowired
	PasswordEncoder encoder;
	
	@Override
	public String registerNewStudent(StudentRegisterReq studentReq) throws StudentException {
		
		Optional<Student> student0 = sRepo.findByEmailId(studentReq.getEmailId());
		
		if(student0.isEmpty())
		{
			Set<String> strRoles = studentReq.getRoles();

			Set<UserRole> roles = new HashSet<>();

			if (strRoles == null) {
				UserRole studentRole = urRepo.findByUserRole(Role.STUDENT)
						.orElseThrow(() -> new RuntimeException("Error: Student_Role is not found..!"));
				roles.add(studentRole);
			} else {

				strRoles.forEach(role -> {
					switch (role) {
					case "student":
						UserRole studentRole = urRepo.findByUserRole(Role.STUDENT)
						.orElseThrow(() -> new RuntimeException("Error: Student_Role is not found..!"));
						roles.add(studentRole);

						break;
					case "admin":
						UserRole adminRole = urRepo.findByUserRole(Role.ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Admin_Role is not found..!"));
						roles.add(adminRole);
						break;
					}
				});
			}
			
			User user = new User();
			user.setName(studentReq.getName());
			user.setEmailId(studentReq.getEmailId());
			user.setPassword(encoder.encode(studentReq.getPassword()));
			user.setRoles(roles);
			uRepo.save(user);
			
			Student student1 = new Student();
			student1.setName(studentReq.getName());
			student1.setAddress(studentReq.getAddress());
			student1.setEmailId(studentReq.getEmailId());
			student1.setPassword(encoder.encode(studentReq.getPassword()));
			student1.setRoles(roles);
			sRepo.save(student1);

			return studentReq.getName()+" you are registered successfully...";
		}
		else throw new StudentException("Student already exist with this emailId..!");
	}

	@Override
	public void deleteStudent(Integer studentId) throws StudentException {
		
		Optional<Student> ip = sRepo.findById(studentId);
		if(ip.isPresent())
		{
			sRepo.deleteById(studentId);
		}
		else throw new StudentException("Student doesnot exist with this Id "+studentId);
	}

	@Override
	public Student updateStudent(Student student) throws StudentException {
		
		Optional<Student> student1 = sRepo.findById(student.getRoll());
		
		if(student1.isPresent())
			return sRepo.save(student);
		else
			throw new StudentException("student doesnot exist...");
	}

	@Override
	public List<Student> viewAllStudents() throws StudentException {
		
		List<Student> students = sRepo.findAll();
		
		if(students.size() == 0)
			throw new StudentException("students not found...");
		else
			return students;
	}

	@Override
	public String addStudentToCourse(Integer courseId, Integer roll) throws CourseException, StudentException {
		
		Optional<Student> student = sRepo.findById(roll);
		
		if(student.isPresent())
		{
			Optional<Course> course = cRepo.findById(courseId);
			
			if(course.isPresent())
			{
				Student s1 = student.get();
				Course c1 = course.get();
				
				List<Student> students = c1.getStudents();
				students.add(s1);
				
				c1.setStudents(students);
				
				cRepo.save(c1);
				
				return s1.getName()+" student added to the course "+c1.getCourseName();
			}
			else throw new CourseException("Course not found...");
		}
		else throw new StudentException("Student not found...");
	}

	
}
