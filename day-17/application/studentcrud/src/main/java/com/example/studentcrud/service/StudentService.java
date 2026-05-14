package com.example.studentcrud.service;

import java.util.List;

import com.example.studentcrud.dto.StudentRequestDto;
import com.example.studentcrud.dto.StudentResponseDto;
import com.example.studentcrud.entity.Student;

public interface StudentService {
    StudentResponseDto saveStudent(StudentRequestDto requestDto);

    List<StudentResponseDto> getAllStudents();

    StudentResponseDto getStudentById(Long id);

    void deleteStudent(Long id);

}
