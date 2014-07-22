
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class TableCreator {

    public static void main(String args[]) throws Exception{
       // Class.forName("org.hsqldb.jdbc.JDBCDriver");
        //String url ="jdbc:hsqldb:file:typesdb" ;
       String url ="jdbc:derby:resources/data/monodb;create=true";
        Connection conn = DriverManager.getConnection(url,"app","");
        Statement stmt = conn.createStatement();
       String sql;
      // sql= "drop table TYPES";
         //stmt.executeUpdate(sql);
        // sql="create table TYPES ( uid INT , type VARCHAR(10),hp FLOAT, kw FLOAT,sucnsize FLOAT,delsize FLOAT,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT,headLower FLOAT,headUpper FLOAT, voltage INTEGER, phases INTEGER)";
       //stmt.executeUpdate(sql);
         sql="create table OBSVALUES ( obid INT , type VARCHAR(10),sno VARCHAR(10), obdate DATE,head FLOAT,disch FLOAT,eff FLOAT,mcurrent FLOAT)";
       stmt.executeUpdate(sql);
       
       // sql = "select * from types";
       //ResultSet rs = stmt.executeQuery(sql);
       //rs.next();
       //System.out.print(rs.getString("type")+rs.getInt("uid"));
        //stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
        System.out.println("Created successfully");

}

}
