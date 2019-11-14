
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.ejb.Stateless;
import javax.ejb.Singleton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Said
 */
@ManagedBean(name="giris")
@SessionScoped
public class Giris{
    
    private String username;
    private String password;
    private String adminName;
    private String adminSurname;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminSurname() {
        return adminSurname;
    }

    public void setAdminSurname(String adminSurname) {
        this.adminSurname = adminSurname;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  
    
     public String loginControl()
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection baglanti = VeriTabani.getCon();
        
        if(baglanti == null)
        {
           return "index.html";
        }
        
        String sqlSorgu = "Select * From ADMIN Where KULLANICI='"+getUsername()+"' and SIFRE='"+getPassword()+"'";
        
        try 
        {
            Statement st = baglanti.createStatement();
            rs = st.executeQuery(sqlSorgu);
            
                if(rs.next()==true){
                    if(rs.getString("SIFRE").equals(getPassword()) && rs.getString("KULLANICI").equals(getUsername()))
                    {
                        // Admin ismini admin sayfasına yazdırmak için 2 tablodan sorgu yapıyoruz.
                        String getir = "SELECT name,surname FROM USERS Where id =(SELECT id FROM ADMIN Where "
                                + "kullanıcı='"+getUsername()+"' and sifre ='"+getPassword()+"')";
                        rs = st.executeQuery(getir);
                        rs.next();
                        setAdminName(rs.getString("Name"));
                        setAdminSurname(rs.getString("SURNAME"));
                    VeriTabani.baglantiKapa(baglanti);
                    return "adminPage.xhtml";//Sifre dogru
                    }
                }
                else
                {
                    System.out.println("Kullanıcı adı ya da şifrenizi hatalı girdiniz!");
                    VeriTabani.baglantiKapa(baglanti);
                    return "hataolustu.xhtml";
                }
           
        } 
        catch (SQLException e) {
            
            System.out.println("Sorgulama yaparken hata olustu!");
            System.out.println(e.getMessage());
            VeriTabani.baglantiKapa(baglanti);
            
            return "hataolustu2.xhtml";//Veritabani sorgulamasinda hata meydana geldi mesaji
        }
    return "index.html";
    }
     
}
