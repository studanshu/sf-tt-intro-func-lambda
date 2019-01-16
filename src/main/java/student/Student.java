package student;

import java.util.Arrays;
import java.util.List;

public class Student {
    private String name;
    private double gpa;
    private List<String> courses;

    private Student() {}

    public static Student ofNameGpaCourses(String name, double gpa, String ... courses) {
        Student self = new Student();
        self.name = name;
        self.gpa = gpa;
        self.courses = Arrays.asList(courses); // "mostly immutable"
        return self;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    public List<String> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", gpa=" + gpa +
                ", courses=" + courses +
                '}';
    }

    public static boolean getSmarterThan(Student ss,/*final*/ double threshold) {
//        threshold += 1;
        return ss.gpa > threshold;
    }
}
