package cittadini;

import java.io.Serializable;
import java.sql.Date;

public class Utente implements Serializable {
	private static final long serialVersionUID = 253842189L;
	String nomecvacc, nome, cognome, codfisc, email, userID, password, vacc;
	Date datavacc;
	int idvax;
	/**
	 * Costruttore usato dalla classe Iscrizione vaccinato
	 * @param nomecvacc
	 * @param nome
	 * @param cognome
	 * @param codfisc
	 * @param datavacc
	 * @param vacc
	 * @param idvacc
	 * 
	 */
	public Utente(String nomecvacc, String nome, String cognome, String codfisc, Date datavacc, String vacc, int idvax) {
		this.nomecvacc= nomecvacc;
		this.nome = nome;
		this.cognome = cognome;
		this.codfisc = codfisc;
		this.datavacc = datavacc;
		this.vacc = vacc;
		this.idvax = idvax;
	}
	
	/**
	 * Costruttore usato dalla classe registrazione per registrare un cittadino
	 * @param nomecvacc
	 * @param nome
	 * @param cognome
	 * @param codfisc
	 * @param datavacc
	 * @param vacc
	 */
	public Utente(String nomecvacc, String nome, String cognome, String codfisc, Date datavacc, String vacc) {

		

	}
	
	public Utente(String nome, String cognome, String codfisc, String email, String userID, String password, int idvax) {
		this.nome = nome;
		this.cognome = cognome;
		this.codfisc = codfisc;
		this.email = email;
		this.userID = userID;
		this.password = password;
		this.idvax = idvax;
	}

	public String getCentroVax() {
		return this.nomecvacc;
	}
	
	public String getNome() {
		return this.nome;
	}
	public String getCognome() {
		return this.cognome;
	}
	public String getCodfisc() {
		return this.codfisc;
	}
	public java.sql.Date getDatavacc() {
		return this.datavacc;
	}
	public String getVacc() {
		return this.vacc;
	}
	public int getIdvax() {
		return this.idvax;
	}
	public String getEmail() {
		return this.email;
	}
	public String getUserID() {
		return this.userID;
	}
	public String getPassword() {
		return this.password;
		
	}
	

}
