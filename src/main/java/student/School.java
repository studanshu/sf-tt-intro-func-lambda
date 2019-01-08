package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@FunctionalInterface
interface StudentCriterion {
//    boolean broken();
    boolean test(Student s);
    static StudentCriterion negate(StudentCriterion crit) {
        return s -> !crit.test(s);
    }

    default StudentCriterion negate() {
        return s -> !this.test(s);
    }

    default StudentCriterion and(StudentCriterion sc) {
        return s -> this.test(s) && sc.test(s);
    }

    default StudentCriterion or(StudentCriterion sc) {
        return s -> this.test(s) || sc.test(s);
    }
}

interface Odd {
    boolean test2(Student s);
}

class SmartCriterion implements StudentCriterion {
    private double threshold;

    public SmartCriterion(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean test(Student s) {
        return s.getGpa() > threshold;
    }
}

class EnthusiasticCriterion implements StudentCriterion {

    @Override
    public boolean test(Student s) {
        return s.getCourses().size() > 2;
    }
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

    public static List<Student> getByCriterion(List<Student> ls, StudentCriterion crit) {
        List<Student> out = new ArrayList<>();

        for (Student s : ls) {
            if (crit.test(s)) {
                out.add(s);
            }
        }

        return out;
    }

    public static void showAll(List<Student> ls) {
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

        StudentCriterion sc = s -> s.getCourses().size() < 2;

        boolean notEnthusiastic =
                ((StudentCriterion)(s -> s.getCourses().size() < 2))
                        .test(Student.ofNameGpaCourses("Freddy", 2.2));

        showAll(getByCriterion(roster, Student.getSmarterThan(2.0)));

//        showAll(getByCriterion(roster, StudentCriterion.negate(sc)));
        showAll(getByCriterion(roster, sc.negate()));
        showAll(getByCriterion(roster, ((StudentCriterion)(s -> s.getGpa() > 3.3)).and(s -> s.getCourses().size() > 2)));
        showAll(getByCriterion(roster, ((StudentCriterion)(s -> s.getGpa() > 3.6)).or(s -> s.getCourses().size() > 3)));


    }
}
