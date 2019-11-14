
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Said
 */
@ManagedBean(name="avukat")
@SessionScoped
public class Avukat {
    private String name;
    private String surname;
    private String age;
    private String email;
    private String gender;
    private String city;
    private String[] davalar; 
    private String id;
    private String listAll;
    private String davaSelect;
    private List<Avukat> list;
    private String baslik;

    public String getBaslik() {
        return baslik;
    }

    public List<Avukat> getList() {
        return list;
    }
    

    public String getDavaSelect() {
        return davaSelect;
    }

    public void setDavaSelect(String davaSelect) {
        this.davaSelect = davaSelect;
    }
   

    public String getListAll() {
        return listAll;
    }

    public void setListAll(String listAll) {
        this.listAll = listAll;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getDavalar() {
        return davalar;
    }

    public void setDavalar(String[] davalar) {
        this.davalar = davalar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String ekle() throws SQLException{
        Connection conn = VeriTabani.getCon();
        PreparedStatement ps;
        ResultSet rs = null;
        String insertSql = "INSERT INTO AVUKATLAR (AD,SOYAD,EMAİL,CINSIYET,YAS,IL) VALUES (?, ?, ?, ?, ?, ?)";
        try{
            
            ps =conn.prepareStatement(insertSql);
            ps.setString(1, getName());
            ps.setString(2, getSurname());
            ps.setString(3, getEmail());
            ps.setString(4, getGender());
            ps.setInt(5, Integer.parseInt(getAge()));
            ps.setString(6, getCity());
            ps.execute();
            Statement st = conn.createStatement();
            
             // Baktığı davalar tablolarına da avukatın iç içe sorgu id sini ekliyecez
            rs = st.executeQuery("SELECT ID FROM AVUKATLAR WHERE EMAİL ='"+email+"'");
            if(rs.next()){
            for (int i=0; i<davalar.length; i++) {
                String insertDavalar=null;
                if(davalar[i].equals("Aile"))
                   insertDavalar = "INSERT INTO AILE (ID) VALUES (?)";
                else if(davalar[i].equals("Ceza"))
                    insertDavalar = "INSERT INTO CEZA (ID) VALUES (?)";
                else if(davalar[i].equals("İdare"))
                    insertDavalar = "INSERT INTO IDARE (ID) VALUES (?)";
                else if(davalar[i].equals("Şirket"))    
                    insertDavalar = "INSERT INTO SIRKET (ID) VALUES (?)";
                else
                    continue;
                ps = conn.prepareStatement(insertDavalar);
                ps.setInt(1, rs.getInt("ID"));
                ps.execute();
                
                } 
            } 
            
        }catch(NumberFormatException | SQLException e){
                
            System.out.println(e.getMessage());
            VeriTabani.baglantiKapa(conn);
            System.out.println(rs.getInt("ID")+"HATA");
            return "index.html";
        }
        VeriTabani.baglantiKapa(conn);
        return "avukatEkle.xhtml";
    }
    public String sil(){
           Connection conn = VeriTabani.getCon();
           
           PreparedStatement ps;
           String delete = "DELETE FROM AVUKATLAR WHERE ID =?";
           String tablolar[] = new String[]{"AILE","CEZA","IDARE","SIRKET"};
            try{
            ps = conn.prepareStatement(delete);
            ps.setInt(1, Integer.parseInt(getId()));
            ps.execute();
            for(int i=0; i<tablolar.length; i++){
                delete ="DELETE FROM "+tablolar[i]+" WHERE ID =?";
                ps = conn.prepareStatement(delete);
                ps.setInt(1, Integer.parseInt(getId()));
                ps.execute();
            }
             VeriTabani.baglantiKapa(conn);
           }catch(NumberFormatException | SQLException e){
               System.out.println(e.getMessage());
               VeriTabani.baglantiKapa(conn);
           }
                 
           
    
        return "avukatSil.xhtml";
    }
    public String getInfoForUpdate(){
        Connection conn = VeriTabani.getCon();
        ResultSet rs;
         Statement st;
         String getir = "SELECT * FROM AVUKATLAR WHERE id="+getId();
         try{
         st= conn.createStatement();
         rs = st.executeQuery(getir);
         if(rs.next()){
             setId(String.valueOf(rs.getInt("ID")));
             setName(rs.getString("AD"));
             setSurname(rs.getString("SOYAD"));
             setEmail(rs.getString("EMAİL"));
             setGender(rs.getString("CINSIYET"));
             setAge(String.valueOf(rs.getInt("YAS")));
             setCity(rs.getString("IL"));
             
         }
         }catch(SQLException e){
         }
         return "guncelle.xhtml";
    }
    public String guncelle(){
        Connection conn = VeriTabani.getCon();
        PreparedStatement ps = null;
        
        String update = "UPDATE AVUKATLAR SET AD=? , SOYAD=? , EMAİL=? , CINSIYET=? , "
                + "YAS=? , IL=? WHERE ID=?";
        try{
        ps = conn.prepareStatement(update);
        ps.setString(1, getName());
        ps.setString(2, getSurname());
        ps.setString(3, getEmail());
        ps.setString(4, getGender());
        ps.setInt(5, Integer.parseInt(getAge()));
        ps.setString(6, getCity());
        ps.setInt(7,Integer.parseInt(getId()));
        ps.executeUpdate();
        }catch(NumberFormatException | SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Guncellenemedi");
        }
                
    
        return "adminPage.xhtml";
    }
    
    public List<Avukat> getAll(){
            Connection con = VeriTabani.getCon();
            String get = "SELECT * FROM AVUKATLAR ORDER BY ID ASC";
            List<Avukat> list1 = new ArrayList<>();
            try{
            PreparedStatement ps = con.prepareStatement(get);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Avukat aa = new Avukat();
                aa.setId(String.valueOf(rs.getInt("ID")));
                aa.setName(rs.getString("AD"));
                aa.setSurname(rs.getString("SOYAD"));
                aa.setEmail(rs.getString("EMAİL"));
                aa.setGender(rs.getString("CINSIYET"));
                aa.setAge(String.valueOf(rs.getInt("YAS")));
                aa.setCity(rs.getString("IL"));
                list1.add(aa);
            }
            return list1;
            }catch(SQLException e){
                System.out.println("Error:");
                System.out.println(e.getMessage());
            }
            return null;
      }
    public void ara(ActionEvent e){
        list = new ArrayList<>();
        String search = (String)e.getComponent().getAttributes().get("button");
        System.out.println(search);
        
        Connection conn = VeriTabani.getCon();
        PreparedStatement ps;
        Statement st;
        String sql;
        ResultSet rs;
        try{
        if(search.equals("isim")){
            
            sql = "SELECT AD,SOYAD,EMAİL,YAS,IL FROM AVUKATLAR WHERE AD=? ORDER BY ID ASC";
            ps = conn.prepareStatement(sql);
            System.out.println(getName());
            ps.setString(1,getName());
            rs = ps.executeQuery();
            baslik = "ADINA GÖRE AVUKATLAR";
           
            
        }
        else if(search.equals("yas")){
            sql = "SELECT AD,SOYAD,EMAİL,YAS,IL FROM AVUKATLAR WHERE YAS=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(getAge()));
            rs = ps.executeQuery();
            baslik = "YAŞINA GÖRE AVUKATLAR";
        }
        else if(search.equals("sehir")){
            sql = "SELECT AD,SOYAD,EMAİL,YAS,IL FROM AVUKATLAR WHERE IL=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,getCity());
            rs = ps.executeQuery();
            baslik = "ÇALIŞTIĞI İLE GÖRE AVUKATLAR";
        }
        else if(search.equals("cinsiyet")){
            sql = "SELECT AD,SOYAD,EMAİL,YAS,IL FROM AVUKATLAR WHERE CINSIYET=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,getGender());
            rs = ps.executeQuery();
            baslik = "CİNSİYETİNE GÖRE AVUKATLAR";
        }
        else if(search.equals("id1")){
            sql = "SELECT * FROM AVUKATLAR WHERE ID=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(getId()));
            rs = ps.executeQuery();
            baslik = "ID'SİNE GÖRE AVUKATLAR";
        }
        else if(search.equals("ad1")){
            sql = "SELECT * FROM AVUKATLAR WHERE AD=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,getName());
            rs = ps.executeQuery();
            baslik = "ADINA GÖRE AVUKATLAR";
        }
        else if(search.equals("yas1")){
            sql = "SELECT * FROM AVUKATLAR WHERE YAS=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(getAge()));
            rs = ps.executeQuery();
            baslik = "YAŞINA GÖRE AVUKATLAR";
        }
        else if(search.equals("sehir1")){
            sql = "SELECT * FROM AVUKATLAR WHERE IL=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,getCity());
            rs = ps.executeQuery();
            baslik = "ÇALIŞTIĞI ŞEHRE GÖRE AVUKATLAR";
        }
        else if(search.equals("cin1")){
            sql = "SELECT * FROM AVUKATLAR WHERE CINSIYET=? ORDER BY AD ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,getGender());
            rs = ps.executeQuery();
            baslik = "CİNSİYETİNE GÖRE AVUKATLAR";
        }
        else{
            st = conn.createStatement();
            if(davaSelect.equals("Aile")){
                sql = "SELECT AVUKATLAR.AD, AVUKATLAR.SOYAD, AVUKATLAR.EMAİL, AVUKATLAR.YAS, AVUKATLAR.IL "
                        + "FROM AVUKATLAR INNER JOIN AILE ON AVUKATLAR.ID=AILE.ID";
                baslik = "AİLE HUKUKU DAVALARINA BAKAN AVUKATLAR";
                
            }
            else if(davaSelect.equals("Ceza")){
                sql = "SELECT AVUKATLAR.AD, AVUKATLAR.SOYAD, AVUKATLAR.EMAİL, AVUKATLAR.YAS, AVUKATLAR.IL "
                        + "FROM CEZA LEFT JOIN AVUKATLAR ON AVUKATLAR.ID=CEZA.ID";
                baslik = "CEZA HUKUKU DAVAlARINA BAKAN AVUKATLAR";
            }
            else if(davaSelect.equals("İdare")){
                sql = "SELECT AVUKATLAR.AD, AVUKATLAR.SOYAD, AVUKATLAR.EMAİL, AVUKATLAR.YAS, AVUKATLAR.IL "
                        + "FROM AVUKATLAR RIGHT JOIN IDARE ON AVUKATLAR.ID=IDARE.ID";
                baslik = "İDARE ve VERGİ HUKUKU DAVALARINA BAKAN AVUKATLAR";
            }
            else{
                sql = "SELECT AVUKATLAR.AD, AVUKATLAR.SOYAD, AVUKATLAR.EMAİL, AVUKATLAR.YAS, AVUKATLAR.IL "
                        + "FROM AVUKATLAR RIGHT JOIN SIRKET ON AVUKATLAR.ID=SIRKET.ID";
                baslik = "ŞİRKETLER HUKUKU DAVALARINA BAKAN AVUKATLAR";
            }
            
            rs = st.executeQuery(sql);
        }
         
           while(rs.next()){
               System.out.println(rs.getString("AD")+"-"+rs.getString("SOYAD")+"-"+rs.getString("EMAİL")+"-"
                       + String.valueOf(rs.getInt("YAS"))+"-"+rs.getString("IL"));
               Avukat aa = new Avukat();
               
               aa.setId(String.valueOf(rs.getInt("ID")));
               aa.setName(rs.getString("AD"));
               aa.setSurname(rs.getString("SOYAD"));
               aa.setEmail(rs.getString("EMAİL"));
               aa.setAge(String.valueOf(rs.getInt("YAS")));
               aa.setCity(rs.getString("IL"));
               list.add(aa);
               
           }
                 
            
        
           
        }catch(SQLException ex){
            System.out.println("Hata:");
            System.out.println(ex.getMessage());
        }
        
        
        
        
        
    }
    
    
    
    
    }
    

