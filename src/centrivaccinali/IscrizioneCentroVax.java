package centrivaccinali;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IscrizioneCentroVax extends JFrame {

	private JPanel contentPane;
	private JTextField tfIndirizzoCentroVax;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IscrizioneCentroVax frame = new IscrizioneCentroVax();
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
		setBounds(100, 100, 450, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRegistra = new JButton("Registra");
		btnRegistra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnRegistra.setBounds(164, 177, 89, 23);
		contentPane.add(btnRegistra);
		
		JLabel lblNomeCentroVax = new JLabel("Nome centro vaccinale : ");
		lblNomeCentroVax.setBackground(UIManager.getColor("Button.highlight"));
		lblNomeCentroVax.setBounds(10, 28, 147, 14);
		contentPane.add(lblNomeCentroVax);
		
		JLabel lblIndirizzo = new JLabel("Indirizzo completo : ");
		lblIndirizzo.setBounds(10, 69, 147, 14);
		contentPane.add(lblIndirizzo);
		
		JLabel lblTipologia = new JLabel("Tipologia :");
		lblTipologia.setBounds(10, 116, 147, 14);
		contentPane.add(lblTipologia);
		
		JTextField tfNomeCentroVax = new JTextField();
		tfNomeCentroVax.setBounds(164, 25, 260, 20);
		contentPane.add(tfNomeCentroVax);
		tfNomeCentroVax.setColumns(10);
		
		tfIndirizzoCentroVax = new JTextField();
		tfIndirizzoCentroVax.setBounds(164, 66, 260, 20);
		contentPane.add(tfIndirizzoCentroVax);
		tfIndirizzoCentroVax.setColumns(10);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ospedaliero", "Aziendale", "Hub"}));
		comboBox.setBounds(164, 112, 260, 22);
		contentPane.add(comboBox);
		
		JButton btnBack = new JButton("Indietro");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnBack.setBounds(10, 177, 89, 23);
		contentPane.add(btnBack);
	}
}
