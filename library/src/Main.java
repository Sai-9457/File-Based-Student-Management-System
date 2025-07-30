import java.io.*;
import java.util.*;


class Student{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;


    }
}

class delete{

}
class update{

}

public class Main {
    public static void main(String[] args) throws Exception {
        StudentManager studentManager=new StudentManager();
        while (true) {
        System.out.println("=====Menu=====");
        System.out.println("1.Add a student.");
        System.out.println("2.Search a student.");
        System.out.println("3.Display All Student.");
        System.out.println("4.Delete Student Data.");
        System.out.println("5.Exit");
        System.out.print("Enter your choice: ");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();

            switch (choice) {
                case 1:
                    Scanner scanner = new Scanner(System.in);
                    Student student = new Student();
                    System.out.println("enter your Id-");

                    int ID = scanner.nextInt();
                    student.setId(ID);
                    System.out.println("Enter your Name-");
                    String NAME = scanner.next();
                    student.setName(NAME);


                    studentManager.saveStudent(student);
                    break;

                case 2:
                    studentManager.studentsearch();
                    break;

                case 3:
                    studentManager.Display();
                    break;

                case 4:
                    studentManager.Delete();
                    break;

                case 5:
                    System.out.println("Exiting The Program.");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
                    return;

            }
        }







    }
}
class StudentManager {

    private static final String FILE_PATH = "C:\\TP_project\\library\\DataBase.txt";
    private static final String TEMP_FILE_PATH = "C:\\TP_project\\library\\TempDB.txt";

    public String saveStudent(Student student) {
        try {
            File file = new File("C:\\TP_project\\library\\DataBase.txt");
            file.createNewFile();
            Scanner fileScanner = new Scanner(file);
            boolean isDuplicate = false;

            if (fileScanner.hasNextLine()) fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    if (id.equals(String.valueOf(student.getId()))) {
                        isDuplicate = true;
                        break;
                    }
                }
            }
            fileScanner.close();

            if (isDuplicate) return " Error: Student ID already exists.";

            boolean isEmpty = file.length() == 0;
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);

            if (isEmpty) {
                fileOutputStream.write("ID | NAME\n".getBytes());
            }
            String record = student.getId() + " | " + student.getName() + "\n";
            fileOutputStream.write(record.getBytes());
            fileOutputStream.close();
            return " Student added successfully.";
        } catch (Exception e) {
            return " Error while saving student: " + e.getMessage();
        }
    }


    public void studentsearch() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID: ");
        int ID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String Name = scanner.nextLine();
        boolean found = false;

        try {
            File file = new File(FILE_PATH);
            Scanner fileScanner = new Scanner(file);

            if (fileScanner.hasNextLine()) fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 2) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();

                    if (id.equals(String.valueOf(ID)) && name.equalsIgnoreCase(Name)) {
                        System.out.println("Data Found:");
                        System.out.println(id + " | " + name);
                        found = true;
                        break;
                    }
                }
            }

            fileScanner.close();

            if (!found) {
                System.out.println("Student Data Not Found.");
            }
        } catch (Exception e) {
            System.out.println("Error while searching: " + e.getMessage());
        }
    }

    public void Display() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("No Data Found.");
                return;
            }

            Scanner scanner = new Scanner(file);
            System.out.println("===== Student Records =====");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error reading student records: " + e.getMessage());
        }
    }

    public void Delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID to delete: ");
        int deleteId = scanner.nextInt();
        boolean deleted = deleteById(deleteId);

        if (deleted) {
            System.out.println("Student with ID " + deleteId + " deleted successfully.");
        } else {
            System.out.println("Student with ID " + deleteId + " not found.");
        }
    }

    public List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(FILE_PATH))) {
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        } catch (Exception e) {
            lines.add("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    public boolean deleteById(int id) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(TEMP_FILE_PATH);
        boolean found = false;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line = reader.readLine();
            if (line != null) {
                writer.write(line);
                writer.newLine(); // Write header
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2 && parts[0].trim().equals(String.valueOf(id))) {
                    found = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

            writer.flush();
        } catch (IOException e) {
            System.out.println("Error during deletion: " + e.getMessage());
            return false;
        }

        // Replace old file with new
        if (found) {
            if (!inputFile.delete()) {
                System.out.println("Could not delete original file.");
                return false;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file.");
                return false;
            }
        } else {
            tempFile.delete(); // clean up
        }

        return found;
    }
}

