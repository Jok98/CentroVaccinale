package centrivaccinali;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import cittadini.Utente;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;  

public class IscrizioneVaccinato extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	//static IscrizioneVaccinato frame = new IscrizioneVaccinato();
	private JTextField tfRicercaCentriVax;
	private JButton btnRicercaCentriVax;
	private JLabel lblRicercaCentriVax;
	private JButton btnSelezionaCentroVax;
	private JLabel lblCentroVaxSelezionato;
	private JTextField tfCentroVaxSelezionato;
	private JLabel lblNomeVaccinato;
	private JTextField tfNomeVaccinato;
	private JLabel lblNewLabel;
	private JTextField tfCognomeVaccinato;
	private JTextField tfCodiceFiscale;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JTextField tfDataVaccinazione;
	private JTextField tfID;
	public static IscrizioneVaccinato frame = new IscrizioneVaccinato();
	
	
	public Utente user;
	
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
	@SuppressWarnings("unchecked")
	public IscrizioneVaccinato() {
		setTitle("Iscrizione vaccinato");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 537);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		/**
		 * ricerca centro vaccinato
		 */
		@SuppressWarnings("rawtypes")
		JList listCentriVax = new JList();
		listCentriVax.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCentriVax.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listCentriVax.setBounds(10, 37, 321, 106);
		contentPane.add(listCentriVax);
		JScrollPane scroll = new JScrollPane (listCentriVax);
		scroll.setBounds(10, 37, 321, 106);
		contentPane.add(scroll);
		
		btnRicercaCentriVax = new JButton(" Ricerca Centro");
		btnRicercaCentriVax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRicercaCentriVax.setBounds(341, 10, 163, 23);
		contentPane.add(btnRicercaCentriVax);
		
		
		lblRicercaCentriVax = new JLabel("Ricerca Centro Vax :");
		lblRicercaCentriVax.setBounds(10, 12, 129, 14);
		contentPane.add(lblRicercaCentriVax);
		
		tfRicercaCentriVax = new JTextField();
		tfRicercaCentriVax.setBounds(149, 11, 182, 20);
		contentPane.add(tfRicercaCentriVax);
		tfRicercaCentriVax.setColumns(10);
		
		btnSelezionaCentroVax = new JButton("Seleziona \n Centro");
		btnSelezionaCentroVax.setBounds(341, 120, 163, 23);
		contentPane.add(btnSelezionaCentroVax);
		
		lblCentroVaxSelezionato = new JLabel("Centro selezionato : ");
		lblCentroVaxSelezionato.setBounds(10, 178, 182, 14);
		contentPane.add(lblCentroVaxSelezionato);
		
		tfCentroVaxSelezionato = new JTextField();
		tfCentroVaxSelezionato.setEditable(false);
		tfCentroVaxSelezionato.setBounds(168, 175, 163, 20);
		contentPane.add(tfCentroVaxSelezionato);
		tfCentroVaxSelezionato.setColumns(10);
		// fine ricerca centro vaccinale
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 203, 514, 14);
		contentPane.add(separator);
		
		lblNewLabel_2 = new JLabel("Dati Vaccinato");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 216, 514, 14);
		contentPane.add(lblNewLabel_2);
		
		/**
		 * Nome vaccinato
		 */
		lblNomeVaccinato = new JLabel("Nome :");
		lblNomeVaccinato.setBounds(10, 247, 109, 14);
		contentPane.add(lblNomeVaccinato);
		
		tfNomeVaccinato = new JTextField();
		tfNomeVaccinato.setBounds(168, 244, 321, 20);
		contentPane.add(tfNomeVaccinato);
		tfNomeVaccinato.setColumns(10);
		//
		
		/**
		 * Cognome vaccinato
		 */
		lblNewLabel = new JLabel("Cognome :");
		lblNewLabel.setBounds(10, 278, 138, 14);
		contentPane.add(lblNewLabel);
		
		tfCognomeVaccinato = new JTextField();
		tfCognomeVaccinato.setBounds(168, 275, 321, 20);
		contentPane.add(tfCognomeVaccinato);
		tfCognomeVaccinato.setColumns(10);
		//
		
		/**
		 * Codice Fiscale
		 */
		tfCodiceFiscale = new JTextField();
		tfCodiceFiscale.setBounds(168, 306, 321, 20);
		contentPane.add(tfCodiceFiscale);
		tfCodiceFiscale.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Codice Fiscale :");
		lblNewLabel_1.setBounds(10, 309, 129, 14);
		contentPane.add(lblNewLabel_1);
		//
		
		/**
		 * data somministrazione
		 */
		lblNewLabel_3 = new JLabel("Data somministrazione : ");
		lblNewLabel_3.setBounds(10, 340, 163, 14);
		contentPane.add(lblNewLabel_3);
		
		tfDataVaccinazione = new JTextField();
		tfDataVaccinazione.setBounds(168, 337, 321, 20);
		contentPane.add(tfDataVaccinazione);
		tfDataVaccinazione.setColumns(10);
		//
		
		/**
		 * vaccino somministrato
		 */
		JLabel lblVaxSomministrato = new JLabel("Vaccino somministrato :");
		lblVaxSomministrato.setBounds(10, 369, 129, 14);
		contentPane.add(lblVaxSomministrato);
		
		JComboBox cbVax = new JComboBox();
		cbVax.setModel(new DefaultComboBoxModel(new String[] {"Pfizer", "AstraZeneca", "Moderna", "J&J"}));
		cbVax.setSelectedIndex(0);
		cbVax.setBounds(168, 365, 134, 22);
		contentPane.add(cbVax);
		//
	
		/**
		 * ID univoco
		 */
		JLabel lblID = new JLabel("ID univoco vaccinazione :");
		lblID.setBounds(10, 394, 138, 14);
		contentPane.add(lblID);
		
		tfID = new JTextField();
		tfID.setBounds(168, 391, 321, 20);
		contentPane.add(tfID);
		tfID.setColumns(10);
		//
		
		
		JButton btnRegistra = new JButton("Registra");
		btnRegistra.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				String sDate1=tfDataVaccinazione.getText();  
				SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		        Date parsed = null;
				try {
					parsed = format.parse(sDate1);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		        java.sql.Date sql = new java.sql.Date(parsed.getTime());
			    
				user = new Utente(tfCentroVaxSelezionato.getText(), tfNomeVaccinato.getText(), 
						tfCognomeVaccinato.getText(), tfCodiceFiscale.getText(), sql,
						(String)cbVax.getSelectedItem() ,tfID.getText());
				ConnessioneServer cs;
				try {
					cs = new ConnessioneServer("registrazioneVaccinato", user);
					System.out.println(cs.richiestaServer(cs));
				} catch (IOException | ClassNotFoundException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
		btnRegistra.setBounds(168, 464, 89, 23);
		contentPane.add(btnRegistra);
		
		/**
		 * 
		 */
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperatoriForm.window.frmAppOperatori.setVisible(true);
				frame.dispose();
			}
		});
		btnBack.setBounds(10, 464, 89, 23);
		contentPane.add(btnBack);
		//
		
		
	}
}
