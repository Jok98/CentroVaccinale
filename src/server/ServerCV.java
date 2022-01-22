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
import java.util.HashMap;
import java.util.Scanner;

import centrivaccinali.*;
import cittadini.Utente;





public class ServerCV {
	private static final String url = "jdbc:postgresql://127.0.0.1/Lab B";
	private static final String user = "postgres";
	private static final String password = "admin";
	static Connection conn = null;
	static Scanner sc= new Scanner(System.in);
	public static final int PORT = 8083;
	
	

static public class ServerThread extends Thread{
	private Socket socket;
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	private PreparedStatement statement;
	private static ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
	private HashMap<String, Integer> Eventiavversi = new HashMap<>();
	private HashMap <String, String> datiLogIn = new HashMap <String, String>();
	
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


	  
@SuppressWarnings("unchecked")
public void run() {
		
    try {
    	
    	
    	
    	ConnessioneServer cs =   (ConnessioneServer) oin.readObject();
    	System.out.println("La richiesta è : " +cs.getRichiesta());
    	switch(cs.getRichiesta()) {
    		
    	case "centroVax" :
    		registraCentroVaccinale(conn,(CentroVaccinale) cs.getObj()); 
    		break;
    	
    	case "registrazioneVaccinato" :
    		registraVaccinato(conn,(Utente) cs.getObj());
    		break;
    	
    	case "srcCentroVax" :
    		System.out.println("trasferimento dati eseguito");
    		String n = "SELECT * FROM centrivaccinali WHERE nome LIKE ? ";
    		statement = conn.prepareStatement(n);
    		String nomeCentro = (String) cs.getObj();
    		//System.out.println("base di ricerca centro vax : "+nomeCentro);
    		statement.setString(1, (nomeCentro+"%"));
        	cvlis = cercaCentroVaccinale(statement);
        	//System.out.println("centro vax trovati: "+cvlis);
        	oout.writeObject(cvlis);
        	cvlis.clear();
    		break;
    		
    	case "ricercaCVComuneTipologia" :
    		String ct = "SELECT * FROM centrivaccinali WHERE comune LIKE ? AND tipologia=?";
    		statement = conn.prepareStatement(ct);
    		String[] ComuneTip = (String[]) cs.getObj();
			statement.setString(1, ComuneTip[0]);
			statement.setString(2, ComuneTip[1]);
			System.out.println("Il server ha ricevuto richiesta per ricerca di : "+ComuneTip[0]+" "+ComuneTip[1]);
			cvlis = new ArrayList<CentroVaccinale>();
        	cvlis = cercaCentroVaccinale(statement);
        	oout.writeObject(cvlis);
			cvlis.clear();
    		break;
    	/**
    	 * TODO gestire media eventi avversi e rinviare al client	
    	 */
    	case "eventiAvversi":
    		Eventiavversi = (HashMap<String, Integer>) cs.getObj();
    		System.out.println("funziona" );
    		System.out.println(Eventiavversi);
    		break;
    	
    	case "LogIn":
    		datiLogIn = (HashMap<String, String>) cs.getObj();
    		Boolean logIn_result = loginCittadino(conn, datiLogIn);
    		//TODO inserire come parametro logIn_result al posto di true dopo che si è verificata che l'interazione col db sia corretta
    		cs.setObj(logIn_result);
    		cs.getObj();
    		oout.writeObject(cs);
    		break;
    	
    	}
    	 socket.close();
    } catch (IOException | ClassNotFoundException | SQLException e) {
      System.err.println("IO Exception");
		e.printStackTrace();
	} 
   
	}
  

}


public static ArrayList<CentroVaccinale> cercaCentroVaccinale(PreparedStatement statement) {
	ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>() ;
	CentroVaccinale cv = null;
	String nome,via,citta,prov,tip = null;
	int CAP;
	int nciv;
	
	try {
		if(!conn.isValid(5)) {
			System.out.println("Connessione col database non stabile o assente");
		} else {
			System.out.println("Connessione col database valida");
		}
		} catch (SQLException a) {
		a.printStackTrace();
		}
	
		try {
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
                prov = rs.getString(1);
                nciv = rs.getInt(2);
                CAP = rs.getInt(3);
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
			statement.setInt(2, cv.getNciv());
			statement.setInt(3, cv.getCap());
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

public static  boolean registraCittadino(Connection conn, Utente user) {
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
	String query = "SELECT * FROM centrivaccinali WHERE comune=?";
	
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
	createTablevacc(conn, ut.getCvacc());
	int id ;
	System.out.println("il centro vac ricevuto dal server : "+ut.getCvacc());
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

		String risultato = null;
		String query = "SELECT * FROM centrivaccinali WHERE nome=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);		
			statement.setString(1, nomecv);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				risultato = rs.getString(5);
            }
		if (!(risultato == null)) {
			String qs = "CREATE TABLE IF NOT EXISTS vaccinati_"+nomecv+"(user_id SERIAL NOT NULL PRIMARY KEY,namecv varchar(60),nome varchar(60), cognome varchar(60), codfisc varchar(16), datavacc date, vaccino varchar(20))";
			statement = conn.prepareStatement(qs);	
			statement.executeUpdate();
			statement.close();
		} else {
			return false;
		}
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	 }

public static boolean loginCittadino(Connection conn,HashMap <String, String> datiLogIn) {

	String query = "SELECT userid,password FROM cittadini_registrati WHERE userid=? AND password=?" ;
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, datiLogIn.get("ID"));
		statement.setString(2, datiLogIn.get("Password"));
		ResultSet rs = statement.executeQuery();
		if (rs.next() == false) { 
			return false;
		} else {
			return true;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return false;
}

/*public static  boolean registraCittadino(Connection conn, Utente user) {
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
	Boolean errore = false ;
	String nomecvacc = user.getNome();
	String query = "SELECT * FROM vaccinati_"+nomecvacc+" WHERE nome=? AND cognome=? AND codfisc=? AND user_id=? ";
	try {
	PreparedStatement statement = conn.prepareStatement(query);		
	statement.setString(1, user.getNome());
	statement.setString(2, user.getCognome());
	statement.setString(3, user.getCodfisc());
	statement.setInt(4, user.getIdvacc());
	ResultSet rs = statement.executeQuery();
	if (rs.next() == false) { 
		errore = true ;
	}
	} catch (SQLException a) {
		a.printStackTrace();
	}
	if (errore == true) {
		return false ;
	} 
	else {
		System.out.println("sto per inserire");
		String stmt = "INSERT INTO cittadini_registrati (nome,cognome,codfisc,email,userid,password,id)"+ "VALUES (?,?,?,?,?,?,?)";
		try {
			PreparedStatement statement = conn.prepareStatement(stmt);
			statement.setString(1, user.getNome());
			statement.setString(2, user.getCognome());
			statement.setString(3, user.getCodfisc());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getUserid());
			statement.setString(6, user.getPswd());
			statement.setInt(7, user.getIdvacc());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return true;
}*/

/*public static boolean inserisciEventiAvversi(Connection conn, Richiesta op) {
	try {
		if(!conn.isValid(5)) {
			System.err.println("Connessione col database non stabile o assente");
			return false;
		} else {
			System.out.println("Connessione col database valida");
		}
	} catch (SQLException a) {
		a.printStackTrace();
	}
	String upd = "INSERT INTO eventiavv (mal di testa, note mal di testa, febbre, note febbre, dolori muscolari, 
	note dolori muscolari, linfoadenopatia, note linfoadenopatia,tachicardia, note tachicardia ,crisi ipertensiva,note crisi ipertensiva) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	int eventiavv[] = op.getEventiavv();
	String eventiavvnote[] = op.getEventiavvnote();
	try {
		PreparedStatement statement = conn.prepareStatement(upd);
		for (int i = 0 ; i <= 5; i++) {
			statement.setInt((i*2) + 1 , eventiavv[i]);
			statement.setString((i*2) + 2 , eventiavvnote[i]);
		}
		statement.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}*/



}