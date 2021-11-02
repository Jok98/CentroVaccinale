package centrivaccinali;

import java.io.Serializable;

public class CentroVaccinale implements Serializable {
	
	/**
	 * chiave utilizzata per ricostruire la classe dopo la serializzazione a uno stream di byte
	 */
	private static final long serialVersionUID = 7613409875169727612L;
	String nome ;
	String via ;
	String nciv ;
	String citta ;
	String prov ;
	String CAP ;
	String tip ;
	
	/**
	 * Il costruttore crea un istanza con argomenti :
	 * @param nome
	 * @param via
	 * @param nciv
	 * @param citta
	 * @param prov
	 * @param CAP
	 * @param tip
	 */
	public CentroVaccinale(String nome,String via, String nciv,String citta, String prov,String CAP, String tip) {
		this.nome = nome;
		this.via = via;
		this.nciv= nciv;
		this.citta = citta;
		this.prov = prov;
		this.CAP = CAP;
		this.tip = tip;
	}
	
	/**
	 * Il metodo getCitta() restituisce la citta del centro vaccinale
	 * @return
	 */
	public String getCitta() {
		return this.citta;
	}
	
	/**
	 * Il metodo getVia() restituisce la via del centro vaccinale
	 * @return
	 */
	public String getVia() {
		return this.via;
	}
	
	/**
	 * Il metodo getCap() restituisce il cap del centro vaccinale
	 * @return
	 */
	public String getCap() {
		return this.CAP;
	}
	
	/**
	 * Il metodo getNome() restituisce il nome del centro vaccinale
	 * @return
	 */
	public String getNome() {
		return this.nome;
	}
	/**
	 * Il metodo getTipris() restituisce il tipo del centro vaccinale
	 * @return
	 */
	public String getTip() {
		return this.tip;
	}
	
	/**
	 * Il metodo getProv() restituisce la provincia del centro vaccinale
	 * @return
	 */
	public String getProv() {
		return this.prov;
	}
	
	/**
	 * Il metodo getNciv() restituisce il numero civico del centro vaccinale
	 * @return
	 */
	public String getNciv() {
		return this.nciv;
	}
	
	/**
	 * Il metodo getInfo() restituisce tutte le informazioni del centro vaccinale 
	 * @return
	 */
	public String getInfo() {
		String info = "Nome : " + this.nome + " | Via : " + this.via + ", " + this.citta + " " + this.CAP + ", " + this.prov + ", Tipologia centro : " + this.tip ;
		
		return info;
	}
}
