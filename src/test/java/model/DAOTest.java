/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

import org.apache.derby.jdbc.*; 
/**
 *
 * @author pauld
 */
public class DAOTest {
    
    private static DataSource myDataSource;
    private static Connection myConnection ;
    private static DAO myObject;
    
    
    @Before
    public void setUp() throws SQLException, IOException, SqlToolError {
        // On crée la connection vers la base de test "in memory"
	myDataSource = getDataSource();
	myConnection = myDataSource.getConnection();
	// On crée la base test
	executeSQLScript(myConnection, "basetest.sql");		
        myObject = new DAO(myDataSource);
        
    }
    
    private static void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
	// On initialise la base avec le contenu d'un fichier de test
	String sqlFilePath = DAOTest.class.getResource(filename).getFile();
	SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
	sqlFile.setConnection(connexion);
	sqlFile.execute();
	sqlFile.closeReader();		
	}
    
    	public static DataSource getDataSource() throws SQLException {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();        
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}
    
    @After
    public void tearDown() throws SQLException {
        myConnection.close();
    }
    

   
    
    @Test
    public void testInsertPurshase() throws SQLException {
        String sql = "Select Count(*) from PURCHASE_ORDER";
        int before = 0;
        int after = 0;
		try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    ResultSet rs = stmt.executeQuery();
                    before = rs.getInt(1);
                } 
        try{
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            myObject.insertPurchase(88888,1, 54, 0, 500, date , date, "CaptObvious");
            try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    ResultSet rs = stmt.executeQuery();
                    after = rs.getInt(1);
                }
            assertEquals("Insertion ratée", before+1,after);
            
        }catch(Exception e){ 
        }
    }

    /*
    @Test
    public void testMaxId() throws Exception {
        int before =myObject.maxId();
        //System.out.println(before);
        try{      
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            myObject.insertPurchase(before,1, 54, 0, 500,date, date, "CaptObvious");
            
       } catch (Exception ex){}
            //les deux doivent être identiques
            int after =myObject.maxId();
            //System.out.println(after);
            assertEquals("Balance incorrecte !", before+1,after);
    }
    */
    
}
