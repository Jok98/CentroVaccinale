package cittadini;

import java.io.Serializable;
import java.sql.Date;

public class Utente implements Serializable {
	private static final long serialVersionUID = 253842189L;
	String nomecvacc, nome, cognome, codfisc,  vacc, idvacc;
	Date datavacc;
	
	public Utente(String nomecvacc, String nome, String cognome, String codfisc, Date datavacc, String vacc, String idvacc) {
		this.nomecvacc= nomecvacc;
		this.nome = nome;
		this.cognome = cognome;
		this.codfisc = codfisc;
		this.datavacc = datavacc;
		this.vacc = vacc;
		this.idvacc = idvacc;
	}
	
	public Utente(String nomecvacc, String nome, String cognome, String codfisc, Date datavacc, String vacc) {
		this.nomecvacc= nomecvacc;
		this.nome = nome;
		this.cognome = cognome;
		this.codfisc = codfisc;
		this.datavacc = datavacc;
		this.vacc = vacc;
	}
	
	public String getCvacc() {
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
	public String getIdvacc() {
		return this.idvacc;
	}
	

}
