package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection connection;
    public Doctor(Connection connection){
        this.connection = connection;
    }
    public void viewDoctors(){
        String query = "select * from doctors";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors: ");
            System.out.println("+------------+-------------------+------------------+----------------+");
            System.out.println("| Doctor Id  | Name              | Specialization   | Phone_No       |");
            System.out.println("+------------+-------------------+------------------+----------------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                int phone = resultSet.getInt("Phone_No");

                System.out.printf("| %-10s | %-17s | %-16s | %-14s |\n",id, name, specialization, phone );
                System.out.println("+------------+-------------------+------------------+----------------+");
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "select * from doctors where id = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
