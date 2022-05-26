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

public class CentriVaccinali {
	static CittadiniForm CF = new CittadiniForm();
	static OperatoriForm OF = new OperatoriForm();
	static CentriVaccinali window = new CentriVaccinali();
	public static JFrame frmProgettoCentriVaccinali;

	public static Socket socket;
	public static ObjectInputStream ins;
	public static ObjectOutputStream outs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmProgettoCentriVaccinali.setVisible(true);

					//openSocket();


				} catch (Exception e) {
					e.printStackTrace();
				}



			}
		});
	}

	/**
	 * Create the application.
	 */
	public CentriVaccinali() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProgettoCentriVaccinali = new JFrame();
		frmProgettoCentriVaccinali.setTitle("Progetto Centri Vaccinali");
		frmProgettoCentriVaccinali.setBounds(100, 100, 450, 300);
		frmProgettoCentriVaccinali.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProgettoCentriVaccinali.getContentPane().setLayout(null);

		JButton btnCittadini = new JButton("Cittadino");
		btnCittadini.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frmProgettoCentriVaccinali.setVisible(false);
				CittadiniForm.frmCittadini.setVisible(true);
			}
		});
		btnCittadini.setBounds(317, 97, 107, 59);
		frmProgettoCentriVaccinali.getContentPane().add(btnCittadini);

		JButton btnOperatore = new JButton("Operatore");
		btnOperatore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmProgettoCentriVaccinali.setVisible(false);
				OperatoriForm.frmAppOperatori.setVisible(true);
			}
		});
		btnOperatore.setBounds(10, 97, 107, 59);
		frmProgettoCentriVaccinali.getContentPane().add(btnOperatore);
	}

	public static Socket openSocket() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(null);
			socket = new Socket(addr, 8083);
			System.out.println(socket.getLocalSocketAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;


	}


}