package centrivaccinali;

import java.io.Serializable;

/**
 * 
 * @author Moi Matteo/Alex Rabuffetti
 *
 */
public class CentroVaccinale implements Serializable {
	
	
	//chiave utilizzata per ricostruire la classe dopo la serializzazione a uno stream di byte
	private static final long serialVersionUID = 7613409875169727612L;
	String nome ;
	String via ;
	Integer nciv ;
	String comune ;
	String prov ;
	Integer CAP ;
	String tipologia ;
	int severita_media;
	int n_segnalazioni;
	public static String richiesta ="centrovax";
	
	/**
	 * Il costruttore imposta un nuovo centro vaccinale con argomenti :
	 * @param nome
	 * @param via 
	 * @param nciv
	 * @param citta
	 * @param prov
	 * @param CAP
	 * @param tipologia
	 */
	public CentroVaccinale(String nome,String via, Integer nciv,String comune, String prov,Integer CAP, String tipologia, int severita_media, int n_segnalazioni) {
		this.nome = nome;
		this.via = via;
		this.nciv= nciv;
		this.comune = comune;
		this.prov = prov;
		this.CAP = CAP;
		this.tipologia = tipologia;
		this.severita_media=severita_media;
		this.n_segnalazioni=n_segnalazioni;
	}
	
	/**
	 * restituisce la citta del centro vaccinale
	 * @return comune
	 */
	public String getComune() {
		return this.comune;
	}
	
	/**
	 * restituisce la via del centro vaccinale
	 * @return via
	 */
	public String getVia() {
		return this.via;
	}
	
	/**
	 * restituisce il cap del centro vaccinale
	 * @return CAP
	 */
	public Integer getCap() {
		return this.CAP;
	}
	
	/**
	 * restituisce il nome del centro vaccinale
	 * @return nome
	 */
	public String getNome() {
		return this.nome;
	}
	/**
	 * restituisce il tipologiao del centro vaccinale
	 * @return tipologia
	 */
	public String getTipologia() {
		return this.tipologia;
	}
	
	/**
	 * restituisce la provincia del centro vaccinale
	 * @return prov
	 */
	public String getProv() {
		return this.prov;
	}
	
	/**
	 * restituisce il numero civico del centro vaccinale
	 * @return nciv
	 */
	public Integer getNciv() {
		return this.nciv;
	}
	
	/**
	 * restituisce tutte le informazioni del centro vaccinale : nome,via,comune,CAP,prov,tipologia,severità media,numero segnalazioni
	 * @return info
	 */
	public String getInfo() {
		String info = "Nome : " + this.nome + "\r\n"+"Via : " + this.via + ", " + this.comune + " " + this.CAP + ", " + this.prov + "\r\n"+"Tipologia centro : " + this.tipologia
				+ "\r\n"+"Severità media : " + this.severita_media+ "\r\n"+"Segnalazioni totali : "+this.n_segnalazioni;
		
		return info;
	}
}
