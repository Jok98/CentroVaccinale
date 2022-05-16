package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

import centrivaccinali.CentriVaccinali;
import centrivaccinali.CentroVaccinale;
import centrivaccinali.ConnessioneServer;

import java.awt.TextField;
import java.awt.Label;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
	private ArrayList<CentroVaccinale> src_result;
	static String nome_Centro_Vax;
	private JTextField tf_NomeCentroVax;
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
				if(tf_NomeCentroVax.getText().isEmpty()) {
					showMessageDialog(null,"Selezionare il centro in cui si è stati vaccinati");
				}else {
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
			}
		});
		btnLogIn.setBounds(460, 423, 89, 23);
		frmCittadini.getContentPane().add(btnLogIn);
		
		JButton btnRegistrazione = new JButton("Registrati");
		btnRegistrazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tf_NomeCentroVax.getText().isEmpty()) {
					showMessageDialog(null,"Selezionare il centro in cui si è stati vaccinati");
				}else {
				Registrazione.frame.setVisible(true);
				Registrazione.tf_NomeCentroVax.setText(nome_Centro_Vax);
				frmCittadini.setVisible(false);
				}
			}
		});
		btnRegistrazione.setBounds(109, 440, 89, 23);
		frmCittadini.getContentPane().add(btnRegistrazione);
		
	
		JList listCentriVax = new JList();
		listCentriVax.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listCentriVax.setBackground(Color.WHITE);
		listCentriVax.setBounds(10, 139, 539, 206);
		frmCittadini.getContentPane().add(listCentriVax);
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
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"", "Ospedaliero", "Aziendale", "Hub"}));
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
					
						src_result =cs.cercaCentroVaccinale(cs);
						String[] centroVax_nome = new String[src_result.size()]  ;
						
						for(int i=0; i<src_result.size();i++) {
							centroVax_nome[i]=src_result.get(i).getNome();
						}
						
						listCentriVax.setModel(new AbstractListModel() {
							String[] values = centroVax_nome ;
							public int getSize() {
								return values.length;
							}
							public Object getElementAt(int index) {
								return values[index];
							}
						});
					} catch (IOException e1) {
					
						e1.printStackTrace();
					}
					
				}
				
				if(tfRicercaNome.getText().isEmpty()&!tfRicercaComune.getText().isEmpty()&comboBox.getSelectedIndex()>=1) {
					try {
						String[] dati_ricerca = {tfRicercaComune.getText(),(String) comboBox.getSelectedItem()};
						Socket socket = CentriVaccinali.openSocket();
						
						ConnessioneServer cs = new ConnessioneServer(socket,"ricercaCVComuneTipologia", dati_ricerca);
					
						src_result =cs.cercaCentroVaccinale(cs);
						String[] centroVax_nome = new String[src_result.size()]  ;
						
						for(int i=0; i<src_result.size();i++) {
							centroVax_nome[i]=src_result.get(i).getNome();
						}
						
						listCentriVax.setModel(new AbstractListModel() {
							String[] values = centroVax_nome ;
							public int getSize() {
								return values.length;
							}
							public Object getElementAt(int index) {
								return values[index];
							}
						});
					} catch (IOException e1) {
						
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
		btnShowResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nome_Centro_Vax = src_result.get(listCentriVax.getSelectedIndex()).getNome();
				tf_NomeCentroVax.setText(nome_Centro_Vax);
				showMessageDialog(null, "Informazione del Centro vaccinale : \r\n"+src_result.get(listCentriVax.getSelectedIndex()).getInfo());
			}
		});
		btnShowResult.setBounds(439, 356, 110, 23);
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
		
		JLabel lbl_CentroVax = new JLabel("Centro vaccinale selezionato : ");
		lbl_CentroVax.setBounds(10, 360, 188, 14);
		frmCittadini.getContentPane().add(lbl_CentroVax);
		
		tf_NomeCentroVax = new JTextField();
		tf_NomeCentroVax.setBounds(208, 356, 226, 20);
		frmCittadini.getContentPane().add(tf_NomeCentroVax);
		tf_NomeCentroVax.setColumns(10);
	}
	
	public static void LogIn_Result(Boolean result) {
		//logIn_status=result;
		if(result==true) {
			AccessConf.frmInvioDatiEventi.setVisible(true);
			frmCittadini.setVisible(false);
			AccessConf.tfCentroVax.setText(nome_Centro_Vax);
		}else {
			showMessageDialog(null,"Dati logIn errati");
		}
	}
	
	
}
