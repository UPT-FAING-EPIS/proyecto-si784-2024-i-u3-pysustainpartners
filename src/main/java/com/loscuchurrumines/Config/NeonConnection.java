package com.loscuchurrumines.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NeonConnection {

    private static final String DATABASE_URL = "jdbc:postgresql://ep-small-haze-88087949.us-east-2.aws.neon.tech/dbSustainPartners";
    private static final String DATABASE_USER = "ximena-ortiz";
    private static final String DATABASE_PASSWORD = "neq9Gm5awICO";
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
