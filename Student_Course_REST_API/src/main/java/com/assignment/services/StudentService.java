package com.assignment.services;

import java.util.List;

import com.assignment.beans.Student;
import com.assignment.exceptions.CourseException;
import com.assignment.exceptions.StudentException;
import com.assignment.payload.request.StudentRegisterReq;

public interface StudentService {
	
	public String registerNewStudent(StudentRegisterReq student) throws StudentException;

	public List<Student> viewAllStudents() throws StudentException;
	
	public Student updateStudent(Student student) throws StudentException;
	
	public void deleteStudent(Integer roll) throws StudentException;
	
	public String addStudentToCourse(Integer courseId, Integer roll) throws CourseException, StudentException;

}
