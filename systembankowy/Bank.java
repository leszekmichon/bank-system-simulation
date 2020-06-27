package systembankowy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Klasa opisujaca obiekt typu Bank - wirtualna reprezentacja placowki bankowej
 */
public class Bank {
	
	/*
	 * Lista klientow banku
	 */
	ArrayList<Klient> spisKlientow = new ArrayList<>();
	
	/*
	 * Metoda zakladajaca nowe konto
	 */
	public Konto zalozKonto(Klient klient, int pin, boolean czySpr) throws IOException {
		Konto konto = new Konto(klient, pin, czySpr, 1, 0);
		return konto;
	}
	
	/*
	 * Metoda dodajaca klienta do listy klientow
	 */
	public void addKlient(Klient klient) {
		spisKlientow.add(klient);
	}
	
}
