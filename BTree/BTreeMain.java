import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * Main Application.
 * <p>
 * You do not need to change this class.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /** Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        /** Read the minimum degree of B+Tree first */

        int degree = scan.nextInt();

        BTree bTree = new BTree(degree);

        /** Reading the database student.csv into B+Tree Node*/
        List<Student> studentsDB = getStudents();

        for (Student s : studentsDB) {
            bTree.insert(s);
        }

        /** Start reading the operations now from input file*/
        try {
            while (scan.hasNextLine()) {
                Scanner s2 = new Scanner(scan.nextLine());

                while (s2.hasNext()) {

                    String operation = s2.next();

                    switch (operation) {
                        case "insert": {

                            long studentId = Long.parseLong(s2.next());
                            String studentName = s2.next() + " " + s2.next();
                            String major = s2.next();
                            String level = s2.next();
                            int age = Integer.parseInt(s2.next());
                            /** Write a logic to generate recordID based on studentId*/
                            Random r = new Random(studentId);
                            long recordID = r.nextLong();

                            Student s = new Student(studentId, age, studentName, major, level, recordID);
                            bTree.insert(s);

                            break;
                        }
                        case "delete": {
                            long studentId = Long.parseLong(s2.next());
                            boolean result = bTree.delete(studentId);
                            if (result)
                                System.out.println("Student deleted successfully.");
                            else
                                System.out.println("Student deletion failed.");

                            break;
                        }
                        case "search": {
                            long studentId = Long.parseLong(s2.next());
                            long recordID = bTree.search(studentId);
                            if (recordID != -1)
                                System.out.println("Student exists in the database at " + recordID);
                            else
                                System.out.println("Student does not exist.");
                            break;
                        }
                        case "print": {
                            List<Long> listOfRecordID = new ArrayList<>();
                            listOfRecordID = bTree.print();
                            System.out.println("List of recordIDs in B+Tree " + listOfRecordID.toString());
                        }
                        default:
                            System.out.println("Wrong Operation");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> getStudents() {

        /**
         * Extract the students information from "Students.csv"
         * return the list<Students>
         */
    	List<Student> studentList = new ArrayList<>();
    	String csv = "/Student.csv";
    	String line = "";
    	String comma = ",";
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
    		
    		// csv's are split by comma
    		String[] temp = line.split(comma);
    		
    		// Store attributes for clarity
    	    long studentId = Long.parseLong(temp[0]);
    	    int age = Integer.parseInt(temp[1]);
    	    String studentName = temp[2];
    	    String major = temp[3];
    	    String level = temp[4];
    	    long recordId = Long.parseLong(temp[5]);
    		
    		// Create student
    		Student s = new Student(studentId, age, studentName, major, level, recordId);
    		studentList.add(s);
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
        return studentList;
    }
}













