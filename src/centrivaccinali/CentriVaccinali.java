package centrivaccinali;

import java.awt.EventQueue;

import javax.swing.JFrame;

import cittadini.*;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
/**
 * 
 * @author Moi Matteo/Alex Rabuffetti
 * 
 */
public class CentriVaccinali {
	static CittadiniForm CF = new CittadiniForm();
	static OperatoriForm OF = new OperatoriForm();
	static CentriVaccinali window;
	public static JFrame frmProgettoCentriVaccinali;

	public static Socket socket;
	public static ObjectInputStream ins;
	public static ObjectOutputStream outs;

	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new CentriVaccinali();
					frmProgettoCentriVaccinali.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}



			}
		});
	}

	/**
	 * Viene creata l'applicazione
	 */
	public CentriVaccinali() {
		initialize();
	}
	
	/**
	 * Inizializza il contenuto del frame
	 */
	private void initialize() {
		frmProgettoCentriVaccinali = new JFrame();
		frmProgettoCentriVaccinali.setTitle("Progetto Centri Vaccinali");
		frmProgettoCentriVaccinali.setBounds(100, 100, 450, 300);
		frmProgettoCentriVaccinali.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProgettoCentriVaccinali.getContentPane().setLayout(null);
		
		//Start button Cittadino
		JButton btnCittadini = new JButton("Cittadino");
		btnCittadini.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frmProgettoCentriVaccinali.setVisible(false);
				CittadiniForm.frmCittadini.setVisible(true);
			}
		});
		btnCittadini.setBounds(317, 97, 107, 59);
		frmProgettoCentriVaccinali.getContentPane().add(btnCittadini);
		//End button Cittadino
		
		//Start button Operatore
		JButton btnOperatore = new JButton("Operatore");
		btnOperatore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmProgettoCentriVaccinali.setVisible(false);
				OperatoriForm.frmAppOperatori.setVisible(true);
			}
		});
		btnOperatore.setBounds(10, 97, 107, 59);
		frmProgettoCentriVaccinali.getContentPane().add(btnOperatore);
		//End button Operatore
	}

	/**
	 * 
	 * @return socket ritorna un socket da utilizzare per la creazione 
	 *  di una nuova istanza della classe Connessione Server
	 *  
	 */
	public static Socket openSocket() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(null);
			socket = new Socket(addr, 8083);
			System.out.println(socket.getLocalSocketAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;


	}


}