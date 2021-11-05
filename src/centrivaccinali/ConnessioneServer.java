package centrivaccinali;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import cittadini.Utente;
//import server.ServerCV;

public class ConnessioneServer {
	
	public Socket socket;
	public static ObjectInputStream ins;
	public static ObjectOutputStream outs;


	public ConnessioneServer(Socket socket) throws IOException {
		this.socket = socket;
		outs = new ObjectOutputStream(socket.getOutputStream());
		ins = new ObjectInputStream(socket.getInputStream());
		
	}
	
	public static boolean registraCentroVaccinaleinale(CentroVaccinale cv) {
		
		try {
			outs.writeObject(cv);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean registraVaccinato(Utente user) {
		
		try {
			outs.writeObject(user);
			System.out.println(user.getNome());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static boolean cercaCentroVaccinaleinale(String srcCentroVax) {

		try {
			outs.writeObject(srcCentroVax);
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



