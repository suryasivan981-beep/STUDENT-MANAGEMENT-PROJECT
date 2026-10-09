
import java.util.*;
import java.io.*;

class Student {
    private int id;
    private String name;
    private int age;
    private double mark1;
    private double mark2;
    private double mark3;

    public Student(int id, String name, int age,
                   double mark1, double mark2, double mark3) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return mark1 + mark2 + mark3;
    }

    public double getPercentage() {
        return getTotal() / 3.0;
    }

    public String getGrade() {
        double percentage = getPercentage();

        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    public void update(String name, int age,
                       double m1, double m2, double m3) {
        this.name = name;
        this.age = age;
        this.mark1 = m1;
        this.mark2 = m2;
        this.mark3 = m3;
    }

    public void display() {
        System.out.printf(
            "ID: %d | Name: %s | Age: %d | Total: %.2f"
            + " | Percentage: %.2f%% | Grade: %s%n",
            id, name, age, getTotal(), getPercentage(), getGrade()
        );
    }

    public String toFile() {
        return id + "," + name.replace(",", " ") + "," + age
            + "," + mark1 + "," + mark2 + "," + mark3;
    }
}

public class StudentManagement {

    static Scanner sc = new Scanner(System.in);
    static HashMap<Integer, Student> students = new HashMap<>();
    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        loadStudents();

        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Sort by Marks");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    sortByMarks();
                    break;
                case 7:
                    saveStudents();
                    System.out.println("Data saved. Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    static double readMark(String message) {
        while (true) {
            try {
                System.out.print(message);
                double mark = Double.parseDouble(sc.nextLine().trim());

                if (mark >= 0 && mark <= 100) {
                    return mark;
                }

                System.out.println("Mark must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid mark.");
            }
        }
    }

    static String readName() {
        while (true) {
            System.out.print("Enter student name: ");
            String name = sc.nextLine().trim();

            if (!name.isEmpty() && !name.contains(",")) {
                return name;
            }

            System.out.println("Name cannot be empty or contain commas.");
        }
    }

    static int readAge() {
        while (true) {
            System.out.print("Enter age: ");
            int age = readInt();

            if (age >= 1 && age <= 120) {
                return age;
            }

            System.out.println("Enter a valid age.");
        }
    }

    static void addStudent() {
        System.out.print("Enter student ID: ");
        int id = readInt();

        if (students.containsKey(id)) {
            System.out.println("Student ID already exists!");
            return;
        }

        String name = readName();
        int age = readAge();
        double m1 = readMark("Enter mark 1: ");
        double m2 = readMark("Enter mark 2: ");
        double m3 = readMark("Enter mark 3: ");

        Student student = new Student(id, name, age, m1, m2, m3);
        students.put(id, student);

        saveStudents();
        System.out.println("Student added successfully!");
    }

    static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student student : students.values()) {
            student.display();
        }
    }

    static void searchStudent() {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Choose search type: ");
        int choice = readInt();

        if (choice == 1) {
            System.out.print("Enter student ID: ");
            Student student = students.get(readInt());

            if (student != null) {
                student.display();
            } else {
                System.out.println("Student not found!");
            }
        } else if (choice == 2) {
            System.out.print("Enter student name: ");
            String name = sc.nextLine().trim();
            boolean found = false;

            for (Student student : students.values()) {
                if (student.getName().toLowerCase()
                        .contains(name.toLowerCase())) {
                    student.display();
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Student not found!");
            }
        } else {
            System.out.println("Invalid search option!");
        }
    }

    static void updateStudent() {
        System.out.print("Enter student ID to update: ");
        int id = readInt();

        Student student = students.get(id);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        String name = readName();
        int age = readAge();
        double m1 = readMark("Enter new mark 1: ");
        double m2 = readMark("Enter new mark 2: ");
        double m3 = readMark("Enter new mark 3: ");

        student.update(name, age, m1, m2, m3);
        saveStudents();

        System.out.println("Student updated successfully!");
    }

    static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = readInt();

        if (students.remove(id) != null) {
            saveStudents();
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    static void sortByMarks() {
        List<Student> list = new ArrayList<>(students.values());

        list.sort(
            Comparator.comparingDouble(Student::getPercentage).reversed()
        );

        if (list.isEmpty()) {
            System.out.println("No students to sort.");
            return;
        }

        System.out.println("\n--- Students: Highest Marks First ---");

        for (Student student : list) {
            student.display();
        }
    }

    static void saveStudents() {
        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Student student : students.values()) {
                writer.write(student.toFile());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving student records.");
        }
    }

    static void loadStudents() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length != 6) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int age = Integer.parseInt(data[2]);
                    double m1 = Double.parseDouble(data[3]);
                    double m2 = Double.parseDouble(data[4]);
                    double m3 = Double.parseDouble(data[5]);

                    if (age < 1 || age > 120
                            || m1 < 0 || m1 > 100
                            || m2 < 0 || m2 > 100
                            || m3 < 0 || m3 > 100) {
                        continue;
                    }

                    students.put(id,
                        new Student(id, name, age, m1, m2, m3));

                } catch (NumberFormatException e) {
                    System.out.println("Skipping an invalid record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading student records.");
        }
    }
}