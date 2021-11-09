package server;

import java.io.Serializable;

import centrivaccinali.CentroVaccinale;
import cittadini.Utente;

public class Richiesta implements Serializable{
	private static final long serialVersionUID = 192873466528L;
	String operazione;
	CentroVaccinale cv;
	String querycom, querytip, querynom ;
	public Utente ut;
	
	public Richiesta(String operazione, CentroVaccinale cv) {
		this.operazione= operazione;
		this.cv= cv;
	}
	
	public Richiesta(String operazione, Utente ut) {
		this.operazione= operazione;
		this.ut= ut;
	}
	
	public Richiesta(String operazione) {
		this.operazione= operazione;
	}
	
	public String getOp() {
		return this.operazione;
	}
	
	public void setComune(String comune) {
		this.querycom = comune;
	}
	
	public void setTip(String tipologia) {
		this.querytip = tipologia;
	}
	
	public void setNome(String nome) {
		this.querynom = nome;
	}
	
	public String getComune() {
		return this.querycom;
	}
	
	public String getInfo() {
		return cv.getInfo();
	}

	public CentroVaccinale getCv() {
		return cv;
	}
	
	public Utente getUt() {
		return ut;
	}

}
