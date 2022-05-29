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
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import static javax.swing.JOptionPane.showMessageDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;  

/**
 * 
 * @author Moi Matteo/Alex Rabuffetti
 *
 */
public class IscrizioneVaccinato extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JButton btnRicercaCentriVax;
	private JButton btnSelezionaCentroVax;
	private JButton btnPulisci;
	private static JTextField tfRicercaCentriVax;
	private static JTextField tfCentroVaxSelezionato;
	private static JTextField tfNomeVaccinato;
	private static JTextField tfCognomeVaccinato;
	private static JTextField tfCodiceFiscale;
	private static JTextField tf_gg;
	private static JTextField tf_aaaa;
	
	private JLabel lblCentroVaxSelezionato;
	private JLabel lblRicercaCentriVax;
	private JLabel lblNomeVaccinato;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lbl_mm;
	static int IdUnivoco;
	
	public static IscrizioneVaccinato frame = new IscrizioneVaccinato();
	private ArrayList<CentroVaccinale> src_result;
	
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
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public IscrizioneVaccinato() {
		setResizable(false);
		setTitle("Iscrizione vaccinato");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Start list listCentriVax
		JList listCentriVax = new JList();
		listCentriVax.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCentriVax.setModel(new AbstractListModel() {
			String[] values = new String[] {};
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
		//End list listCentriVax
		
		//Start button btnRicercaCentriVax
		btnRicercaCentriVax = new JButton(" Ricerca Centro");
		btnRicercaCentriVax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = CentriVaccinali.openSocket();
					ConnessioneServer cs = new ConnessioneServer(socket,"srcCentroVax", tfRicercaCentriVax.getText());
					
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
		});
		btnRicercaCentriVax.setBounds(341, 10, 163, 23);
		contentPane.add(btnRicercaCentriVax);
		//End button btnRicercaCentriVax
		
		//Start lblRicercaCentriVax
		lblRicercaCentriVax = new JLabel("Ricerca Centro Vax :");
		lblRicercaCentriVax.setBounds(10, 12, 129, 14);
		contentPane.add(lblRicercaCentriVax);
		//End lblRicercaCentriVax
		
		//Start tfRicercaCentriVax
		tfRicercaCentriVax = new JTextField();
		tfRicercaCentriVax.setBounds(149, 11, 182, 20);
		contentPane.add(tfRicercaCentriVax);
		tfRicercaCentriVax.setColumns(10);
		//End tfRicercaCentriVax
		
		//Start button btnSelezionaCentroVax
		btnSelezionaCentroVax = new JButton("Seleziona Centro");
		btnSelezionaCentroVax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String centrovax_selected=src_result.get(listCentriVax.getSelectedIndex()).getNome();
				//showMessageDialog(null, "Informazione del Centro vaccinale : \r\n"+src_result.get(listCentriVax.getSelectedIndex()).getInfo());
				tfCentroVaxSelezionato.setText(centrovax_selected);
			}
		});
		btnSelezionaCentroVax.setBounds(341, 174, 163, 23);
		contentPane.add(btnSelezionaCentroVax);
		//End button btnSelezionaCentroVax
		
		//Start lblCentroVaxSelezionato
		lblCentroVaxSelezionato = new JLabel("Centro selezionato : ");
		lblCentroVaxSelezionato.setBounds(10, 178, 182, 14);
		contentPane.add(lblCentroVaxSelezionato);
		//End lblCentroVaxSelezionato
		
		//Start tfCentroVaxSelezionato
		tfCentroVaxSelezionato = new JTextField();
		tfCentroVaxSelezionato.setEditable(false);
		tfCentroVaxSelezionato.setBounds(168, 175, 163, 20);
		contentPane.add(tfCentroVaxSelezionato);
		tfCentroVaxSelezionato.setColumns(10);
		//End tfCentroVaxSelezionato
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 203, 514, 14);
		contentPane.add(separator);
		
		//Star label Dati Vaccinato
		lblNewLabel_2 = new JLabel("Dati Vaccinato");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 216, 514, 14);
		contentPane.add(lblNewLabel_2);
		//End label Dati Vaccinato
		
		//Star label lblNomeVaccinato
		lblNomeVaccinato = new JLabel("Nome :");
		lblNomeVaccinato.setBounds(10, 247, 109, 14);
		contentPane.add(lblNomeVaccinato);
		//End label lblNomeVaccinato
		
		//Start textfield tfNomeVaccinato
		tfNomeVaccinato = new JTextField();
		tfNomeVaccinato.setBounds(168, 244, 321, 20);
		contentPane.add(tfNomeVaccinato);
		tfNomeVaccinato.setColumns(10);
		//End textfield tfNomeVaccinato
		
		//Start label Cognome
		lblNewLabel = new JLabel("Cognome :");
		lblNewLabel.setBounds(10, 278, 138, 14);
		contentPane.add(lblNewLabel);
		//End label Cognome
		
		//Start textfield tfCognomeVaccinato
		tfCognomeVaccinato = new JTextField();
		tfCognomeVaccinato.setBounds(168, 275, 321, 20);
		contentPane.add(tfCognomeVaccinato);
		tfCognomeVaccinato.setColumns(10);
		//End textfield tfCognomeVaccinato
		
		//Start textfield tfCodiceFiscale
		tfCodiceFiscale = new JTextField();
		tfCodiceFiscale.setBounds(168, 306, 321, 20);
		contentPane.add(tfCodiceFiscale);
		tfCodiceFiscale.setColumns(10);
		//End textfield tfCodiceFiscale
		
		//Start label Codice Fiscale
		lblNewLabel_1 = new JLabel("Codice Fiscale :");
		lblNewLabel_1.setBounds(10, 309, 129, 14);
		contentPane.add(lblNewLabel_1);
		//End label Codice Fiscale
		
		//Start label Data somministrazione
		lblNewLabel_3 = new JLabel("Data somministrazione : ");
		lblNewLabel_3.setBounds(10, 340, 163, 14);
		contentPane.add(lblNewLabel_3);
		//End label Data somministrazione
		
		//Start textfield giorni
		tf_gg = new JTextField();
		tf_gg.setBounds(189, 337, 46, 20);
		contentPane.add(tf_gg);
		tf_gg.setColumns(10);
		//End textfield giorni
		
		//Start label lblVaxSomministrato
		JLabel lblVaxSomministrato = new JLabel("Vaccino somministrato :");
		lblVaxSomministrato.setBounds(10, 369, 129, 14);
		contentPane.add(lblVaxSomministrato);
		//End label lblVaxSomministrato
		
		//Start ComboBox cbVax contiene tipologie vaccini
		JComboBox cbVax = new JComboBox();
		cbVax.setModel(new DefaultComboBoxModel(new String[] {"Pfizer", "AstraZeneca", "Moderna", "J&J"}));
		cbVax.setSelectedIndex(0);
		cbVax.setBounds(168, 365, 163, 22);
		contentPane.add(cbVax);
		//End ComboBox cbVax contiene tipologie vaccini
		
		//Start button btnDatiCentroVax
		JButton btnDatiCentroVax = new JButton("Mostra dati");
		btnDatiCentroVax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String centrovax_selected=src_result.get(listCentriVax.getSelectedIndex()).getNome();
				showMessageDialog(null, "Informazione del Centro vaccinale : \r\n"+src_result.get(listCentriVax.getSelectedIndex()).getInfo());
			}
		});
		btnDatiCentroVax.setBounds(341, 120, 163, 23);
		contentPane.add(btnDatiCentroVax);
		//End button btnDatiCentroVax
		
		//Start ComboBox cb_Month
		JComboBox cb_Month = new JComboBox();
		cb_Month.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		cb_Month.setSelectedIndex(0);
		cb_Month.setBounds(285, 337, 46, 22);
		contentPane.add(cb_Month);
		//End ComboBox cb_Month
		
		//Start label lbl_gg
		JLabel lbl_gg = new JLabel("gg");
		lbl_gg.setBounds(168, 340, 27, 14);
		contentPane.add(lbl_gg);
		//End label lbl_gg
		
		//Start label lbl_mm
		lbl_mm = new JLabel("mm");
		lbl_mm.setBounds(256, 340, 27, 14);
		contentPane.add(lbl_mm);
		//End label lbl_mm
		
		//Start textfield tf_aaaa
		tf_aaaa = new JTextField();
		tf_aaaa.setBounds(403, 337, 86, 20);
		contentPane.add(tf_aaaa);
		tf_aaaa.setColumns(10);
		//End textfield tf_aaaa
		
		//Start label lbl_aaaa
		JLabel lbl_aaaa = new JLabel("aaaa");
		lbl_aaaa.setBounds(360, 340, 33, 14);
		contentPane.add(lbl_aaaa);
		//End label lbl_aaaa
		
		//Start Button btnRegistra
		/* l'actionPerformed controlla i dati inseriti dall'user e li utilizza per creare una nuova istanza 
		della classe utente la quale verrà poi inviata al server per registrare l'utente nel db */
		JButton btnRegistra = new JButton("Registra");
		btnRegistra.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				if(tfCentroVaxSelezionato.getText().isEmpty()) {
					showMessageDialog(null,"Selezionare il centro in cui si e' stata eseguita la vaccinazione");
				}else {
					String gg = (tf_gg.getText().length()<2)? "0"+tf_gg.getText():null;
					String sDate1=gg+((String) cb_Month.getSelectedItem())+tf_aaaa.getText(); 
					System.out.println("data della vaccinazione :"+sDate1);
					if (sDate1.isEmpty()& sDate1.length()!=6) {
						showMessageDialog(null,"Immettere data vaccinazione");
					} else {
						SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
						Date parsed = null;
						try {
							parsed = format.parse(sDate1);
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						java.sql.Date sql = new java.sql.Date(parsed.getTime());
					
						Boolean isValid = true;
					
						String NomeVaccinato = tfNomeVaccinato.getText().replaceAll(" ", "").replaceAll("[0-9]","");
						String CognomeVaccinato = tfCognomeVaccinato.getText().replaceAll(" ", "").replaceAll("[0-9]","");
						String CodiceFiscale = tfCodiceFiscale.getText().replaceAll(" ", "");
						
						if (NomeVaccinato.isEmpty()||CognomeVaccinato.isEmpty()||CodiceFiscale.isEmpty()) isValid = false;			
						
						if (isValid==true) {
								user = new Utente(tfCentroVaxSelezionato.getText(), NomeVaccinato, 
								CognomeVaccinato, CodiceFiscale, sql,(String)cbVax.getSelectedItem() );
								try {	
									//System.out.println("centyro vax rilevato dal client "+tfCentroVaxSelezionato.getText());
									Socket socket = CentriVaccinali.openSocket();
									ConnessioneServer cs = new ConnessioneServer(socket,"registrazioneVaccinato", user);
					
									System.out.println(ConnessioneServer.richiestaServer(cs));
								} catch (IOException | ClassNotFoundException e1) {
					
									e1.printStackTrace();
								}
								showMessageDialog(null,"Id univoco vaccinato : "+IdUnivoco);
								/*OperatoriForm.window.frmAppOperatori.setVisible(true);
								frame.dispose();*/
						} else {
							showMessageDialog(null,"Ci sono stringhe vuote");
						}
					}
				   }
				}
			});
		btnRegistra.setBounds(168, 409, 89, 23);
		contentPane.add(btnRegistra);
		//End Button btnRegistra
		
		//Start button btnBack
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperatoriForm.window.frmAppOperatori.setVisible(true);
				CleanAll();
				frame.dispose();
			}
		});
		btnBack.setBounds(10, 409, 89, 23);
		contentPane.add(btnBack);
		
		btnPulisci = new JButton("Pulisci");
		btnPulisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CleanAll();
			}
		});
		btnPulisci.setBounds(415, 409, 89, 23);
		contentPane.add(btnPulisci);
		//End button btnBack
	}
	static void CleanAll() {
		
		try {
			tfRicercaCentriVax.setText("");
			tfCentroVaxSelezionato.setText("");
			tfNomeVaccinato.setText("");
			tfCognomeVaccinato.setText("");
			tfCodiceFiscale.setText("");
			tf_gg.setText("");
			tf_aaaa.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
