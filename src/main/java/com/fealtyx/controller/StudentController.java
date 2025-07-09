package com.fealtyx.controller;

import com.fealtyx.model.Student;
import com.fealtyx.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody @Valid Student student) {
        service.addStudent(student);
        return ResponseEntity.ok("Student added.");
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable int id) {
        Student student = service.getStudent(id);
        if (student == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable int id, @RequestBody @Valid Student student) {
        service.updateStudent(id, student);
        return ResponseEntity.ok("Student updated.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        service.deleteStudent(id);
        return ResponseEntity.ok("Student deleted.");
    }

    @GetMapping("/{id}/summary")
    public String getStudentSummary(@PathVariable int id) {
        Student student = service.getStudent(id);
        if (student != null) {
            return service.getStudentSummaryFromOllama(student);
        } else {
            return "Student not found!";
        }
    }
}
