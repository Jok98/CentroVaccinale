package server;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.HeadlessException;
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

public class ServerThread extends Thread{
	
	private static String url = "jdbc:postgresql://127.0.0.1/Laboratorio";
	private static  String user = "postgres";
	private static  String password = "admin";
	static Connection conn = null;
	static Scanner sc= new Scanner(System.in);
	public static int PORT = 8083;
	private Socket socket;
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	private PreparedStatement statement;
	
	private static ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
	private ArrayList<Object> Eventi_Avversi = new ArrayList<Object>();
	private HashMap <String, String> datiLogIn = new HashMap <String, String>();
	private static Boolean first_AD=true;
	//private static Scanner scanner = new Scanner(System.in);
	
	ServerThread (Socket s){
		socket = s;
		try {
			oin = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) { e.printStackTrace(); }
		start();
	}	

	
public static void main(String[] args) {
	    try {
	    	
	      while (true) {
	    	/*do {
	    	 	 String changeDb="";
	    		 System.out.println("Vuoi modificare dati login database? y/n");
	    		 Scanner scanner = new Scanner(System.in);
	    		 changeDb  = scanner.next();
	    	 }while((!changeDb.equals("y"))&(!changeDb.equals("n")));
	    	 //System.out.println("digitato : "+changeDb);
	    	 if(changeDb.equals("y")) {
	    		 System.out.println("inserire nuovi parametri : \r");
	    		 System.out.println("inserire nuovo nome database : \r");
	    		 url = "jdbc:postgresql://127.0.0.1/"+scanner.nextLine();
	    		 System.out.println("inserire nuovo user db : \r");
	    		 user = scanner.nextLine();
	    		 System.out.println("inserire nuova password db : \r");
	    		 password = scanner.nextLine();
	    	 }*/
	    	 
	    	 ServerSocket s = new ServerSocket(PORT);  
	    	 conn = connect();
	    	 if(first_AD==true)showMessageDialog(null,"Server started"); else first_AD=false;
		     System.out.println("Server started");
	         Socket socket = s.accept();
	         new ServerThread(socket);
	         boolean connesso = s.isBound();
	         System.out.println(connesso);
	        
	         String create_table_centro = "CREATE TABLE IF NOT EXISTS centrivaccinali "+"(siglaprov varchar(2),numciv int ,cap int,comune varchar(20),nome varchar(60) PRIMARY KEY,"
	        		+ "indirizzo varchar(60),tipologia varchar(20), severita_media float DEFAULT -1 , n_segnalazioni float DEFAULT 0 )";
	    	 createTable(conn,create_table_centro);
	        
	         String create_table_cittadini = "CREATE TABLE IF NOT EXISTS cittadini_registrati ( nome varchar(20),cognome varchar(20),"
	        		+ "codfisc varchar(16)PRIMARY KEY, email varchar(30),userid varchar(16),password varchar(30),id varchar(20), centroVax varchar(60))";
	    	 createTable(conn,create_table_cittadini);
	    	 //65535 valore massimo 16bit
	    	 String create_table_id = "CREATE SEQUENCE IF NOT EXISTS IDprog AS INT START WITH 1 INCREMENT BY 1 MAXVALUE 65535";
	    	 createTable(conn,create_table_id);
	    	 s.close();
	
	    }
	    } catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	      
	    }
	}
	
	public static Connection connect() {
	Connection conn = null;
	  try {
          conn = DriverManager.getConnection(url, user, password);
          System.out.println("Connessione al database PostgreSQL effettuata");
          if(first_AD==true)showMessageDialog(null,"Connessione al database PostgreSQL effettuata");
          //showMessageDialog(null,"Connessione al database PostgreSQL effettuata");
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
    		
    		int id =registraVaccinato(conn,(Utente) cs.getObj());
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
			statement.setString(1, (ComuneTip[0]+"%"));
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
    		String logIn_result = loginCittadino(conn, datiLogIn);
    		String centroVax = getCentroVax(conn, datiLogIn.get("ID"));
    		cs.setRichiesta("LogIn");
    		datiLogIn.clear();
    		datiLogIn.put("logInResult", logIn_result);
    		datiLogIn.put("centroVax", centroVax);
    		cs.setObj(datiLogIn);
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



public static String getCentroVax(Connection conn, String idUser) {
	
	String centroVax = null;
	String query = "SELECT centroVax FROM cittadini_registrati WHERE userid = ?";
		
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, idUser);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			centroVax = rs.getString(1);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return centroVax;
}


public static ArrayList<CentroVaccinale> cercaCentroVaccinale(PreparedStatement statement) {
	ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>() ;
	CentroVaccinale cv = null;
	String nome,via,citta,prov,tip = null;
	int nciv, CAP,severita_media, n_segnalazioni ;

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
                severita_media = rs.getInt(8);
                n_segnalazioni = rs.getInt(9);
                cv = new CentroVaccinale (nome, via ,nciv, citta, prov, CAP, tip, severita_media,n_segnalazioni);
                cvlis.add(cv);  
            }
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cvlis;
}


