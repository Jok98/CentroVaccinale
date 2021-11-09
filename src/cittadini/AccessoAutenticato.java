package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AccessoAutenticato {

	public JFrame frmInvioDatiEventi;
	public static AccessoAutenticato window = new AccessoAutenticato();
	private JTable tblEventiAvversi;
	private JTable tblNomeColonne;
	private JTextField tfCentroVax;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//AccessoAutenticato window = new AccessoAutenticato();
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
		tblEventiAvversi.setRowSelectionAllowed(false);
		tblEventiAvversi.setModel(new DefaultTableModel(
			new Object[][] {
				{"mal di testa", null, null},
				{"febbre", null, null},
				{"dolori muscolari e articolari", null, null},
				{"linfoadenopatia", null, null},
				{"tachicardia", null, null},
				{"crisi ipertensiva", null, null},
			},
			new String[] {
				"Evento", "Severita", "Note"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Integer.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblEventiAvversi.getColumnModel().getColumn(0).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(0).setPreferredWidth(175);
		tblEventiAvversi.getColumnModel().getColumn(1).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(1).setPreferredWidth(105);
		tblEventiAvversi.getColumnModel().getColumn(2).setResizable(false);
		tblEventiAvversi.getColumnModel().getColumn(2).setPreferredWidth(200);
		tblEventiAvversi.getColumnModel().getColumn(2).setMinWidth(35);
		tblEventiAvversi.setBounds(10, 70, 601, 96);
		frmInvioDatiEventi.getContentPane().add(tblEventiAvversi);
		
		tblNomeColonne = new JTable();
		tblNomeColonne.setEnabled(false);
		tblNomeColonne.setModel(new DefaultTableModel(
			new Object[][] {
				{"Evento avverso", "Severit\u00E0 (da 1 a 5)", "Note opzionali (256 caratteri)"},
			},
			new String[] {
				"0", "1", "2"
			}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			@SuppressWarnings("unchecked")
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblNomeColonne.getColumnModel().getColumn(0).setResizable(false);
		tblNomeColonne.getColumnModel().getColumn(0).setPreferredWidth(175);
		tblNomeColonne.getColumnModel().getColumn(1).setResizable(false);
		tblNomeColonne.getColumnModel().getColumn(1).setPreferredWidth(105);
		tblNomeColonne.getColumnModel().getColumn(2).setResizable(false);
		tblNomeColonne.getColumnModel().getColumn(2).setPreferredWidth(200);
		tblNomeColonne.setRowSelectionAllowed(false);
		tblNomeColonne.setBounds(10, 48, 601, 16);
		frmInvioDatiEventi.getContentPane().add(tblNomeColonne);
		
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
