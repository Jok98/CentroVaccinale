package server;

import java.sql.Statement;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import centrivaccinali.*;
import cittadini.Utente;

public class ServerCV {
	private static final String url = "jdbc:postgresql://127.0.0.1/Lab B";
	private static final String user = "postgres";
	private static final String password = "Bonardabuono";
	static Connection conn = null;
	static Scanner sc= new Scanner(System.in);
	public static final int PORT = 8083;
	

static public class ServerThread extends Thread{
	private Socket socket;
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	
	
	ServerThread (Socket s){
		socket = s;
		try {
			oin = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) { e.printStackTrace(); }
		start();
	}	

	
public static void main(String[] args) throws IOException, SQLException {
	    ServerSocket s = new ServerSocket(PORT);  
	    conn = connect();
	    	System.out.println("Server started");
	 	    System.out.println(s.getLocalSocketAddress());
	    
	   
	    try {
	      while (true) {
	        Socket socket = s.accept();
	        new ServerThread(socket);
	        boolean connesso = s.isBound();
	        System.out.println(connesso);
	    }
	    } finally {
	      s.close();
	    }
	}
	

	public static Connection connect() {
	Connection conn = null;
	  try {
          conn = DriverManager.getConnection(url, user, password);
          System.out.println("Connessione al server PostgreSQL effettuata.");
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
      return conn;
  }


	  
public void run() {
    try {
    	
    	ConnessioneServer cs =   (ConnessioneServer) oin.readObject();
    	System.out.println(cs.getRichiesta());
    	switch(cs.getRichiesta()) {
    		
    	case "centroVax" :
    		registraCentroVaccinale(conn,(CentroVaccinale) cs.getObj()); 
    		break;
    	
    	case "registrazioneVaccinato" :
    		registraVaccinato(conn,(Utente) cs.getObj());
    		break;
    	
    	case "srcCentroVax" :
    		ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
    		
        	cvlis = cercaCentroVaccinale(conn,(String) cs.getObj());
        	oout.writeObject(cvlis);
    		break;
    	
    	}

      
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("IO Exception");
		e.printStackTrace();
	} 
    finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.println("Socket not closed");
      }
    }
}	
}

public static  boolean registraCentroVaccinale(Connection conn, CentroVaccinale cv) {
	try {
		if(!conn.isValid(5)) {
			System.out.println("Connessione col database non stabile o assente");
			return false;
		} else {
			System.out.println("Connessione col database valida");
		}
		} catch (SQLException a) {
		a.printStackTrace();
		}
		String state = "INSERT INTO centrivaccinali (siglaprov,numciv,cap,comune,nome,indirizzo,tipologia)"+ "VALUES (?,?,?,?,?,?,?)";
		try {
			
			PreparedStatement statement = conn.prepareStatement(state);	
			statement.setString(1, cv.getProv());
			statement.setString(2, cv.getNciv());
			statement.setString(3, cv.getCap());
			statement.setString(4, cv.getComune());
			statement.setString(5, cv.getNome());
			statement.setString(6, cv.getVia());
			statement.setString(7, cv.getTipologia());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	 }

public static  boolean registraVaccinato(Connection conn, Utente ut) {
	try {
		if(!conn.isValid(5)) {
			System.out.println("Connessione col database non stabile o assente");
			return false;
		} else {
			System.out.println("Connessione col database valida");
		}
		
	} catch (SQLException a) {
		a.printStackTrace();
	}
	int id ;
	String query = "SELECT * FROM vaccinati_"+ut.getCvacc();
	try {
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);		
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.next()) {
			id = 1;				
		}
		else {
			rs.last();
			id = rs.getInt(1) + 1;
		}
		String state = "INSERT INTO vaccinati_"+ut.getCvacc()+"(user_id,namecv,nome,cognome,codfisc,datavacc,vaccino)"+ "VALUES (?,?,?,?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(state);	
		statement = conn.prepareStatement(state);	
		statement.setInt(1,id);
		statement.setString(2, ut.getCvacc());
		statement.setString(3, ut.getNome());
		statement.setString(4, ut.getCognome());
		statement.setString(5, ut.getCodfisc());
		statement.setDate(6, ut.getDatavacc());
		statement.setString(7, ut.getVacc());
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {	
			e.printStackTrace();
			return false;
		}
	return true;
}

public static  boolean createTablevacc(Connection conn, String nomecv) {
	try {
		if(!conn.isValid(5)) {
			System.out.println("Connessione col database non stabile o assente");
			return false;
		} else {
			System.out.println("Connessione col database valida");
		}
		} catch (SQLException a) {
			a.printStackTrace();
			return false;
		}
        String qs = "CREATE TABLE IF NOT EXISTS vaccinati_"+nomecv+"(user_id SERIAL NOT NULL PRIMARY KEY,namecv varchar(60),nome varchar(60), cognome varchar(60), codfisc varchar(16), datavacc date, vaccino varchar(20))";
		try {
			
			PreparedStatement statement = conn.prepareStatement(qs);	
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	 }

public static ArrayList<CentroVaccinale> cercaCentroVaccinale(Connection conn, String comune) {
	ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
	CentroVaccinale cv = null;
	String nome,via,nciv,citta,prov,CAP,tip = null;
	
	try {
		if(!conn.isValid(5)) {
			System.out.println("Connessione col database non stabile o assente");
		} else {
			System.out.println("Connessione col database valida");
		}
		} catch (SQLException a) {
		a.printStackTrace();
		}
		//String nomecentro = "centrivaccinali";	
		//String query = "SELECT siglaprov,numciv,cap,comune,nome,indirizzo,tipologia FROM "+nomecentro+" WHERE comune='molina'";
		//String query = "SELECT * FROM centrivaccinali WHERE comune="+"'"+comune+"'";
		String query = "SELECT * FROM centrivaccinali WHERE comune=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);		
			statement.setString(1, comune);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
                prov = rs.getString(1);
                nciv = rs.getString(2);
                CAP = rs.getString(3);
                citta = rs.getString(4);
                nome = rs.getString(5);
                via = rs.getString(6);
                tip = rs.getString(7);
                cv = new CentroVaccinale (nome, via ,nciv, citta, prov, CAP, tip);
                cvlis.add(cv);
            }
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cvlis;
	 }

}
