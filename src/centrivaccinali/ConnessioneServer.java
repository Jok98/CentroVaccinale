package centrivaccinali;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import cittadini.Utente;


public class ConnessioneServer implements Serializable {
	
	public static final long serialVersionUID = 192873466528L;
	public static Socket socket = CentriVaccinali.socket;
	public static ObjectInputStream ins;
	public static ObjectOutputStream outs;
	String richiesta;
	Object obj;

	public ConnessioneServer(String richiesta, Object obj) throws IOException {
		outs = new ObjectOutputStream(socket.getOutputStream());
		ins = new ObjectInputStream(socket.getInputStream());
		this.richiesta = richiesta;
		this.obj =obj;
		
		
	}

	
	public String getRichiesta() {
		return richiesta;
	}
	
	public Object getObj() {
		return obj;
	}
	
	public static boolean registraCentroVaccinale(ConnessioneServer cs) {
		
		try {
			
			outs.writeObject(cs);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean registraVaccinato(ConnessioneServer cs) {
		
		try {
			outs.writeObject(cs);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static boolean cercaCentroVaccinale(ConnessioneServer cs) {

		try {
			outs.writeObject(cs);
			ArrayList<CentroVaccinale> cvlis = new ArrayList<CentroVaccinale>();
			cvlis = (ArrayList<CentroVaccinale>) ins.readObject();
			/*for (CentroVaccinale c : cvlis) {
				c.getInfo();
			}*/
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	
		return true;
	}
}



