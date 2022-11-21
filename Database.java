
package smartfarm;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.io.*;


/**
 *
 * @author Emin Gregory
 */

public class Database {
    
    
     static boolean error = false;
     private Statement statement;
     private Connection conn;
     private String connectionURL;
     private String connectionURL2;
     private String driver;
     private String dbName;
     
          
     
    private String createCowTable;
    private String createBirthTable;
    private String dropCowTable = "DROP TABLE COWTABLE";
    private String dropBirthTable = "DROP TABLE BIRTHTABLE";
    
    
    
    public void shut() throws FileNotFoundException, IOException, SQLException
            
    {
        connectionURL = "jdbc:derby:" + dbName + "; drop=true";
        connectionURL2 = "jdbc:derby: ;shutdown=true";
        DriverManager.getConnection(connectionURL2);
    }
    
    
     Database(String connect){
       
       connect(); 
        
        
    }
    
     
private void connect() 
              
      {
          
    
    // Create  SQL statement  and connection variables
    driver = "org.apache.derby.jdbc.EmbeddedDriver"; 
    dbName = "Farm";
    connectionURL = "jdbc:derby:" + dbName + "; create=true";
    
    
    try 
    {
        Class.forName(driver);
      
        
    }catch(java.lang.ClassNotFoundException e)
    {
        System.out.println(e);
   
    }
    
    try 
    {
   
    conn = DriverManager.getConnection(connectionURL);
    
    statement = conn.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
   
    }
    
    catch (SQLException se)
    {
        System.out.println(se);
    }
  
}  
               
   
 public Statement getStatement()
 {
     return statement;
 }


 
    public void CreateTables() 
 {                  
       createCowTable = "CREATE TABLE COWTABLE "
                       + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,"
                       +" COW_ID VARCHAR(25),"
                       +" COW_NAME VARCHAR(50), "
                       +" PRIMARY KEY (ID), UNIQUE (COW_ID)"
                       + ")";
             
       
       createBirthTable = "CREATE TABLE BIRTHTABLE"
                  + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,"
                  +" COW_ID INT REFERENCES COWTABLE (ID), "
                  + "PARENT_ID VARCHAR(25), "
                  + "PARENT_NAME VARCHAR(50),"
                  + "CALF_ID VARCHAR(25), "
                  + "CALF_NAME VARCHAR(50)"
                  +")";
        
        try {
            
            
            statement.execute(dropCowTable);
            statement.execute(dropBirthTable);
            
          
            } catch(SQLException ex1)
                
                    
                 {   
                      try {
                                
                                statement.execute(dropBirthTable);
                                statement.execute(dropCowTable);
                                statement.execute(createCowTable);
                                statement.execute(createBirthTable);
                                System.out.println("Tables Created");
                         } 
                          
                          catch(SQLException ex2) { 
                          
                              try {
                                
                                
                                statement.execute(createCowTable);
                                statement.execute(createBirthTable);
                                System.out.println("Tables Created");
                         } 
                          
                          catch(SQLException ex3) { System.out.println(ex3); }
                          
                          
                          
                          
                          }
           
            }
        }
 
}

