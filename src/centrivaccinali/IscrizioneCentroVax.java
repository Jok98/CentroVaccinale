package centrivaccinali;

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
import java.io.IOException;
import java.io.Serializable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class IscrizioneCentroVax extends JFrame {
	
	public CentroVaccinale CV;
	private JPanel contentPane;
	private JTextField tfIndirizzoCentroVax;
	private JTextField tfNCivico;
	private JTextField tfComune;
	private JTextField tfSiglaProvincia;
	private JTextField tfCAP;
	
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
		
		/**
		 * Gestione oggetto nome centro
		 */
		JLabel lblNomeCentroVax = new JLabel("Nome centro vaccinale : ");
		lblNomeCentroVax.setBackground(UIManager.getColor("Button.highlight"));
		lblNomeCentroVax.setBounds(10, 28, 147, 14);
		contentPane.add(lblNomeCentroVax);
		
		JTextField tfNomeCentroVax = new JTextField();
		tfNomeCentroVax.setBounds(164, 25, 260, 20);
		contentPane.add(tfNomeCentroVax);
		tfNomeCentroVax.setColumns(10);
		/**
		 * 
		 */
		
		
		/**
		 * Gestione oggetti Via / Indirizzo
		 */
		JLabel lblVia = new JLabel("Via : ");
		lblVia.setBounds(10, 56, 89, 14);
		contentPane.add(lblVia);
		
		tfIndirizzoCentroVax = new JTextField();
		tfIndirizzoCentroVax.setBounds(164, 53, 260, 20);
		contentPane.add(tfIndirizzoCentroVax);
		tfIndirizzoCentroVax.setColumns(10);
		/**
		 * 
		 */
		
		
		/**
		 * Gestione oggetti tipologia
		 */
		JLabel lblTipologia = new JLabel("Tipologia :");
		lblTipologia.setBounds(10, 212, 147, 14);
		contentPane.add(lblTipologia);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ospedaliero", "Aziendale", "Hub"}));
		comboBox.setBounds(164, 208, 260, 22);
		contentPane.add(comboBox);
		/**
		 * 
		 */
		
		

		
		
		/**
		 * Gestione oggetti numero civico
		 */
		JLabel lblNCivico = new JLabel("Numero civico :");
		lblNCivico.setBounds(10, 87, 147, 14);
		contentPane.add(lblNCivico);
		
		tfNCivico = new JTextField();
		tfNCivico.setBounds(164, 84, 260, 20);
		contentPane.add(tfNCivico);
		tfNCivico.setColumns(10);
		/**
		 * 
		 */
		
		
		/**
		 * gestione oggetti Comune
		 */
		JLabel lblComune = new JLabel("Comune : ");
		lblComune.setBounds(10, 118, 147, 14);
		contentPane.add(lblComune);
		
		tfComune = new JTextField();
		tfComune.setBounds(164, 115, 260, 20);
		contentPane.add(tfComune);
		tfComune.setColumns(10);
		/**
		 * 
		 */
		
		/**
		 * Gestione oggetti Sigla provincia
		 */
		JLabel lblSiglaProvincia = new JLabel("Sigla provincia :");
		lblSiglaProvincia.setBounds(10, 149, 147, 14);
		contentPane.add(lblSiglaProvincia);
		
		tfSiglaProvincia = new JTextField();
		tfSiglaProvincia.setBounds(164, 146, 260, 20);
		contentPane.add(tfSiglaProvincia);
		tfSiglaProvincia.setColumns(10);
		/**
		 * 
		 */
	
		/**
		 * Gestione oggetti CAP
		 */
		JLabel lblCAP = new JLabel("CAP :");
		lblCAP.setBounds(10, 180, 46, 14);
		contentPane.add(lblCAP);
		
		tfCAP = new JTextField();
		tfCAP.setBounds(164, 177, 260, 20);
		contentPane.add(tfCAP);
		tfCAP.setColumns(10);
		/**
		 * 
		 */
		
		
		/**
		 * Inizio btnRegistra
		 */
		JButton btnRegistra = new JButton("Registra");
		btnRegistra.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				//creare nuova istanza locale nel caso in cui non funzionasse
				CV = new CentroVaccinale(tfNomeCentroVax.getText(), tfIndirizzoCentroVax.getText(), tfNCivico.getText(), 
						tfComune.getText(), tfSiglaProvincia.getText(), tfCAP.getText(), (String) comboBox.getSelectedItem());
				/*System.out.println(tfNomeCentroVax.getText()+  tfIndirizzoCentroVax.getText()+ tfNCivico.getText()+ tfComune.getText()
				+ tfSiglaProvincia.getText()+ tfCAP.getText()+ (String) comboBox.getSelectedItem());*/
				
				System.out.println(CV.tipologia);	
				ConnessioneServer cs;
				try {
					cs = new ConnessioneServer("centroVax", CV);
					System.out.println(cs.registraCentroVaccinale(cs));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

			}
		});
		btnRegistra.setBounds(164, 273, 89, 23);
		contentPane.add(btnRegistra);
		//
		
		/**
		 * Gestione btnBack
		 */
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperatoriForm.window.frmAppOperatori.setVisible(true);
				frame.dispose();
			}
		});
		btnBack.setBounds(10, 273, 89, 23);
		contentPane.add(btnBack);
		//
		
		
	}
	
	
}
