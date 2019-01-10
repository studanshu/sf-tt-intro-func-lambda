package student;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
interface StudentCriterion<T> {
//    boolean broken();
    boolean test(T s);
    default StudentCriterion<T> negate(StudentCriterion crit) {
        return s -> !crit.test(s);
    }

    default StudentCriterion<T> negate() {
        return s -> !this.test(s);
    }

    default StudentCriterion<T> and(StudentCriterion<? super T> sc) {
        return s -> this.test(s) && sc.test(s);
    }

    default StudentCriterion<T> or(StudentCriterion<? super T> sc) {
        return s -> this.test(s) || sc.test(s);
    }
}

interface Odd {
    boolean test2(Student s);
}

class SmartCriterion implements StudentCriterion<Student> {
    private double threshold;

    public SmartCriterion(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean test(Student s) {
        return s.getGpa() > threshold;
    }
}

class EnthusiasticCriterion implements StudentCriterion<Student> {

    @Override
    public boolean test(Student s) {
        return s.getCourses().size() > 2;
    }
}

class SmartCriterionUsingPredicate implements Predicate<Student> {

    private double threshold;

    public SmartCriterionUsingPredicate(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean test(Student student) { return student.getGpa() > threshold; }
}

public class School {


    //  "Separate what changes independently"
//    public static List<Student> getSmart(List<Student> ls, double threshold) {
//        List<Student> out = new ArrayList<>();
//
//        Iterator<Student> iter = ls.iterator();
//        while (iter.hasNext()) {
//            Student s = iter.next();
//            if (s.getGpa() > threshold) {
//                out.add(s);
//            }
//        }
//
//        return out;
//    }
//
//    public static List<Student> getEnthusiastic(List<Student> ls, int threshold) {
//        List<Student> out = new ArrayList<>();
//
//        for (Student s : ls) {
//            if (s.getCourses().size() > threshold) {
//                out.add(s);
//            }
//        }
//
//        return out;
//    }

    public static List<? extends Student> getByCriterion(List<? extends Student> ls, StudentCriterion<? super Student> crit) {
        List<Student> out = new ArrayList<>();

        for (Student s : ls) {
            if (crit.test(s)) {
                out.add(s);
            }
        }

        return out;
    }

    public static void showAll(List<? extends Student> ls) {
        for (Student s : ls) {
            System.out.println("> " + s);
        }
        System.out.println("-----------------------");
    }

    public static void main(String[] args) {
        List<Student> roster = Arrays.asList(
                Student.ofNameGpaCourses("Fred", 2.2, "Math", "Physics"),
                Student.ofNameGpaCourses("Jim", 3.2, "Art"),
                Student.ofNameGpaCourses("Sheila", 3.7, "Math", "Physics", "Quantum Mechanics"),
                Student.ofNameGpaCourses("Leela", 2.6, "Chemistry", "Physics", "AI"),
                Student.ofNameGpaCourses("Manju", 3.4, "Math", "History", "Psychology")
        );
        showAll(roster);

//        showAll(getSmart(roster, 3.5));
//        showAll(getSmart(roster, 3.0));
//
//        showAll(getEnthusiastic(roster, 2));
//
        showAll(getByCriterion(roster, new SmartCriterion(3.5)));
        showAll(getByCriterion(roster, new SmartCriterion(3.0)));

        showAll(getByCriterion(roster, new EnthusiasticCriterion()));
//        showAll(getByCriterion(roster, new StudentCriterion() {
//
//            @Override
//            public boolean test(Student s) {
//                return s.getCourses().size() < 2;
//            }
//        }));

//        showAll(getByCriterion(roster, /*new StudentCriterion() {*/
//
//            /*@Override
//            public boolean test*/(Student s) -> {
//                return s.getCourses().size() < 2;
//            }
//        /*}*/));

//        showAll(getByCriterion(roster, (Student s) -> { // lambda format #1
//                return s.getCourses().size() < 2;
//            }));

//        showAll(getByCriterion(roster, s -> { // lambda format #2 argument types are optional (all or nothing)
//            // single untyped argument doesn't require parens...
//                return s.getCourses().size() < 2;
//            }));

        // lambda format #3 "expression lambda" (previously "block lambda")
        showAll(getByCriterion(roster, s -> s.getCourses().size() < 2 ));

        StudentCriterion<Student> sc = s -> s.getCourses().size() < 2;

        boolean notEnthusiastic =
                ((StudentCriterion<Student>)(s -> s.getCourses().size() < 2))
                        .test(Student.ofNameGpaCourses("Freddy", 2.2));

        showAll(getByCriterion(roster, Student.getSmarterThan(2.0)));

//        showAll(getByCriterion(roster, StudentCriterion.negate(sc)));
        showAll(getByCriterion(roster, sc.negate()));
        showAll(getByCriterion(roster, ((StudentCriterion<Student>)(s -> s.getGpa() > 3.3)).and(s -> s.getCourses().size() > 2)));
        showAll(getByCriterion(roster, ((StudentCriterion<Student>)(s -> s.getGpa() > 3.6)).or(s -> s.getCourses().size() > 3)));


    }

}
