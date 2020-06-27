package systembankowy;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * Klasa opisujaca obiekt typu Klient
 */
public class Klient {
	
	private String imie;
	
	private String nazwisko;
	
	private String pesel;

	private String haslo;

	
	/*
	 * Lista kont posiadanych przez klienta
	 */
	public Map<Integer, Konto> spisKont = new HashMap<>();
	
	/*
	 * Konstruktor ustawiajacy wartosci podstawowych pol obiektu i wpisujacy te wartosci do bazy danych 
	 */
	public Klient(String imie, String nazwisko, String pesel, String haslo, Bank bank) throws IOException {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.pesel = pesel;
		this.haslo = haslo;
		bank.addKlient(this);

		boolean flag = true;
		BufferedReader br = new BufferedReader(new FileReader("bazaklientow.txt"));
		String st;
		while ((st = br.readLine()) != null) {
			if (st.equals(imie+";"+nazwisko+";"+pesel+";"+haslo)) {
				flag = false;
				break;
			}
		}
		if (flag) {
			FileWriter fw = new FileWriter("bazaklientow.txt", true);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.write(imie+";"+nazwisko+";"+pesel+";"+haslo);
			writer.newLine();
			writer.close();
		}
	}
	
	/*
	 * Zwraca obiekt Konto o podanym id
	 */
	public Konto getKonto(int id) {
		return spisKont.get(id);
	}
	
	public String getNazwisko() {
		return imie+" "+nazwisko;
	}

	public String getPesel() {
		return pesel;
	}

	public int getKontoSize() {
		return spisKont.size();
	}

	public String getHaslo() {
		return haslo;
	}
	
	/*
	 * Dodaje konto do listy kont klienta i ustawia jego id
	 */
	public void addKonto(Konto konto) {
		int i = spisKont.size();
		spisKont.put(i+1, konto);
		konto.setId(i+1);
	}
	
}
