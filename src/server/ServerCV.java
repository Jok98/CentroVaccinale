package server;

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
	private static final String url = "jdbc:postgresql://127.0.0.1/Laboratorio";
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
	private ArrayList<Object> Eventi_Avversi = new ArrayList<Object>();
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

	    
	   
	    try {
	      while (true) {
	        Socket socket = s.accept();
	        new ServerThread(socket);
	        boolean connesso = s.isBound();
	        System.out.println(connesso);
	        
	        String create_table_centro = "CREATE TABLE IF NOT EXISTS centrivaccinali "+"(siglaprov varchar(2),numciv int ,cap int,comune varchar(20),nome varchar(20) PRIMARY KEY,"
	        		+ "indirizzo varchar(30),tipologia varchar(20), severita_media int, n_segnalazioni int )";
	    	createTable(conn,create_table_centro);
	        
	        String create_table_cittadini = "CREATE TABLE IF NOT EXISTS cittadini_registrati ( nome varchar(20),cognome varchar(20),"
	        		+ "codfisc varchar(16)PRIMARY KEY, email varchar(30),userid varchar(16),password varchar(30),id varchar(20))";
	    	createTable(conn,create_table_cittadini);
	    	
	    	String create_table_id = "CREATE SEQUENCE IF NOT EXISTS IDprog AS INT START WITH 1 INCREMENT BY 1 ";
	    	createTable(conn,create_table_id);
	
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
    	System.out.println("La richiesta inviata dal client : " +cs.getRichiesta());
    	switch(cs.getRichiesta()) {
    		
    	case "centroVax" :
    		registraCentroVaccinale(conn,(CentroVaccinale) cs.getObj()); 
    		break;
    	
    	case "registrazioneVaccinato" :
    		int id = set_get_Id(conn);
    		registraVaccinato(conn,(Utente) cs.getObj(), id);
    		cs.setRichiesta("IdUnivoco");
    		cs.setObj(id);
    		cs.getObj();
    		oout.writeObject(cs);
    		System.out.println("id univoco del vaccinato : "+id);
    		break;
    		
    	case "registrazioneCittadino" :
    		registraCittadino(conn,(Utente) cs.getObj());
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
    		Eventi_Avversi = (ArrayList<Object>) cs.getObj();
    		System.out.println("Eventi avversi ricevuti dal sever : "+Eventi_Avversi);
    		inserisciEventiAvversi(conn, Eventi_Avversi );
    		break;
    	
    	case "LogIn":
    		datiLogIn = (HashMap<String, String>) cs.getObj();
    		Boolean logIn_result = loginCittadino(conn, datiLogIn);
    		cs.setRichiesta("LogIn");
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

public static int set_get_Id(Connection conn) {

	int id = 0 ;
	try {
		String query = "SELECT nextval('idprog') FROM idprog";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
		id = rs.getInt(1);
		
		}
		
		

	} catch (SQLException e) {
		e.printStackTrace();
		return -1;
	}
	return id;
}

public static ArrayList<CentroVaccinale> cercaCentroVaccinale(PreparedStatement statement) {
	ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>() ;
	CentroVaccinale cv = null;
	String nome,via,citta,prov,tip = null;
	int CAP;
	int nciv;
	
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


public static  boolean registraVaccinato(Connection conn, Utente utente, int id) {

	String create_table_query = "CREATE TABLE IF NOT EXISTS vaccinati_"+utente.getCentroVax()+"(id_univoco int PRIMARY KEY ,namecv varchar(60), nome varchar(60), cognome varchar(60), codfisc varchar(16), datavacc date, vaccino varchar(20))";
	createTable(conn,create_table_query);
	System.out.println("il centro vac ricevuto dal server : "+utente.getCentroVax());
	try {
		String state = "INSERT INTO vaccinati_"+utente.getCentroVax()+"(id_univoco,namecv,nome,cognome,codfisc,datavacc,vaccino)"+ "VALUES (?,?,?,?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(state);	
		statement = conn.prepareStatement(state);	
		statement.setInt(1, id);
		statement.setString(2, utente.getCentroVax());
		statement.setString(3, utente.getNome());
		statement.setString(4, utente.getCognome());
		statement.setString(5, utente.getCodfisc());
		statement.setDate(6, utente.getDatavacc());
		statement.setString(7, utente.getVacc());
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {	
		e.printStackTrace();
		return false;
	}
	return true;
}

@SuppressWarnings("unused")
public static  boolean createTable(Connection conn, String create_table_query) {

			try {
				PreparedStatement statement = conn.prepareStatement(create_table_query);
				statement.executeUpdate();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
		return true;
}

public static boolean inserisciEventiAvversi(Connection conn, ArrayList<Object> Eventi_Avversi) {
	String centroVax = (String) Eventi_Avversi.get(0);
	String create_table_query = "CREATE TABLE IF NOT EXISTS "+centroVax+"_eventi_avversi (mal_di_testa INTEGER, note_mal_di_testa varchar(256),"
			+ " febbre INTEGER, note_febbre varchar(256), dolori_muscolari INTEGER, note_dolori_muscolari varchar(256),"
			+ " linfoadenopatia INTEGER, note_linfoadenopatia varchar(256), tachicardia INTEGER, note_tachicardia varchar(256), crisi_ipertensiva INTEGER, note_crisi_ipertensiva varchar(256) )";
	
	createTable(conn,create_table_query);
	
	String upd = "INSERT INTO "+centroVax+"_eventi_avversi ( mal_di_testa , note_mal_di_testa , febbre, note_febbre, dolori_muscolari,"
			+ " note_dolori_muscolari, linfoadenopatia, note_linfoadenopatia,tachicardia, note_tachicardia ,"
			+ " crisi_ipertensiva, note_crisi_ipertensiva) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	try {
		PreparedStatement statement = conn.prepareStatement(upd);
		int somma_ev_av = 0 ;
		for (int i = 1 ; i < 13; i++) {
			if(i%2!=0) {
				somma_ev_av = (int) Eventi_Avversi.get(i)+somma_ev_av;
				statement.setInt(i, (int) Eventi_Avversi.get(i) );
				
			}else statement.setString(i, (String) Eventi_Avversi.get(i));
		}
		
		int sev_media_att = somma_ev_av/6;
		System.out.println(sev_media_att);

		updateEventiAvversi(centroVax,sev_media_att);
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

public static void updateEventiAvversi(String centroVax, int sev_media_att) {
	 
	 
	String query = "SELECT severita_media, n_segnalazioni FROM centrivaccinali WHERE nome=?" ;
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, centroVax);
		ResultSet rs = statement.executeQuery();
		rs.next();
		int sevMediaPrec = rs.getInt(1);
		int nEvAvPrec = rs.getInt(2);
		int nEvAv = nEvAvPrec+1;
		int sev_media_tot = (sev_media_att+sevMediaPrec)/2;
		statement = conn.prepareStatement("UPDATE centrivaccinali SET severita_media = ?, n_segnalazioni = ? WHERE nome = ?;");
		statement.setInt(1, sev_media_tot);
		statement.setInt(2, nEvAv);
		statement.setString(3, centroVax);
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}

	
}


public static boolean loginCittadino(Connection conn,HashMap <String, String> datiLogIn) {

	//bisogna controllare il centro vax dell'utente
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

public static  boolean registraCittadino(Connection conn, Utente user) throws SQLException {

	Boolean successo = true ;
	String query = "SELECT * FROM vaccinati_" + user.getCentroVax() +" WHERE codfisc = ? AND id_univoco = ?";
	PreparedStatement statement = conn.prepareStatement(query);		
	statement.setString(1, user.getCodfisc());
	statement.setInt(2, user.getIdvax());
	
	successo = checkUserData(conn, statement);

	if (successo == false) {
		System.out.println("Centro vaccinale o ID univoco non corrispondono");
		return false ;
	} 
	else {	
		String stmt = "INSERT INTO cittadini_registrati (nome,cognome,codfisc,email,userid,password,id)"+ "VALUES (?,?,?,?,?,?,?)";
		try {
			statement = conn.prepareStatement(stmt);
			statement.setString(1, user.getNome());
			statement.setString(2, user.getCognome());
			statement.setString(3, user.getCodfisc());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getUserID());
			statement.setString(6, user.getPassword());
			statement.setInt(7, user.getIdvax());
			statement.executeUpdate();
			System.out.println("Cittadino "+user.getNome()+" registrato correttamente");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return true;
}

public static boolean checkUserData(Connection conn, PreparedStatement statement) {
	 
	 try {
			ResultSet rs = statement.executeQuery();
			if (rs.next() == false) { 
				return false;
			}
			} catch (SQLException a) {
				a.printStackTrace();
			}
	 return true ;
}





}