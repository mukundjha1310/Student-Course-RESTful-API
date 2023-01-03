package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assignment.beans.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer>{

	public Course findByCourseName(String cName);

}
