package com.assignment.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.assignment.beans.Course;
import com.assignment.exceptions.CourseException;
import com.assignment.repository.CourseRepo;

@Service
public class CourseServiceImpl implements CourseService{
	
	@Autowired
	private CourseRepo cRepo;

	
	@Override
	public Course registerNewCourse(Course course) throws CourseException {
		
		Course course1 = cRepo.findByCourseName(course.getCourseName());
		
		if(course1 == null)
		{
			cRepo.save(course);
			return course;
		}
		else throw new CourseException("course is already exist...");
	}

	@Override
	public void deleteCourse(Integer courseId) throws CourseException {
		
		Optional<Course> c = cRepo.findById(courseId);
		if(c.isPresent())
		{
			cRepo.deleteById(courseId);
		}
		else throw new CourseException("course doesnot exist with this Id "+courseId);
	}

	@Override
	public Course updateCourse(Course course) throws CourseException {
		
		Optional<Course> course1 = cRepo.findById(course.getCourseId());
		
		if(course1.isPresent())
			return cRepo.save(course);
		else
			throw new CourseException("course doesnot exist...");
	}

	@Override
	public List<Course> viewAllCourses() throws CourseException {
		
		List<Course> courses = cRepo.findAll();
		
		if(courses.size() == 0)
			throw new CourseException("courses not found...");
		else
			return courses;
		
	}

}
