package cittadini;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import centrivaccinali.CentriVaccinali;
import centrivaccinali.ConnessioneServer;

public class Registrazione {

	static JFrame frame;
	private JTextField tfNome;
	private JTextField tfCognome;
	private JTextField tfCodiceFiscale;
	private JTextField tfEmail;
	private JTextField tfUserID;
	private JPasswordField passwordField;
	private JTextField tfIDUnivocoVax;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registrazione window = new Registrazione();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Registrazione() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 326);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		

		
		JLabel lblNome = new JLabel("Nome :");
		lblNome.setBounds(10, 11, 112, 14);
		frame.getContentPane().add(lblNome);
		
		JLabel lblCognome = new JLabel("Cognome :");
		lblCognome.setBounds(10, 42, 112, 14);
		frame.getContentPane().add(lblCognome);
		
		tfNome = new JTextField();
		tfNome.setBounds(174, 8, 250, 20);
		frame.getContentPane().add(tfNome);
		tfNome.setColumns(10);
		
		tfCognome = new JTextField();
		tfCognome.setBounds(174, 39, 250, 20);
		frame.getContentPane().add(tfCognome);
		tfCognome.setColumns(10);
		
		tfCodiceFiscale = new JTextField();
		tfCodiceFiscale.setBounds(174, 70, 250, 20);
		frame.getContentPane().add(tfCodiceFiscale);
		tfCodiceFiscale.setColumns(10);
		
		JLabel lblCodicefiscale = new JLabel("Codice Fiscale :");
		lblCodicefiscale.setBounds(10, 73, 112, 14);
		frame.getContentPane().add(lblCodicefiscale);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(174, 101, 250, 20);
		frame.getContentPane().add(tfEmail);
		tfEmail.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setBounds(10, 104, 112, 14);
		frame.getContentPane().add(lblEmail);
		
		tfUserID = new JTextField();
		tfUserID.setBounds(174, 132, 250, 20);
		frame.getContentPane().add(tfUserID);
		tfUserID.setColumns(10);
		
		JLabel lblUserID = new JLabel("User ID");
		lblUserID.setBounds(10, 135, 46, 14);
		frame.getContentPane().add(lblUserID);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(10, 166, 112, 14);
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(174, 163, 250, 20);
		frame.getContentPane().add(passwordField);
		
		JLabel lblIDUnicoVax = new JLabel("Id univoco vaccinazione :");
		lblIDUnicoVax.setBounds(10, 197, 150, 14);
		frame.getContentPane().add(lblIDUnicoVax);
		
		tfIDUnivocoVax = new JTextField();
		tfIDUnivocoVax.setBounds(174, 194, 250, 20);
		frame.getContentPane().add(tfIDUnivocoVax);
		tfIDUnivocoVax.setColumns(10);
		
		JButton btnRegistra = new JButton("Registrati");
		btnRegistra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String password = String.valueOf(passwordField.getPassword());
				Utente user = new Utente(tfNome.getText(), tfCognome.getText(), tfCodiceFiscale.getText(), tfEmail.getText(),
						tfUserID.getText(), password, Integer.valueOf(tfIDUnivocoVax.getText()));
				try {
					//System.out.println("centyro vax rilevato dal client "+tfCentroVaxSelezionato.getText());
					Socket socket = CentriVaccinali.openSocket();
					ConnessioneServer cs = new ConnessioneServer(socket,"registrazioneCittadino", user);
					
					System.out.println(ConnessioneServer.richiestaServer(cs));
				} catch (IOException | ClassNotFoundException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		btnRegistra.setBounds(174, 248, 89, 23);
		frame.getContentPane().add(btnRegistra);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CittadiniForm.frmCittadini.setVisible(true);
				frame.dispose();
			}
		});
		btnBack.setBounds(10, 248, 89, 23);
		frame.getContentPane().add(btnBack);
	}

}
