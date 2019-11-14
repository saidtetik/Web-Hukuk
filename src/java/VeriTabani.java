
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ProgApp
 */
public class VeriTabani {
   
    
    public static void baglantiKapa(Connection con)
    {
        try {
            con.close();
        } catch (SQLException e) {
            
            System.out.println("Baglanti kapatilirken hata olustu!");
        }
    }
    
    public static Connection getCon()
    {
        Connection con = null;
        String url = "jdbc:derby://localhost:1527/hukuk";
        String user = "hukuk", pass = "hukuk";
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            
            System.out.println("Veritabanina Baglanilamadi!\nDetay : "+e.getMessage());
            return null;
        }
        
        return con;
    }
    
   
}
