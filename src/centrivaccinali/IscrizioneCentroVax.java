package centrivaccinali;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cittadini.Utente;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.awt.event.ActionEvent;


/**
 * 
 * @author Moi Matteo/Alex Rabuffetti
 *
 */
public class IscrizioneCentroVax extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public CentroVaccinale CV;
	private JPanel contentPane;
	private static JTextField tfNomeCentroVax;
	private static JTextField tfIndirizzoCentroVax;
	private static JTextField tfNCivico;
	private static JTextField tfComune;
	private static JTextField tfSiglaProvincia;
	private static JTextField tfCAP;
	
	ConnessioneServer cs ;
	
	public static IscrizioneCentroVax frame= new IscrizioneCentroVax();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IscrizioneCentroVax() {
		setTitle("Registrazione Centro Vaccinale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//Start label lblNomeCentroVax
		JLabel lblNomeCentroVax = new JLabel("Nome centro vaccinale : ");
		lblNomeCentroVax.setBackground(UIManager.getColor("Button.highlight"));
		lblNomeCentroVax.setBounds(10, 28, 147, 14);
		contentPane.add(lblNomeCentroVax);
		//End label lblNomeCentroVax
		
		//Start textfield tfNomeCentroVax
		tfNomeCentroVax = new JTextField();
		tfNomeCentroVax.setBounds(164, 25, 260, 20);
		contentPane.add(tfNomeCentroVax);
		tfNomeCentroVax.setColumns(10);
		//End textfield tfNomeCentroVax
		
		
		//Start label lblVia
		JLabel lblVia = new JLabel("Via : ");
		lblVia.setBounds(10, 56, 89, 14);
		contentPane.add(lblVia);
		//End label lblVia
		
		//Start textfield tfIndirizzoCentroVax
		tfIndirizzoCentroVax = new JTextField();
		tfIndirizzoCentroVax.setBounds(164, 53, 260, 20);
		contentPane.add(tfIndirizzoCentroVax);
		tfIndirizzoCentroVax.setColumns(10);
		//End textfield tfIndirizzoCentroVax
		
		
		//Start label lblTipologia
		JLabel lblTipologia = new JLabel("Tipologia :");
		lblTipologia.setBounds(10, 212, 147, 14);
		contentPane.add(lblTipologia);
		//End label lblTipologia
		
		//Start comboBox per la selezione della tipologia di centro vaccinale
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ospedaliero", "Aziendale", "Hub"}));
		comboBox.setBounds(164, 208, 260, 22);
		contentPane.add(comboBox);
		//End comboBox
		
		//Start label lblNCivico
		JLabel lblNCivico = new JLabel("Numero civico :");
		lblNCivico.setBounds(10, 87, 147, 14);
		contentPane.add(lblNCivico);
		//End label lblNCivico
		
		//Start textfield tfNCivico
		tfNCivico = new JTextField();
		tfNCivico.setBounds(164, 84, 260, 20);
		contentPane.add(tfNCivico);
		tfNCivico.setColumns(10);
		//End textfield tfNCivico
		
		
		//Start label lblComune
		JLabel lblComune = new JLabel("Comune : ");
		lblComune.setBounds(10, 118, 147, 14);
		contentPane.add(lblComune);
		//End label lblComune
		
		//Start textfield tfComune
		tfComune = new JTextField();
		tfComune.setBounds(164, 115, 260, 20);
		contentPane.add(tfComune);
		tfComune.setColumns(10);
		//End textfield tfComune
		
		//Start label lblSiglaProvincia
		JLabel lblSiglaProvincia = new JLabel("Sigla provincia :");
		lblSiglaProvincia.setBounds(10, 149, 147, 14);
		contentPane.add(lblSiglaProvincia);
		//End label lblSiglaProvincia
		
		//Start textfield tfSiglaProvincia
		tfSiglaProvincia = new JTextField();
		tfSiglaProvincia.setBounds(164, 146, 260, 20);
		contentPane.add(tfSiglaProvincia);
		tfSiglaProvincia.setColumns(10);
		//End textfield tfSiglaProvincia
	
		//Start lblCAP
		JLabel lblCAP = new JLabel("CAP :");
		lblCAP.setBounds(10, 180, 46, 14);
		contentPane.add(lblCAP);
		//End lblCAP
		
		//Start textfield tfCAP
		tfCAP = new JTextField();
		tfCAP.setBounds(164, 177, 260, 20);
		contentPane.add(tfCAP);
		tfCAP.setColumns(10);
		//End textfield tfCAP
		
		
		/**
		 * Start button btnRegistra
		 * l'actionPerformed controlla e salva i dati inseriti dall'user
		 * poi li utilizza per creare una nuova istanza della classe CentroVaccinale
		 * la quale sarà poi inviata al server tramite la classe Connessione server
		 */
		JButton btnRegistra = new JButton("Registra");
		btnRegistra.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				Boolean isValid = true;
				Integer NCivico = null;
				Integer CAP = null;
				String centrovax, IndirizzoCentroVax,  Comune, SiglaProvincia;
				
				centrovax = tfNomeCentroVax.getText().replaceAll(" ", "");
				IndirizzoCentroVax = tfIndirizzoCentroVax.getText().replaceAll(" ", "");
				try {
					 NCivico = Integer.valueOf(tfNCivico.getText().replaceAll(" ", ""));
				} catch (NumberFormatException a) {
					showMessageDialog(null,"Formato del numero civico non valido");
				}
				Comune = tfComune.getText().replaceAll(" ", "").replaceAll("[0-9]","");
				SiglaProvincia = tfSiglaProvincia.getText().replaceAll(" ", "").replaceAll("[0-9]","");
				IndirizzoCentroVax = tfIndirizzoCentroVax.getText().replaceAll(" ", "");
				try {
					CAP = Integer.valueOf(tfCAP.getText().replaceAll(" ", ""));
				} catch (NumberFormatException a) {
					showMessageDialog(null,"Formato del CAP non valido");
				}
				
				if (centrovax.isEmpty() || IndirizzoCentroVax.isEmpty() || Comune.isEmpty() || SiglaProvincia.isEmpty()  
						|| CAP.toString().isEmpty() || (CAP.toString().length()>5))	isValid = false;
				
				if (isValid == true) {
				
					CV = new CentroVaccinale(centrovax, IndirizzoCentroVax, NCivico, 
						Comune, SiglaProvincia, CAP, (String) comboBox.getSelectedItem(),-1,0);
				
					//System.out.println(CV.tipologia);	
				
					try {
						Socket socket = CentriVaccinali.openSocket();
						ConnessioneServer cs = new ConnessioneServer(socket,"centroVax", CV);
						System.out.println(cs.richiestaServer(cs)); 
					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					OperatoriForm.window.frmAppOperatori.setVisible(true);
					dispose();
					
				} else {
					showMessageDialog(null,"Errore compilazione campi");
				}
			   }
		});
		btnRegistra.setBounds(164, 273, 89, 23);
		contentPane.add(btnRegistra);
		//
		
		//Start btnBack
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				OperatoriForm.window.frmAppOperatori.setVisible(true);
				CleanAll();
				dispose();
			}
		});
		btnBack.setBounds(10, 273, 89, 23);
		contentPane.add(btnBack);
		//End btnBack
		
		//Start button Pulisci
		JButton btnPulisci = new JButton("Pulisci");
		btnPulisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CleanAll();
			}
		});
		btnPulisci.setBounds(335, 273, 89, 23);
		contentPane.add(btnPulisci);
		//End button Pulisci

	}
	static void CleanAll() {
		
		try {
			tfNomeCentroVax.setText("");
			tfIndirizzoCentroVax.setText("");
			tfNCivico.setText("");
			tfComune.setText("");
			tfSiglaProvincia.setText("");
			tfIndirizzoCentroVax.setText("");
			tfCAP.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
