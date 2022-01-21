package centrivaccinali;

import java.io.Serializable;

public class CentroVaccinale implements Serializable {
	
	/**
	 * chiave utilizzata per ricostruire la classe dopo la serializzazione a uno stream di byte
	 */
	private static final long serialVersionUID = 7613409875169727612L;
	String nome ;
	String via ;
	Integer nciv ;
	String comune ;
	String prov ;
	Integer CAP ;
	String tipologia ;
	public static String richiesta ="centrovax";
	
	/**
	 * Il costruttore crea un istanza con argomenti :
	 * @param nome
	 * @param via
	 * @param nciv
	 * @param citta
	 * @param prov
	 * @param CAP
	 * @param tipologia
	 */
	public CentroVaccinale(String nome,String via, Integer nciv,String comune, String prov,Integer CAP, String tipologia) {
		this.nome = nome;
		this.via = via;
		this.nciv= nciv;
		this.comune = comune;
		this.prov = prov;
		this.CAP = CAP;
		this.tipologia = tipologia;
	}
	
	/**
	 * Il metodo getCitta() restituisce la citta del centro vaccinale
	 * @return
	 */
	public String getComune() {
		return this.comune;
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
	public Integer getCap() {
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
	 * Il metodo gettipologiaris() restituisce il tipologiao del centro vaccinale
	 * @return
	 */
	public String getTipologia() {
		return this.tipologia;
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
	public Integer getNciv() {
		return this.nciv;
	}
	
	/**
	 * Il metodo getInfo() restituisce tutte le informazioni del centro vaccinale 
	 * @return
	 */
	public String getInfo() {
		String info = "Nome : " + this.nome + "\r\n"+"Via : " + this.via + ", " + this.comune + " " + this.CAP + ", " + this.prov + "\r\n"+"Tipologia centro : " + this.tipologia ;
		
		return info;
	}
}
