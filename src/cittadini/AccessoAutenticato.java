package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import centrivaccinali.CentriVaccinali;
import centrivaccinali.ConnessioneServer;

import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;
import java.awt.Font;

public class AccessoAutenticato {

	public JFrame frmInvioDatiEventi;
	public static AccessoAutenticato window = new AccessoAutenticato();
	static JTextField tfCentroVax;
	private ArrayList<Object> Eventi_Avversi = new ArrayList<Object>();
	private JTextField tf_0;
	private JTextField tf_1;
	private JTextField tf_2;
	private JTextField tf_3;
	private JTextField tf_4;
	private JTextField tf_5;
	/**
	 * Lancia l'applicazione 
	 * @throws Exception
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccessoAutenticato window = new AccessoAutenticato();
					window.frmInvioDatiEventi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Inizializza il contenuto del frame
	 */
	public AccessoAutenticato() {
		initialize();
	}

	@SuppressWarnings("serial")
	private void initialize() {
		frmInvioDatiEventi = new JFrame();
		frmInvioDatiEventi.setTitle("Segnalazione eventi avversi");
		frmInvioDatiEventi.setBounds(100, 100, 625, 606);
		frmInvioDatiEventi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmInvioDatiEventi.getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 38, 621, 10);
		frmInvioDatiEventi.getContentPane().add(separator);
		
		tfCentroVax = new JTextField();
		tfCentroVax.setEditable(false);
		tfCentroVax.setBounds(327, 7, 270, 20);
		frmInvioDatiEventi.getContentPane().add(tfCentroVax);
		tfCentroVax.setColumns(10);
		
		
		JLabel lblCentroVax = new JLabel("Centro vaccinale dove \u00E8 stata eseguita la vaccinazione :");
		lblCentroVax.setBounds(10, 10, 321, 14);
		frmInvioDatiEventi.getContentPane().add(lblCentroVax);
		
		JComboBox cb_0 = new JComboBox();
		cb_0.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_0.setSelectedIndex(0);
		cb_0.setBounds(175, 80, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_0);
		
		JComboBox cb_1 = new JComboBox();
		cb_1.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_1.setSelectedIndex(0);
		cb_1.setBounds(175, 156, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_1);
		
		JComboBox cb_2 = new JComboBox();
		cb_2.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_2.setSelectedIndex(0);
		cb_2.setBounds(175, 232, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_2);
		
		JComboBox cb_3 = new JComboBox();
		cb_3.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_3.setSelectedIndex(0);
		cb_3.setBounds(175, 308, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_3);
		
		JComboBox cb_4 = new JComboBox();
		cb_4.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_4.setSelectedIndex(0);
		cb_4.setBounds(175, 382, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_4);
		
		JComboBox cb_5 = new JComboBox();
		cb_5.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		cb_5.setSelectedIndex(0);
		cb_5.setBounds(175, 454, 32, 22);
		frmInvioDatiEventi.getContentPane().add(cb_5);
		
		tf_0 = new JTextField();
		tf_0.setBounds(242, 59, 355, 65);
		frmInvioDatiEventi.getContentPane().add(tf_0);
		tf_0.setColumns(10);
		
		tf_1 = new JTextField();
		tf_1.setColumns(10);
		tf_1.setBounds(242, 135, 355, 65);
		frmInvioDatiEventi.getContentPane().add(tf_1);
		
		tf_2 = new JTextField();
		tf_2.setColumns(10);
		tf_2.setBounds(242, 211, 355, 65);
		frmInvioDatiEventi.getContentPane().add(tf_2);
		
		tf_3 = new JTextField();
		tf_3.setColumns(10);
		tf_3.setBounds(242, 287, 355, 65);
		frmInvioDatiEventi.getContentPane().add(tf_3);
		
		tf_4 = new JTextField();
		tf_4.setColumns(10);
		tf_4.setBounds(242, 363, 355, 61);
		frmInvioDatiEventi.getContentPane().add(tf_4);
		
		tf_5 = new JTextField();
		tf_5.setColumns(10);
		tf_5.setBounds(242, 435, 355, 61);
		frmInvioDatiEventi.getContentPane().add(tf_5);
		
