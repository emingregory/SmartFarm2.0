
package smartfarm;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



/**
 *
 * @author Emin
 */

 class SmartFarm {
   
 
   public SmartFarm() {
       
       statement = database.getStatement();
   }   
     
   ResultSet result;
   Statement statement;
   Database database = new Database("connect");  
   
   
       
    public void start() {
        
        database.CreateTables();  //*Uncomment this function to clear the database if needed*      
       
        statement = database.getStatement();
        Scanner inputCowDetails = new Scanner(System.in);
        String cowName, calfName; 
       
        String matchingCowID, matchingCowParentID, cowID, calfID;
        
        
                 //Determine if the farm has any cows in it
                              
                  try {
                        
                        result = statement.executeQuery("SELECT COWTABLE.COW_ID, COWTABLE.COW_NAME, BIRTHTABLE.PARENT_ID, BIRTHTABLE.PARENT_NAME," +
                        "BIRTHTABLE.CALF_ID, BIRTHTABLE.CALF_NAME " +
                        "FROM COWTABLE, BIRTHTABLE WHERE COWTABLE.ID = BIRTHTABLE.COW_ID");
                        
                        while (result.next()) {
                            
                                                      
                            matchingCowID = result.getString("COW_ID");
                            matchingCowParentID = result.getString("PARENT_ID");
                                       
                            // if there are any cows in the farm, determine the first cow of the farm
                            System.out.print("Enter Cow ID: ");
                            cowID = inputCowDetails.nextLine();
							   
                            if (cowID.equals(matchingCowID) && matchingCowParentID == null) {
                            
                                cowName = result.getString("COW_Name");
                                System.out.print("Enter the ID of the calf which has been born to " + cowName + ": ");
                                calfID = inputCowDetails.nextLine();
                                System.out.print("Enter the NAME of the calf which has been born to " + cowName + ": ");
                                calfName = inputCowDetails.nextLine(); 

                             try {
                               
                             
                               
                               statement.executeUpdate("INSERT INTO COWTABLE (COW_ID, COW_NAME) "
                                       + "values('"+ calfID +"', '" + calfName + "')"); 
                               
                               statement.executeUpdate("INSERT INTO BIRTHTABLE (COW_ID, PARENT_ID, PARENT_NAME, CALF_ID, CALF_NAME) "
                                       + "values((SELECT ID FROM COWTABLE WHERE COW_ID = '" + cowID +"'), null, null, '" + calfID + "', '" + calfName + "')");
                               
                               System.out.println("Succ1esfully added " + cowName + "'s newborn cow ID: " + calfID + " NAME: " + calfName + " to the farm");
                           
                               
                           } catch (SQLException ex) {
                               
                               System.out.println(ex);
                           }
                          
                            System.out.println("Succesfully added the first cow's newborn calf, ID: " + calfID + " NAME: " + calfName + " to the list");
                           
                            nextStage();
                           
                      }  else {    ResultSet result2;
                                
                                    try {
                                        String removedCowName;
                                        String removedCowID;
                                        result2 = statement.executeQuery("SELECT COWTABLE.COW_ID, COWTABLE.COW_NAME," +
                                        "BIRTHTABLE.CALF_ID, BIRTHTABLE.CALF_NAME " +
                                        "FROM COWTABLE, BIRTHTABLE WHERE COWTABLE.COW_ID = BIRTHTABLE.CALF_ID");
                                        
                                       if(result2.next()) {
                                           
                                           
                                           removedCowName = result2.getString("COW_NAME");
                                           removedCowID = result2.getString("COW_ID");
                                           System.out.println("COW_NAME " + removedCowName);
                                           if (cowID.equals(removedCowID) && !removedCowName.equals("removed from farm"))  {
                                                                                        
                                                 cowName = removedCowName;
                                                 System.out.print("Enter the ID of the calf which has been born to " + cowName + ": ");
                                                 calfID = inputCowDetails.nextLine();
                                                 System.out.print("Enter the NAME of the calf which has been born to " + cowName + ": ");
                                                 calfName = inputCowDetails.nextLine(); 

                                                statement.executeUpdate("INSERT INTO COWTABLE (COW_ID, COW_NAME) "
                                                + "values('"+ calfID +"', '" + calfName + "')"); 
                               
                                                statement.executeUpdate("INSERT INTO BIRTHTABLE (COW_ID, PARENT_ID, PARENT_NAME, CALF_ID, CALF_NAME) "
                                                + "values( (SELECT ID FROM COWTABLE WHERE COW_ID = '" + cowID +"'), "
                                                + "(SELECT CAST(CAST(b.COW_ID AS CHAR(48)) AS VARCHAR(10)) FROM BIRTHTABLE b WHERE b.COW_ID IN (SELECT ID FROM COWTABLE) AND b.CALF_ID = '" + cowID +"'),"
                                                + "(SELECT COW_NAME FROM COWTABLE c WHERE c.ID IN (SELECT COW_ID FROM BIRTHTABLE) FETCH FIRST 1 ROWS ONLY),'" + calfID + "', '" + calfName + "')");
                               
                                                
                               
                                                System.out.println("Succesfully added " + cowName + "'s newborn cow ID: " + calfID + " NAME: " + calfName + " to the farm");

                                                                                     
                                            } else if (removedCowName.equals("removed from farm")) {
                                                
                                                     System.out.println("COW ID: " + removedCowID + " " + removedCowName + " therefore it cannot produce any more calves.");
                                                
                                                }
                                       }
                                 
                                    } catch(SQLException e) {
                                        
                                       System.out.println("1 " +  e);
                                    }
                                   

                            nextStage();
                       
                         }
                
                   } 
        
         if (!result.next()) {
                          // If the cow hasn't got parent ID, it means it is the first cow in the farm, without parents 
                           System.out.println("There are no cows in the farm, adding the first cow");
                           
                           System.out.print("Enter Cow ID: ");
                           cowID = inputCowDetails.nextLine();
                           System.out.print("Enter Cow Name: ");
                           cowName = inputCowDetails.nextLine();
                           System.out.print("Enter the ID of the calf which has been born to " + cowName + ": ");
                           calfID = inputCowDetails.nextLine();
                           System.out.print("Enter the NAME of the calf which has been born to " + cowName + ": ");
                           calfName = inputCowDetails.nextLine(); 

                           try {
                               
                              statement.executeUpdate("INSERT INTO COWTABLE (COW_ID, COW_NAME) "
                                       + "values('"+ cowID +"', '" + cowName + "')"); 
                               
                               
                              statement.executeUpdate("INSERT INTO COWTABLE (COW_ID, COW_NAME) "
                                       + "values('"+ calfID +"', '" + calfName + "')"); 
                               
                              statement.executeUpdate("INSERT INTO BIRTHTABLE (COW_ID, PARENT_ID, PARENT_NAME, CALF_ID, CALF_NAME) "
                                       + "values((SELECT ID FROM COWTABLE WHERE COW_ID = '" + cowID +"'), null, null, '" + calfID + "', '" + calfName + "')");
                               
                               System.out.println("Succesfully added " + cowName + "'s newborn cow ID: " + calfID + " NAME: " + calfName + " to the farm");
                           
                               
                           } catch (SQLException ex) {
                               
                               System.out.println("Last exception" + ex);
                           }
                           
                        
                           nextStage();
                } 
                           
                           
    } catch (SQLException e) {
                                   
            System.out.println(e);
    }
       
}   
 
    
    
    void nextStage() {
        //Clear the screen
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        System.out.println("\n\n");
        
        Scanner inputChoice = new Scanner(System.in);
        String userInput;
        
        System.out.println("\nTo continue, type one of the options below: \n"
                         + "1 Add cows\n"
                         + "2 Remove a cow from the farm\n"
                         + "3 Print entire farm data\n"
                         + "4 Exit\n");
        System.out.print("Enter your choice: ");
        userInput = inputChoice.nextLine();
        
        
            if (userInput.equals("1")) {
                
                start();
                
            } else if (userInput.equals("2")) {
                
                endLifeSpan();
                
            } else if (userInput.equals("3")){
                
                printFarmData(); 
                
            } else if (userInput.equals("4")){
                
                System.exit(0);
                
            } else {
                
                System.out.println("Please check your entry and try again");
                nextStage();
            }
  }
        
 
    
    void endLifeSpan() {
        
        String cowName;
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        System.out.println("\n");
        
        Scanner inputChoice = new Scanner(System.in);
        String cowID;
        System.out.println("\nEnter the ID of the cow to be removed from the farm's database: \n");
        cowID = inputChoice.nextLine();
        System.out.println("Farm's cow list before the removal");
        
        try {
            
            result = statement.executeQuery("SELECT COW_NAME FROM COWTABLE WHERE COW_ID = '" + cowID + "'");
            
            if (result.next()) {
                
                cowName = result.getString("COW_NAME");
                
                statement.executeUpdate("UPDATE COWTABLE SET COW_NAME = '"
                + "removed from farm' WHERE COW_ID = '" + cowID + "'");
                System.out.println("COW with the ID: " + cowID + " has been removed from the farm");
            }
          
        
        } catch (SQLException e) {
            
            System.out.println(e);
        }
     
      nextStage();  
    }
    
    
    void printFarmData() {
        
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        System.out.println("\n\n\n");
        
                      
          try {
                                   
                result = statement.executeQuery("SELECT COWTABLE.COW_ID, COWTABLE.COW_NAME, " +
                "BIRTHTABLE.CALF_ID, BIRTHTABLE.CALF_NAME, BIRTHTABLE.PARENT_ID " +
                "FROM COWTABLE, BIRTHTABLE WHERE COWTABLE.ID = BIRTHTABLE.COW_ID");
                               
                    while (result.next()) {
                                       
                         System.out.println(result.getString("COW_ID") + ", " + result.getString("COW_NAME" ) + " has given birth to " 
                         + "ID: " + result.getString("CALF_ID") + " NAME: "  + result.getString("CALF_NAME"));
                               
                }
                               
            } catch (SQLException ex1) {
                                   
                      System.out.println(ex1);
            }                     
              
        nextStage();
       
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
       
       SmartFarm farm = new SmartFarm();
       // Clear the screen
       System.out.print("\033[H\033[2J");  
       System.out.flush(); 
       
       
       farm.nextStage();
 
        
    }
    
}
