package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalMgmtSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "admin";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println();
                System.out.println("Enter Your Choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppoitment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM !! ");
                        return;
                    default:
                        System.out.println("Enter Valid Choice !");
                        break;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppoitment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();

        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appoitmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appoitmentDate, connection)){
                String appoitmentQuery = "insert into appoitments(patient_id, doctor_id, appoitment_date) values(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appoitmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3, appoitmentDate);
                    int affectedRows = preparedStatement.executeUpdate();

                    if(affectedRows>0){
                        System.out.println("Appointment Booked !");
                    }
                    else{
                        System.out.println("Failed to Book Appointment !");
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor not Available on this date !");
            }
        }
        else {
            System.out.println("Either Doctor or Patient does not Exist !! ");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appoitmentDate, Connection connection){
        String query = "select count(*) from appoitments where doctor_id = ? and appoitment_date = ?";
        try{
            PreparedStatement preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appoitmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