		JButton btnSegnala = new JButton("Segnala");
		btnSegnala.addActionListener(new ActionListener() {
		/**
		 * 
		 *Questo metodo raccoglie i dati inseriti dall'utente, li inserisce nell'ArrayList di tipo Oggetto e li
		 *invia al server tramite ConnessioneServer per inserire poi i dati nel DB e aggiornare la media 
		 * @param e Azione dell'utente
		 * @return void
		 * @throws IOException nel caso di operazioni I/O fallite o corrotte
		 * @throws ClassNotFoundException nel caso in cui si cerchi di caricare una classe non trovata
		 * 
		 */
			public void actionPerformed(ActionEvent e) {
				//System.out.println((String) tblEventiAvversi.getModel().getValueAt(0, 0));
				Boolean check = true;
				Eventi_Avversi.add(tfCentroVax.getText());
				JComboBox [] cb = {cb_0,cb_1,cb_2,cb_3,cb_4,cb_5};
				JTextField [] tf = {tf_0,tf_1,tf_2,tf_3,tf_4,tf_5};
				for(int i =0 ; i<=5; i++) {
					Eventi_Avversi.add(Integer.valueOf((String) cb[i].getSelectedItem()));
					Eventi_Avversi.add((String)tf[i].getText());
					
				}
				System.out.println(Eventi_Avversi);
				try {
					Socket socket = CentriVaccinali.openSocket();
					ConnessioneServer cs = new ConnessioneServer(socket,"eventiAvversi", Eventi_Avversi);
					System.out.println(ConnessioneServer.richiestaServer(cs));
				} catch (IOException | ClassNotFoundException e1) {
					
					e1.printStackTrace();
				}
				Eventi_Avversi.clear();
				//frmInvioDatiEventi.setVisible(false);
				CittadiniForm.frmCittadini.setVisible(true);
				frmInvioDatiEventi.dispose();
				//System.out.println(Eventiavversi);
			}
		});
		btnSegnala.setBounds(508, 521, 89, 42);
		frmInvioDatiEventi.getContentPane().add(btnSegnala);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmInvioDatiEventi.setVisible(false);
				CittadiniForm.frmCittadini.setVisible(true);
				frmInvioDatiEventi.dispose();
			}
		});
		btnBack.setBounds(10, 521, 89, 42);
		frmInvioDatiEventi.getContentPane().add(btnBack);
		
		JLabel lblMalDiTesta = new JLabel("Mal di testa :");
		lblMalDiTesta.setLabelFor(cb_0);
		lblMalDiTesta.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMalDiTesta.setBounds(10, 84, 98, 14);
		frmInvioDatiEventi.getContentPane().add(lblMalDiTesta);
		
		JLabel lblFebbre = new JLabel("Febbre :");
		lblFebbre.setLabelFor(cb_1);
		lblFebbre.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFebbre.setBounds(10, 160, 98, 14);
		frmInvioDatiEventi.getContentPane().add(lblFebbre);
		
		JLabel lblDoloriMuscolariE = new JLabel("Dolori muscolari/articolari");
		lblDoloriMuscolariE.setLabelFor(cb_2);
		lblDoloriMuscolariE.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDoloriMuscolariE.setBounds(10, 236, 155, 20);
		frmInvioDatiEventi.getContentPane().add(lblDoloriMuscolariE);
		
		JLabel lblLinfoadenopatia = new JLabel("Linfoadenopatia :");
		lblLinfoadenopatia.setLabelFor(cb_3);
		lblLinfoadenopatia.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLinfoadenopatia.setBounds(10, 312, 98, 14);
		frmInvioDatiEventi.getContentPane().add(lblLinfoadenopatia);
		
		JLabel lblTachicardia = new JLabel("Tachicardia :");
		lblTachicardia.setLabelFor(cb_4);
		lblTachicardia.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTachicardia.setBounds(10, 386, 98, 14);
		frmInvioDatiEventi.getContentPane().add(lblTachicardia);
		
		JLabel lblCrisiIpertensiva = new JLabel("Crisi ipertensiva :");
		lblCrisiIpertensiva.setLabelFor(cb_5);
		lblCrisiIpertensiva.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCrisiIpertensiva.setBounds(10, 458, 98, 14);
		frmInvioDatiEventi.getContentPane().add(lblCrisiIpertensiva);
		
		
		
		
	}
}