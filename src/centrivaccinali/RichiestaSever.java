package centrivaccinali;

public class RichiestaSever {
	String richiesta;
	Object obj;
	
	public RichiestaSever(String richiesta, Object obj) {
		this.richiesta = richiesta;
		this.obj =obj;
		
	}
	
	public String getRichiesta() {
		return richiesta;
	}
	
	public Object getObj() {
		return obj;
	}
	
	
}
