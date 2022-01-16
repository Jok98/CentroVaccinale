package centrivaccinali;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

import cittadini.CittadiniForm;
import cittadini.Utente;


public class ConnessioneServer implements Serializable {
	
	public static final long serialVersionUID = 192873466528L;
	public static Socket socket = CentriVaccinali.socket;
	public static ObjectInputStream oin;
	public static ObjectOutputStream oout;
	String richiesta;
	public Object obj;

	public ConnessioneServer(String richiesta, Object obj) throws IOException {
		
		oout = new ObjectOutputStream(socket.getOutputStream());
		oin = new ObjectInputStream(socket.getInputStream());
		this.richiesta = richiesta;
		this.obj =obj;
	}
	
public ConnessioneServer(Socket socket, String richiesta, Object obj) throws IOException {
		this.socket = socket;
		oout = new ObjectOutputStream(socket.getOutputStream());
		oin = new ObjectInputStream(socket.getInputStream());
		this.richiesta = richiesta;
		this.obj =obj;
	}

	public String getRichiesta() {
		return richiesta;
	}
	
	public Object getObj() {
		return obj;
	}
	
	public String setRichiesta(String richiesta) {
		return this.richiesta =richiesta;
	}
	
	public Object setObj(Object obj) {
		return this.obj=obj;
	}
	
	public static boolean richiestaServer(ConnessioneServer cs) throws ClassNotFoundException, IOException {
		
		try {
			
			oout.writeObject(cs);
			ricezioneServer();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	
		
		return true;
	}
	/**
	 * TODO vedere se è problematico un solo login, se si torna indietro non si può più accedere
	 */
	public static void ricezioneServer() {
		
		try {
			ConnessioneServer return_cs =  (ConnessioneServer) oin.readObject();
			System.out.println("La risposta del server per la richiesta di  :"+return_cs.getRichiesta());
			
	    	
	    	switch(return_cs.getRichiesta()) {
	    	case "LogIn" :
	    		Boolean logIn_result = (Boolean) return_cs.getObj();
	    		CittadiniForm.LogIn_Result((Boolean) return_cs.getObj());
	    	}
	    	
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
	}
	
	@SuppressWarnings("unchecked")
	public static boolean cercaCentroVaccinale(ConnessioneServer cs) {

		try {
			oout.writeObject(cs);
			ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
			cvlis = (ArrayList<CentroVaccinale>) oin.readObject();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	
		return true;
	}
}



