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
	
	private static String url = "jdbc:postgresql://127.0.0.1/laboratorio";
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
	
	/**
	 * Essendo il server multithreading, questo è il singolo thread, il quale compito è di comunicare con uno specifico client e gestire le sue richieste 
	 *
	 * @param s Oggetto di tipo Socket necessario per la comunicazione server-client 
	 * @throws IOException nel caso di operazioni I/O fallite o corrotte
	 */
	ServerThread (Socket s){
		socket = s;
		try {
			oin = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) { e.printStackTrace(); }
		start();
	}	

	/**
	 * Questo è il main della classe, dove ci si connette al db e si creano le tabelle fondamentali per il coretto funzionamento del programma 
	 * @param args
	 */
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
	    	 if(first_AD==true)showMessageDialog(null,"Server started");
	    	 first_AD=false;
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
	/**
	 * Questo metodo viene usato per connettere il server al db fornendo le credenziali di accesso 
	 * @return conn Oggetto di tipo Connection comunicante con uno specifico DB
	 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
	 * 
	 */
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


	  

/**
 * riceve un'istanza della ConnessioneServer la quale contiene sia la stringa indicante la 
 * richista dal client,  sia i dati necessari all'elaborazione della richiesta sottoforma di oggetto
 * conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @throws IOException nel caso di operazioni I/O fallite o corrotte
 * @throws ClassNotFoundException nel caso in cui si cerchi di caricare una classe non trovata
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 * 
 */
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

/**
 * Questo metodo serve per restituire e aggionrare l'ID univoco estratto dal DB  
 * 
 * 
 * 
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @return int Valore corrisondente all'id  
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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


/**
 * Questo metodo serve per restituire il nome del centro vaccinale nel quale si è vaccinato un particolare user
 * 
 * 
 *  
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param idUser Valore ID univoco dello user associato a un particolare centro vaccinale 
 * @return centroVax Stringa corrispondente al nome del centro vaccinale
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB 
 */ 


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


/**
 * Questo metodo serve a ricercare uno o più centri vaccinali, sia per comune e tipologia, sia per nome del centro vaccinale 
 * 
 * 
 * 
 * @param statement oggetto di tipo PreparedStatement contenente la query da eseguire
 * @return ArrayList di tipo CentroVaccinale contenente tutti i Centri Vaccinali risultanti dalla query
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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

/**
 * Questo metodo serve a registrare un cittadino appena vaccinato in un centro vaccinale, metodo usato dagli operatori dei centri vaccinali
 *  
 *
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param utente Oggetto di tipo Utente contenente informazioni sul cittadino
 * @return int Oggetto di tipo int corrispondente all'id del nuovo vaccinato 
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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

/**
 * Questo metodo serve ad inserire un nuovo centro vaccinale nel DB
 * 
 * 
 * 
 * @param conn  Oggetto di tipo Connection comunicante con uno specifico DB
 * @param cv Oggeto di tipo CentroVaccinale corrispondente al centro vaccinale da inserire nel DB
 * @return successo Oggetto di tipo Boolean notificante il successo o insuccesso dell'operazione
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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

/**
 * Questo metodo serve ad inserire nel db un nuovo cittadino, operazione preliminare necessaria per il futuro login del cittadino  
 * 
 *  
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param user Oggetto di tipo Utente contenente informazioni sul cittadino
 * @return Boolean valore primitivo corrispondente al successo o insuccesso dell'operazione
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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

/**
 * Questo metodo serve al cittadino per eseguire il login, operazione preliminare necessaria per usufruire delle funzioni riservate ai cittadini registrati
 * 
 * 
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param datiLogIn HashMap di tipo <Stringa,Stringa> contenente informazioni quali userid e password del cittadino 
 * @return String equivalente a "true" o "false" in caso di successo o insuccesso 
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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

/**
 * Questo metodo viene usato dal cittadino che ha eseguito il login per comunicare e inserire nel DB eventuali 
 * problemi di salute in seguito alla somministrazione del vaccino
 * 
 * 
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param Eventi_Avversi ArrayList di tipo Object contenente l'intensità dei problemi di salute ed eventuali note opzionali
 * @return Boolean valore primitivo corrispondente al successo o insuccesso dell'operazione
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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
				somma_ev_av = (int) Eventi_Avversi.get(i)+somma_ev_av;  //calcola somma severità degli eventi avversi 
				statement.setInt(i, (int) Eventi_Avversi.get(i) );		
				
			}else statement.setString(i, (String) Eventi_Avversi.get(i));
		}	
		float sev_media_att = (float) (somma_ev_av/6.0);				//calcola severità media degli eventio avversi comunicati dal singolo utente 
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

/**
 * Questo metodo, chiamato a sua volta in inserisciEventiAvversi(), serve a calcolare e aggiornare la media delle severità ed il numero degli 
 * eventi avversi relativi ad un centro vaccinale  
 * 
 * @param centroVax Stringa corrispondente al nome del centro vaccinale da aggiornare
 * @param sev_media_att Float corrispondente alla media delle severità prima dei nuovi inserimenti 
 * @return void 
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

public static void updateEventiAvversi(String centroVax, float sev_media_att) {
	 
	String query = "SELECT severita_media, n_segnalazioni FROM centrivaccinali WHERE nome=?" ;
	try {
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, centroVax);
		ResultSet rs = statement.executeQuery();
		rs.next();
		float sevMediaPrec = rs.getInt(1);
		int nEvAvPrec = rs.getInt(2);
		int nEvAv = nEvAvPrec+1;  		//aggiorna il numero di eventi avversi 
		float sev_media_tot;
		
		/*per calcolare la nuova media della severità globale, prima si moltiplica la media precedente per il numero 
		di segnalazioni precedenti  (ricavando la somma totale delle severità precedenti), che verrà poi sommata a
		quelli della singola segnalazione del cittadin e infine diviso per il numero di segnalazioni aggiornate */
		
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

/**
 * Questo metodo viene usato per verificare che una certa istanza esista o meno del DB 
 * 
 * 
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param statement oggetto di tipo PreparedStatement contenente la query da eseguire 
 * @return boolean valore primitivo di tipo Boolean, vero se l'istanza è gia presente nel DB, falso se non è già presente 
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 */

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



/**
 * Questo metodo viene usato per la creazione dinamica di una tabella nel caso non esista già
 * 
 * @param conn Oggetto di tipo Connection comunicante con uno specifico DB
 * @param create_table_query Stringa contenente il comando SQL da eseguire per la creazione della query
 * @return boolean valore primitivo Boolean corrispondente al successo o insuccesso della creazione della tabella 
 * @throws SQLException nel caso di errati comandi sql o mancata comunicazione col DB
 *  
 */
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