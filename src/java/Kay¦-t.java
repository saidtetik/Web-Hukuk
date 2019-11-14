
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Said
 */
@ManagedBean(name="kayit")
@RequestScoped
public class Kayıt {
    private static final String CHECK_SIFRE ="hukukweb";
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String checkSifre;

    public String getCheckSifre() {
        return checkSifre;
    }

    public void setCheckSifre(String checkSifre) {
        this.checkSifre = checkSifre;
    }

    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String kayitOl() throws SQLException{
        ResultSet rs = null;
        PreparedStatement ps= null;
        Connection baglanti = VeriTabani.getCon();
        if(!getCheckSifre().equals(CHECK_SIFRE)){
            System.out.println("Adminin verdiği şifre yanlış");
            return "kayitOl.xhtml";
        }
        if(baglanti == null)
        {
           return "index.html";
        }
        String userInsert = "INSERT INTO USERS (name,surname,email,username,password,id) values (?, ?, ?, ?, ?,?)";
        String adminInsert = "INSERT INTO ADMIN (kullanıcı,sifre) values (?, ?)";
        try{
            // Kullanıcı adı ve şifre tablosuna ekledik
            ps = baglanti.prepareStatement(adminInsert);
            ps.setString(1, getUsername());
            ps.setString(2, getPassword());
            ps.executeUpdate();
            
            // Kayıt form bilgisini ekliyecez
            String getirId = "SELECT ID FROM ADMIN WHERE kullanıcı='"+getUsername()+"' and sifre='"+getPassword()+"'";
            Statement st = baglanti.createStatement();
            rs = st.executeQuery(getirId);
            rs.next();
            
            int id = rs.getInt("ID");
            ps = baglanti.prepareStatement(userInsert);
            ps.setString(1, getName());
            ps.setString(2, getSurname());
            ps.setString(3, getEmail());
            ps.setString(4, getUsername());
            ps.setString(5, getPassword());
            ps.setInt(6,id);
            ps.executeUpdate();

            VeriTabani.baglantiKapa(baglanti);
            System.out.println("2 tabloya da eklendi");
        }
        catch(SQLException e){
            VeriTabani.baglantiKapa(baglanti);
            System.out.println(e.getMessage());
            return "hataolustu2.xhtml";
        }
        return "adminGiris.xhtml";
    }
}
