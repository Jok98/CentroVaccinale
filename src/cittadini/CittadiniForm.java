package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

import centrivaccinali.CentriVaccinali;
import centrivaccinali.ConnessioneServer;

import java.awt.TextField;
import java.awt.Label;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class CittadiniForm {

	public static JFrame frmCittadini;
	static Registrazione registrazione = new Registrazione();
	static AccessoAutenticato AccessConf = new AccessoAutenticato();
	static CentriVaccinali centrivax = new CentriVaccinali();
	private JTextField tfID;
	private JPasswordField passwordField;
	private Boolean logIn_status = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					CittadiniForm window = new CittadiniForm();
					CittadiniForm.frmCittadini.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CittadiniForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	@SuppressWarnings("unchecked")
	private void initialize() {
		frmCittadini = new JFrame();
		frmCittadini.setTitle("App Cittadini - Centri Vaccinali");
		frmCittadini.setBounds(100, 100, 575, 513);
		frmCittadini.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCittadini.getContentPane().setLayout(null);
		
		JButton btnLogIn = new JButton("LogIn");
		btnLogIn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				HashMap <String, String> datiLogIn = new HashMap <String, String>();
				datiLogIn.put("ID", tfID.getText());	
				datiLogIn.put("Password", String.valueOf(passwordField.getPassword()));
				
				//System.out.println(datiLogIn.get("Password"));
				try {
					Socket socket = CentriVaccinali.openSocket();
					ConnessioneServer cs = new ConnessioneServer(socket,"LogIn", datiLogIn);
					System.out.println(ConnessioneServer.richiestaServer(cs));
				} catch (IOException | ClassNotFoundException e1) {
					
					e1.printStackTrace();
				}
				//AccessConf.frmInvioDatiEventi.setVisible(true);
				//frmCittadini.setVisible(false);
			}
		});
		btnLogIn.setBounds(460, 423, 89, 23);
		frmCittadini.getContentPane().add(btnLogIn);
		
		JButton btnRegistrazione = new JButton("Registrati");
		btnRegistrazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registrazione.frmRegistrazionePressoCentro.setVisible(true);
				frmCittadini.setVisible(false);
			}
		});
		btnRegistrazione.setBounds(109, 440, 89, 23);
		frmCittadini.getContentPane().add(btnRegistrazione);
		
	
		JList list_result = new JList();
		list_result.setModel(new AbstractListModel() {
			String[] values = new String[] {"1", "23", "3", "5", "1", "8", "418", "148"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
			
		});
		list_result.setBackground(Color.WHITE);
		list_result.setBounds(10, 139, 539, 206);
		frmCittadini.getContentPane().add(list_result);
		/*JScrollPane scroll = new JScrollPane (list_result);
		scroll.setBounds(10, 52, 414, 142);
		frmCittadini.getContentPane().add(scroll);*/
		
		TextField tfRicercaNome = new TextField();
		tfRicercaNome.setBounds(85, 38, 349, 22);
		frmCittadini.getContentPane().add(tfRicercaNome);
		
		Label lblRicerca = new Label("Ricerca centro vaccinale per : ");
		lblRicerca.setBounds(10, 10, 177, 22);
		frmCittadini.getContentPane().add(lblRicerca);
		
		TextField tfRicercaComune = new TextField();
		tfRicercaComune.setBounds(85, 66, 349, 22);
		frmCittadini.getContentPane().add(tfRicercaComune);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"", "Ospedale"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(195, 94, 239, 22);
		frmCittadini.getContentPane().add(comboBox);
		
		Button btnCerca = new Button("Cerca");
		btnCerca.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				if(!tfRicercaNome.getText().isEmpty()&tfRicercaComune.getText().isEmpty()&comboBox.getSelectedIndex()==0) {
					try {
						Socket socket = CentriVaccinali.openSocket();
						ConnessioneServer cs = new ConnessioneServer(socket,"srcCentroVax", tfRicercaNome.getText());
						System.out.println(ConnessioneServer.richiestaServer(cs));
					} catch (IOException | ClassNotFoundException e1) {
					
						e1.printStackTrace();
					}
					
				}
				
				if(tfRicercaNome.getText().isEmpty()&!tfRicercaComune.getText().isEmpty()&comboBox.getSelectedIndex()>=1) {
					try {
						String[] dati_ricerca = {tfRicercaComune.getText(),(String) comboBox.getSelectedItem()};
						Socket socket = CentriVaccinali.openSocket();
						ConnessioneServer cs = new ConnessioneServer(socket,"ricercaCVComuneTipologia", dati_ricerca);
						System.out.println(ConnessioneServer.richiestaServer(cs));
					} catch (IOException | ClassNotFoundException e1) {
						
						e1.printStackTrace();
					}
				}
			}
		});
		btnCerca.setBounds(460, 40, 89, 76);
		frmCittadini.getContentPane().add(btnCerca);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				centrivax.frmProgettoCentriVaccinali.setVisible(true);
				frmCittadini.dispose();
			}
		});
		btnBack.setBounds(10, 440, 89, 23);
		frmCittadini.getContentPane().add(btnBack);
		
		JButton btnShowResult = new JButton("Mostra dati");
		btnShowResult.setBounds(195, 355, 110, 23);
		frmCittadini.getContentPane().add(btnShowResult);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 389, 569, 14);
		frmCittadini.getContentPane().add(separator);
		
		JLabel lblNome = new JLabel("Nome :");
		lblNome.setBounds(10, 46, 69, 14);
		frmCittadini.getContentPane().add(lblNome);
		
		JLabel lblComune = new JLabel("Comune :");
		lblComune.setBounds(10, 71, 69, 14);
		frmCittadini.getContentPane().add(lblComune);
		
		JLabel lblTipologia = new JLabel("Tipologia :");
		lblTipologia.setBounds(10, 96, 147, 14);
		frmCittadini.getContentPane().add(lblTipologia);
		
		JLabel lblID = new JLabel("ID :");
		lblID.setBounds(297, 414, 41, 14);
		frmCittadini.getContentPane().add(lblID);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(262, 444, 76, 14);
		frmCittadini.getContentPane().add(lblPassword);
		
		tfID = new JTextField();
		tfID.setBounds(348, 411, 86, 20);
		frmCittadini.getContentPane().add(tfID);
		tfID.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(348, 441, 86, 20);
		frmCittadini.getContentPane().add(passwordField);
	}
	
	public static void LogIn_Result(Boolean result) {
		//logIn_status=result;
		if(result==true) {
			AccessConf.frmInvioDatiEventi.setVisible(true);
			frmCittadini.setVisible(false);
		}else {
			System.out.println("Dati logIn errati");
		}
	}
	
	
}
