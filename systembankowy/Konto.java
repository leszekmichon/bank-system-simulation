package systembankowy;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/*
 * Klasa opisujaca obiekt typu Konto 
 */
public class Konto {
	
	/*
	 * Klient do ktorego jest przypisane konto
	 */
	Klient klient;
	
	int id;

	long numerKonta;

	boolean czyAktywowane = false;
	
	long srodki = 0;
	
	int pin;
	
	/*
	 * Konstruktor ustawiajacy wartosci podstawowych pol obiektu i wpisujacy PIN konta do bazy danych 
	 */
	public Konto(Klient klient, int pin, boolean czySpr, long nr, long pieniadze) throws IOException {
		this.klient = klient;
		this.pin = pin;

		if (czySpr) {
			BufferedReader brrr = new BufferedReader(new FileReader("baza.txt"));
			String strr;
			while ((strr = brrr.readLine()) != null) {
				if (strr.split(";")[0].equals(Long.toString(nr))) {
					numerKonta = nr;
					break;
				}
			}
			System.out.println("Numer konta: "+numerKonta);

			BufferedReader br = new BufferedReader(new FileReader("skarbiec.txt"));
			String st;
			while ((st = br.readLine()) != null) {
				if (st.split(";")[0].equals(nr)) {
					FileWriter fws = new FileWriter("skarbiec.txt", true);
					BufferedWriter writers = new BufferedWriter(fws);
					writers.write(this.getNumerKonta()+";"+pieniadze+";"+klient.getPesel());
					writers.newLine();
					writers.close();
					break;
				}
			}
			this.aktywuj();
			klient.addKonto(this);
		} else  {
			numerKonta = 10000 + (long) (Math.random() * (99999 - 10000));
			System.out.println("Numer konta: "+numerKonta);
			boolean flag = true;
			BufferedReader br = new BufferedReader(new FileReader("baza.txt"));
			String st;
			while ((st = br.readLine()) != null) {

				if (st.equals(this.getNumerKonta()+";"+(this.id+1)+";"+pin+";"+klient.getPesel())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				FileWriter fw = new FileWriter("baza.txt", true);
				this.setId(this.getWlasciciel().getKontoSize() + 1);
				BufferedWriter writer = new BufferedWriter(fw);
				writer.write(this.getNumerKonta()+";"+this.id+";"+pin+";"+klient.getPesel());
				writer.newLine();
				writer.close();

			}
			FileWriter fws = new FileWriter("skarbiec.txt", true);
			BufferedWriter writers = new BufferedWriter(fws);
			writers.write(this.getNumerKonta()+";"+this.getSrodki()+";"+klient.getPesel());
			writers.newLine();
			writers.close();

			klient.addKonto(this);
		}

	}
	
	public void aktywuj() {
		czyAktywowane = true;
	}
	
	public void dezaktywuj() {
		czyAktywowane = false;
	}
	
	public void zmianaSrodkow(long zmiana) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("skarbiec.txt"));
		String st;
		ArrayList<String> lines = new ArrayList<>();
		while ((st = br.readLine()) != null) {

			String tab[] = st.split(";");

			if(tab[0].equals(Long.toString(numerKonta))) {
				long stanKonta = Long.parseLong(tab[1]);
				stanKonta = stanKonta + zmiana;

				lines.add(tab[0]+";"+stanKonta);

			} else lines.add(st);

		}
		FileWriter fw = new FileWriter("skarbiec.txt", false);
		BufferedWriter writer = new BufferedWriter(fw);
		for (String i : lines) {
			writer.write(i);
			writer.newLine();
		}
		writer.newLine();
		writer.close();

		srodki += zmiana;
	}
	
	public long getSrodki() throws IOException {

		BufferedReader br1 = new BufferedReader(new FileReader("skarbiec.txt"));
		String st1;
		while ((st1 = br1.readLine()) != null) {
			String tab[] = st1.split(";");
			if(tab[0].equals(Long.toString(numerKonta))) {
				return Long.parseLong(tab[1]);
			}
		}
		return 0;

	}
	
	public boolean getCzyAktywowane() {
		return czyAktywowane;
	}
	
	public int getPin() {
		return pin;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Klient getWlasciciel() {
		return klient;
	}

	public long getNumerKonta() {
		return numerKonta;
	}
	
}
