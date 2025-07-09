package com.fealtyx.service;

import com.fealtyx.model.Student;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {
    private final ConcurrentHashMap<Integer, Student> studentMap = new ConcurrentHashMap<>();

    public void addStudent(Student student) {
        studentMap.put(student.getId(), student);
    }

    public Collection<Student> getAllStudents() {
        return studentMap.values();
    }

    public Student getStudent(int id) {
        return studentMap.get(id);
    }

    public void updateStudent(int id, Student student) {
        studentMap.put(id, student);
    }

    public void deleteStudent(int id) {
        studentMap.remove(id);
    }

    public String getStudentSummaryFromOllama(Student student) {
        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = "Summarize the following student profile:\n"
                    + "Name: " + student.getName() + "\n"
                    + "Age: " + student.getAge() + "\n"
                    + "Email: " + student.getEmail();

            String jsonInputString = "{ \"model\": \"llama3\", \"prompt\": \"" + prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\", \"stream\": false }";


            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            br.close();
            conn.disconnect();

            // Parse response string to extract "response" field
            String responseStr = response.toString();
            int startIndex = responseStr.indexOf("\"response\":\"") + 12;
            int endIndex = responseStr.indexOf("\"", startIndex);
            String result = responseStr.substring(startIndex, endIndex);

            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to generate summary.";
        }
    }
}
