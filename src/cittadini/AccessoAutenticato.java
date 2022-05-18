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

public class AccessoAutenticato {

	public JFrame frmInvioDatiEventi;
	public static AccessoAutenticato window = new AccessoAutenticato();
	private JTable tblEventiAvversi;
	static JTextField tfCentroVax;
	private ArrayList<Object> Eventi_Avversi = new ArrayList<Object>();
	/**
	 * Launch the application.
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
	 * Create the application.
	 */
	public AccessoAutenticato() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frmInvioDatiEventi = new JFrame();
		frmInvioDatiEventi.setTitle("Segnalazione eventi avversi");
		frmInvioDatiEventi.setBounds(100, 100, 637, 300);
		frmInvioDatiEventi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmInvioDatiEventi.getContentPane().setLayout(null);
		
		tblEventiAvversi = new JTable();
		tblEventiAvversi.setSurrendersFocusOnKeystroke(true);
		tblEventiAvversi.setRowSelectionAllowed(false);
		tblEventiAvversi.setModel(new DefaultTableModel(
			new Object[][] {
				{"Mal di testa", null, null},
				{"Febbre", null, null},
				{"Dolori muscolari e articolari", null, null},
				{"Linfoadenopatia", null, null},
				{"Tachicardia", null, null},
				{"Crisi ipertensiva", null, null},
			},
			new String[] {
				"EventoAvverso", "Severita", "Note Aggiuntive"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblEventiAvversi.getColumnModel().getColumn(0).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(0).setPreferredWidth(150);
		tblEventiAvversi.getColumnModel().getColumn(0).setMinWidth(100);
		tblEventiAvversi.getColumnModel().getColumn(1).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(1).setPreferredWidth(35);
		tblEventiAvversi.getColumnModel().getColumn(2).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(2).setPreferredWidth(200);
		tblEventiAvversi.getColumnModel().getColumn(2).setMinWidth(200);
		tblEventiAvversi.setBounds(10, 70, 587, 96);
		frmInvioDatiEventi.getContentPane().add(tblEventiAvversi);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 38, 621, 10);
		frmInvioDatiEventi.getContentPane().add(separator);
		
		tfCentroVax = new JTextField();
		tfCentroVax.setEditable(false);
		tfCentroVax.setBounds(341, 7, 270, 20);
		frmInvioDatiEventi.getContentPane().add(tfCentroVax);
		tfCentroVax.setColumns(10);
		
		
		JLabel lblCentroVax = new JLabel("Centro vaccinale dove \u00E8 stata eseguita la vaccinazione :");
		lblCentroVax.setBounds(10, 10, 321, 14);
		frmInvioDatiEventi.getContentPane().add(lblCentroVax);
		
		JButton btnSegnala = new JButton("Segnala");
		btnSegnala.addActionListener(new ActionListener() {
		/**
		 * @param e
		 * TODO da inserire controlli su input e modificare valore cella note in stringa
		 * 
		 */
			public void actionPerformed(ActionEvent e) {
				System.out.println((String) tblEventiAvversi.getModel().getValueAt(0, 0));
				
				Eventi_Avversi.add(tfCentroVax.getText());
				for(int i =0 ; i<=5; i++) {
					Eventi_Avversi.add((Integer)tblEventiAvversi.getModel().getValueAt(i, 1));
					Eventi_Avversi.add((String)tblEventiAvversi.getModel().getValueAt(i, 2));
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
		btnSegnala.setBounds(242, 227, 89, 23);
		frmInvioDatiEventi.getContentPane().add(btnSegnala);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmInvioDatiEventi.setVisible(false);
				CittadiniForm.frmCittadini.setVisible(true);
				frmInvioDatiEventi.dispose();
			}
		});
		btnBack.setBounds(10, 227, 89, 23);
		frmInvioDatiEventi.getContentPane().add(btnBack);
		
		
	}
}
