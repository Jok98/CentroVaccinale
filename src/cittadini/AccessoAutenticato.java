package cittadini;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class AccessoAutenticato {

	public JFrame frmInvioDatiEventi;
	public static AccessoAutenticato window = new AccessoAutenticato();
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
	private void initialize() {
		frmInvioDatiEventi = new JFrame();
		frmInvioDatiEventi.setTitle("Segnalazione eventi avversi");
		frmInvioDatiEventi.setBounds(100, 100, 450, 300);
		frmInvioDatiEventi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

}