public static int registraVaccinato(Connection conn, Utente utente) throws SQLException {
	int id = -1;

	System.out.println("il centro vax ricevuto dal server : "+utente.getCentroVax());
	
	Boolean successo = true ;
	String query = "SELECT * FROM vaccinati_"+utente.getCentroVax()+" WHERE codfisc =?";
	PreparedStatement statement = conn.prepareStatement(query);		
	statement.setString(1, utente.getCodfisc());
	successo = checkUserData(conn, statement);

	if (successo == true) {
		System.out.println("Vaccinato già registrato");
		showMessageDialog(null,"Vaccinato già registrato");
		return id ;
	} 
	else {
	
	try {
		id = set_get_Id(conn);
		String state = "INSERT INTO vaccinati_"+utente.getCentroVax()+"(id_univoco,nome,cognome,codfisc,datavacc,vaccino,nomeCentro)"+ "VALUES (?,?,?,?,?,?,?)";
		statement = conn.prepareStatement(state);	
		statement.setInt(1, id);
		statement.setString(2, utente.getNome());
		statement.setString(3, utente.getCognome());
		statement.setString(4, utente.getCodfisc());
		statement.setDate(5, utente.getDatavacc());
		statement.setString(6, utente.getVacc());
		statement.setString(7, utente.getCentroVax());
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {	
		e.printStackTrace();
		return id;
	}
	return id;
	}
}

public static  boolean registraCentroVaccinale(Connection conn, CentroVaccinale cv) throws SQLException {
	Boolean successo = true ;
	String query = "SELECT * FROM centrivaccinali WHERE nome=? AND comune=?";
	PreparedStatement statement = conn.prepareStatement(query);		
	statement.setString(1, cv.getNome());
	statement.setString(2, cv.getComune());
	successo = checkUserData(conn, statement);

	if (successo == true) {
		System.out.println("Centro vaccinale già esistente");
		showMessageDialog(null,"Centro vaccinale già esistente");
		return false ;
	} 
	else {
	String create_table_query = "CREATE TABLE IF NOT EXISTS vaccinati_"+cv.getNome()+"(id_univoco int PRIMARY KEY , nome varchar(60), cognome varchar(60), codfisc varchar(16), "
				+ "datavacc date, vaccino varchar(20),nomeCentro varchar(60))";
	createTable(conn,create_table_query);
	String state = "INSERT INTO centrivaccinali (siglaprov,numciv,cap,comune,nome,indirizzo,tipologia)"+ "VALUES (?,?,?,?,?,?,?)";
		try {
			
			statement = conn.prepareStatement(state);	
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
}

public static  boolean registraCittadino(Connection conn, Utente user) throws SQLException   {
	//se un cittadino si è vaccinato in diversi centri vax verrà preso uno di questi come riferimento per la registrazione
	Boolean successo = false ;
	
	String query = "SELECT * FROM vaccinati_" + user.getCentroVax() +" WHERE codfisc = ? AND id_univoco = ?";
	PreparedStatement statement = conn.prepareStatement(query);		
	statement.setString(1, user.getCodfisc());
	statement.setInt(2, user.getIdvax());
	successo = checkUserData(conn, statement);
	
	
	if (successo == false) {
		System.out.println("Centro vaccinale o ID univoco non corrispondono");
		showMessageDialog(null,"Centro vaccinale o ID univoco non corrispondono");
		return false ;
	} 
	else {	
		String stmt = "INSERT INTO cittadini_registrati (nome,cognome,codfisc,email,userid,password,id,centroVax)"+ "VALUES (?,?,?,?,?,?,?,?)";
		try {
			statement = conn.prepareStatement(stmt);
			statement.setString(1, user.getNome());
			statement.setString(2, user.getCognome());
			statement.setString(3, user.getCodfisc());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getUserID());
			statement.setString(6, user.getPassword());
			statement.setInt(7, user.getIdvax());
			statement.setString(8, user.getCentroVax());
			statement.executeUpdate();
			System.out.println("Cittadino "+user.getNome()+" registrato correttamente");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return true;
}

public static String loginCittadino(Connection conn,HashMap <String, String> datiLogIn) {
	String query = "SELECT userid,password FROM cittadini_registrati WHERE userid=? AND password=?" ;
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, datiLogIn.get("ID"));
		statement.setString(2, datiLogIn.get("Password"));
		ResultSet rs = statement.executeQuery();
		if (rs.next() == false) { 
			return "false";
		} else {
			return "true";
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return "false";
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
		float sev_media_att = (float) (somma_ev_av/6.0);
		System.out.println("severita' media eventi avversi segnalati : "+sev_media_att);
		updateEventiAvversi(centroVax,sev_media_att);
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

public static void updateEventiAvversi(String centroVax, float sev_media_att) {
	 
	String query = "SELECT severita_media, n_segnalazioni FROM centrivaccinali WHERE nome=?" ;
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, centroVax);
		ResultSet rs = statement.executeQuery();
		rs.next();
		float sevMediaPrec = rs.getInt(1);
		int nEvAvPrec = rs.getInt(2);
		int nEvAv = nEvAvPrec+1;
		float sev_media_tot;
		if((sevMediaPrec<0)) {
			sev_media_tot = sev_media_att;
		} else sev_media_tot = (sev_media_att+(sevMediaPrec*nEvAvPrec))/nEvAv;
		
		statement = conn.prepareStatement("UPDATE centrivaccinali SET severita_media = ?, n_segnalazioni = ? WHERE nome = ?;");
		statement.setFloat(1, sev_media_tot);
		statement.setInt(2, nEvAv);
		statement.setString(3, centroVax);
		statement.executeUpdate();
		statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}	
}

public static boolean checkUserData(Connection conn, PreparedStatement statement) {
	 try {
			ResultSet rs = statement.executeQuery();
			if (rs.next() == true) { 
				return true;
			}
			} catch (SQLException a) {
				//rimosso print errore
			}
	 return false ;
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

}