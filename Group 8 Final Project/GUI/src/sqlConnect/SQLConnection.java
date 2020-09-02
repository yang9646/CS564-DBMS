package sqlConnect;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * UW-Madison, CS 564, Summer 2020, Team 8 Final Project: Game Ranking Database
 * GUI/Front End by @author Nate Sackett
 * Requires JavaFX and mysql-connector; fxml GUI design implemented using SceneBuilder
 * 
 * SQLConnection connects to local MySQL database with Game Rank Data 
 * and exports that connection via getConnection
 * 
 * Design based on example by @author Humayun Kabir from https://www.youtube.com/channel/UCE5PI5NwZm7Y8rfcB2Q9dTQ
 * and example by instructor @author Dr. Hien Nguyen, email: hnguyen36@wisc.edu
 */
public class SQLConnection {

	/* TODO: for new Users...
	 * Update ID, and password to match local instance of Game Rank Database
	 * */
    static final String ID ="root";    
    static final String password="******";

    public Connection connection;
    
    public Connection getConnection(String databaseURL) {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("databaseURL"+ databaseURL);
            connection = DriverManager.getConnection(databaseURL, ID, password);
            System.out.println("Successfully connected to the database");
         }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return connection;
        }
}