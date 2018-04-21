package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
	
	private MeteoDAO dao = new MeteoDAO();
	private List<Rilevamento> rilevamenti;
	private List<Citta> citta;
	private int bestCost=0;
	private List<SimpleCity> soluzione=new ArrayList<SimpleCity>();
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		rilevamenti=dao.getAllRilevamenti();
		citta = dao.getCitta();	
	}



	public String trovaSequenza(int mese) {
		List<SimpleCity> parziale = new ArrayList<SimpleCity>();
		bestCost=0;
		for(Citta c:citta) {
			
			c.setVisited(true);
			c.increaseCounter();
			c.increaseCounterB();
			SimpleCity s = new SimpleCity(c.getNome(),this.costoC(mese, c, 1,null),1);
			parziale.add(s);
			this.ricorsiva(2, mese, parziale,c);
			parziale.remove(s);
			c.setVisited(false);
			c.decreaseCounter();
			c.resetCounterB();
		}
		StringBuilder sb=new StringBuilder();
		sb.append("l'elenco delle citta visitate è:\n");
		
		for(SimpleCity s:soluzione) {
			sb.append(s+", ");
			System.out.println("STEP: "+s.getStep()+"CITTA: "+s.getNome()+" COSTO:  "+s.getCosto());
		}
		System.out.println(" \n     -----     ");
		sb.append("\nIl costo finale è: "+this.calcolaCostoSoluzione(soluzione));
		return sb.toString();
	}
	
	private void ricorsiva(int step,int mese,List<SimpleCity> parziale,Citta last) {
		if(step-1==NUMERO_GIORNI_TOTALI) {
			this.verificaCostoSoluzione(parziale);
			return;
		}
		for(Citta c:citta) {
			int tcont=0;
			if(c.equals(last)) {				
				last.increaseCounterB();
				tcont=last.getCounterB();
			}	
			else {
				if(last.getCounterB()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					continue;	
				c.increaseCounterB();
				tcont=last.getCounterB();
				last.resetCounterB();
			}
			if(c.getCounter()<NUMERO_GIORNI_CITTA_MAX) {
				
				SimpleCity s = new SimpleCity(c.getNome(),this.costoC(mese, c, step,last),step);
				c.increaseCounter();
				c.setVisited(true);
				parziale.add(s);
				if(this.checkCost(parziale))
					this.ricorsiva(step+1, mese, parziale,c);	
				parziale.remove(s);
				c.setVisited(false);
				c.decreaseCounter();
			}
			c.decreaseCounterB();
			if(c.getCounterB()==0)
				last.setCounterB(tcont);
		}
		return;
	}
	
	/**
	 * AGGIORNA IL COSTO OTTIMALE IN BASE ALLA SOLUZIONE APPENA TROVATA
	 * @param soluzioneCandidata
	 */
	private void verificaCostoSoluzione(List<SimpleCity> soluzioneCandidata) {
		int costoS=this.calcolaCostoSoluzione(soluzioneCandidata);
		if(!this.checkVisit(soluzioneCandidata))
			return;
		if(bestCost==0) {
			soluzione.clear();
			soluzione.addAll(soluzioneCandidata);
			bestCost=costoS;
		}			
		if(bestCost>costoS) {
			bestCost=costoS;
			soluzione.clear();
			soluzione.addAll(soluzioneCandidata);
		}
		return;
	}
	
	
	/**VERIFICA SE SONOP STATE VISITATE TUTTE LE CITTA
	 * @param soluzioneCandidata
	 * @return
	 */
	private boolean checkVisit(List<SimpleCity> soluzioneCandidata) {
		for(Citta c:citta)
			if(!c.isVisited())
				return false;
		return true;
	}
	



	/**
	 * LA FUNZIONE CONTROLLA SE IL COSTO DELLA SOLUZIONE PARZIALE HA GIA SUPERATO IL COSTO OTTIMALE
	 * @param parziale
	 * @return
	 */
	private boolean checkCost(List<SimpleCity> parziale) {
		int costoS=this.calcolaCostoSoluzione(parziale);
		if(bestCost==0)
			return true;
		else if(bestCost<costoS) 
			return false;
		return true;
	}

	
	/**
	 * LA FUNZIONE CALCOLA IL COSTO DI UNA CITTA IN UN DETERMINATO GIORNO/MESE
	 * @param mese
	 * @param c
	 * @param day
	 * @param s
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int costoC(int mese,Citta c,int day,Citta last) {  
		int costo=0;
		for(Rilevamento r:c.getRilevamenti())
			if(r.getData().getDate()==day &&r.getData().getMonth()==(mese-1))
				costo=r.getUmidita();
		if(last!=null&&!c.equals(last))
			costo+=COST;		
		return costo;
	}
	
	/**
	 * LA FUZNIONE CALCOLA IL COSTO DI UNA SOLUZIONE CONOSCENDO LE CITTA VISITATE
	 * @param soluzione
	 * @return
	 */
	private int calcolaCostoSoluzione(List<SimpleCity> soluzione) {
		int costo=0;
		for(SimpleCity s:soluzione)
			costo+=s.getCosto();
		return costo;
	}


	

	
	//DA QUI METODI ESERCIZIO 1
	
	public String getUmiditaMedia(int mese) {
		StringBuilder sb= new StringBuilder();
		for(Citta c:citta) {
			sb.append("umidità "+c.getNome()+":  "+this.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("deprecation")
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		List<Rilevamento> rilevamentoMese = new ArrayList<Rilevamento>();
		
		for(Rilevamento r: rilevamenti)
			if((r.getData().getMonth()+1)==mese &&r.getLocalita().equals(localita))
				rilevamentoMese.add(r);

		return rilevamentoMese;
	}
	
	public double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		List<Rilevamento> rilevamentoMese =  this.getAllRilevamentiLocalitaMese(mese, localita);
		double avg=0.0;
		for(Rilevamento r:rilevamentoMese)
			avg+=r.getUmidita();

		return avg/=rilevamentoMese.size();
	}

}
