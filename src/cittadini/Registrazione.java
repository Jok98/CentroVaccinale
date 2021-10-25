package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Registrazione {

	public static JFrame frmRegistrazionePressoCentro;
	public static Registrazione window = new Registrazione();
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
					window.frmRegistrazionePressoCentro.setVisible(true);
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
		frmRegistrazionePressoCentro = new JFrame();
		frmRegistrazionePressoCentro.setTitle("Registrazione presso centro vaccinale");
		frmRegistrazionePressoCentro.setBounds(100, 100, 450, 321);
		frmRegistrazionePressoCentro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegistrazionePressoCentro.getContentPane().setLayout(null);
		
		JLabel lblNome = new JLabel("Nome :");
		lblNome.setBounds(10, 11, 112, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblNome);
		
		JLabel lblCognome = new JLabel("Cognome :");
		lblCognome.setBounds(10, 42, 112, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblCognome);
		
		tfNome = new JTextField();
		tfNome.setBounds(174, 8, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfNome);
		tfNome.setColumns(10);
		
		tfCognome = new JTextField();
		tfCognome.setBounds(174, 39, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfCognome);
		tfCognome.setColumns(10);
		
		tfCodiceFiscale = new JTextField();
		tfCodiceFiscale.setBounds(174, 70, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfCodiceFiscale);
		tfCodiceFiscale.setColumns(10);
		
		JLabel lblCodicefiscale = new JLabel("Codice Fiscale :");
		lblCodicefiscale.setBounds(10, 73, 112, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblCodicefiscale);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(174, 101, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfEmail);
		tfEmail.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setBounds(10, 104, 112, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblEmail);
		
		tfUserID = new JTextField();
		tfUserID.setBounds(174, 132, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfUserID);
		tfUserID.setColumns(10);
		
		JLabel lblUserID = new JLabel("User ID");
		lblUserID.setBounds(10, 135, 46, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblUserID);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(10, 166, 112, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(174, 163, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(passwordField);
		
		JLabel lblIDUnicoVax = new JLabel("Id univoco vaccinazione :");
		lblIDUnicoVax.setBounds(10, 197, 150, 14);
		frmRegistrazionePressoCentro.getContentPane().add(lblIDUnicoVax);
		
		tfIDUnivocoVax = new JTextField();
		tfIDUnivocoVax.setBounds(174, 194, 250, 20);
		frmRegistrazionePressoCentro.getContentPane().add(tfIDUnivocoVax);
		tfIDUnivocoVax.setColumns(10);
		
		JButton btnRegistra = new JButton("Registrati");
		btnRegistra.setBounds(174, 248, 89, 23);
		frmRegistrazionePressoCentro.getContentPane().add(btnRegistra);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnBack.setBounds(10, 248, 89, 23);
		frmRegistrazionePressoCentro.getContentPane().add(btnBack);
		
		
	}
}
