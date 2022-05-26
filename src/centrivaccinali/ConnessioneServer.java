package centrivaccinali;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import cittadini.CittadiniForm;


public class ConnessioneServer implements Serializable {
	
	public static final long serialVersionUID = 192873466528L;
	public static Socket socket = CentriVaccinali.socket;
	public static ObjectInputStream oin;
	public static ObjectOutputStream oout;
	String richiesta;
	public Object obj;


	
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
	
	public  Object setObj(Object obj) {
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
	 * 
	 */
	public static void ricezioneServer() {
		
		try {
			ConnessioneServer return_cs =  (ConnessioneServer) oin.readObject();
			System.out.println("La risposta del server per la richiesta di  :"+return_cs.getRichiesta());
	    	
	    	switch(return_cs.getRichiesta()) {
	    	case "LogIn" :
	    		HashMap<String, String> datiLogIn = (HashMap<String, String>) return_cs.getObj();
	    		CittadiniForm.LogIn_Result(datiLogIn.get("logInResult"),datiLogIn.get("centroVax"));
	    		System.out.println("Risultato log in : "+datiLogIn.get("logInResult"));
	    		datiLogIn.clear();
	    	break;	
	    	case "IdUnivoco" :
	    		IscrizioneVaccinato.IdUnivoco = (int) return_cs.getObj();
	    		System.out.println("id univoco del vaccinato : "+(int) return_cs.getObj());
	    	break;	
	    	}
	    	socket.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
    	
	}
	

	//@SuppressWarnings("unchecked")
	public ArrayList<CentroVaccinale> cercaCentroVaccinale(ConnessioneServer cs) {
		ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
		try {
			oout.writeObject(cs);
			
			cvlis = (ArrayList<CentroVaccinale>) oin.readObject();
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		//System.out.println("il client ha ricevuto dal server il centro vax : "+cvlis.get(0).getNome()+" "+cvlis.size());
		
		return cvlis;
	}
}



