package centrivaccinali;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OperatoriForm {

	public static JFrame frmAppOperatori;
	public static CentriVaccinali CentriVax = new CentriVaccinali();
	public static OperatoriForm window = new OperatoriForm();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperatoriForm window = new OperatoriForm();
					window.frmAppOperatori.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OperatoriForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAppOperatori = new JFrame();
		frmAppOperatori.setTitle("App Operatori - Centro Vaccinale");
		frmAppOperatori.setBounds(100, 100, 450, 214);
		frmAppOperatori.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAppOperatori.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Iscrizione \r\ncentro vaccinale");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(0, 0, 217, 130);
		frmAppOperatori.getContentPane().add(btnNewButton);
		
		JButton btnIscrizioneVax = new JButton("Iscrizione vaccinato");
		btnIscrizioneVax.setBounds(217, 0, 217, 130);
		btnIscrizioneVax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmAppOperatori.getContentPane().add(btnIscrizioneVax);
		
		JButton btnBack = new JButton("Indierto");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CentriVax.frmProgettoCentriVaccinali.setVisible(true);
				window.frmAppOperatori.dispose();
			}
		});
		btnBack.setBounds(168, 141, 101, 32);
		frmAppOperatori.getContentPane().add(btnBack);
	}

}