package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

import centrivaccinali.CentriVaccinali;

import java.awt.TextField;
import java.awt.Label;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class CittadiniForm {

	public static JFrame frmCittadini;
	static Registrazione registrazione = new Registrazione();
	static AccessoAutenticato AccessConf = new AccessoAutenticato();
	static CentriVaccinali centrivax = new CentriVaccinali();
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
	
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	private void initialize() {
		frmCittadini = new JFrame();
		frmCittadini.setTitle("App Cittadini - Centri Vaccinali");
		frmCittadini.setBounds(100, 100, 450, 371);
		frmCittadini.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCittadini.getContentPane().setLayout(null);
		
		JButton btnLogIn = new JButton("LogIn");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccessConf.frmInvioDatiEventi.setVisible(true);
				frmCittadini.setVisible(false);
			}
		});
		btnLogIn.setBounds(335, 302, 89, 23);
		frmCittadini.getContentPane().add(btnLogIn);
		
		JButton btnRegistrazione = new JButton("Registrati");
		btnRegistrazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registrazione.frmRegistrazionePressoCentro.setVisible(true);
				frmCittadini.setVisible(false);
			}
		});
		btnRegistrazione.setBounds(168, 302, 89, 23);
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
		list_result.setBounds(1, 46, 323, 95);
		frmCittadini.getContentPane().add(list_result);
		JScrollPane scroll = new JScrollPane (list_result);
		scroll.setBounds(10, 52, 414, 142);
		frmCittadini.getContentPane().add(scroll);
		
		TextField tfRicerca = new TextField();
		tfRicerca.setBounds(168, 10, 180, 22);
		frmCittadini.getContentPane().add(tfRicerca);
		
		Label lblRicerca = new Label("Ricerca centro vaccinale : ");
		lblRicerca.setBounds(10, 10, 177, 22);
		frmCittadini.getContentPane().add(lblRicerca);
		
		Button btnCerca = new Button("Cerca");
		btnCerca.setBounds(354, 10, 70, 22);
		frmCittadini.getContentPane().add(btnCerca);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centrivax.frmProgettoCentriVaccinali.setVisible(true);
				frmCittadini.dispose();
			}
		});
		btnBack.setBounds(10, 302, 89, 23);
		frmCittadini.getContentPane().add(btnBack);
		
		JButton btnShowResult = new JButton("Mostra dati");
		btnShowResult.setBounds(159, 205, 110, 23);
		frmCittadini.getContentPane().add(btnShowResult);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 250, 434, 23);
		frmCittadini.getContentPane().add(separator);
	}
}
