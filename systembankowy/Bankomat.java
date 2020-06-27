package systembankowy;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static systembankowy.Symulacja.okno;
import static systembankowy.Symulacja.oknoBankomat;


/*
 * Klasa opisujaca obiekt typu Bankomat
 */
public class Bankomat {
	
	/*
	 * Wyswietla interfejs bankomatu, ktory po podaniu prawidlowego kodu PIN umozliwia wplate i wyplate srodkow z konta
	 * 
	 * @param n - pomocnicza zmienna typu String 
	 * @param klient - obiekt typu Klient reprezentujacy klienta ktory korzysta z bankomatu
	 * @param id - id konta do ktorego klient chce uzyskac dostep
	 */
	public void menu(String n, Klient klient, int id, Bank bank, int licznik) throws IOException {

		if (licznik==3) {
			JFrame frame6 = new JFrame();
			frame6.setTitle("Bankomat");
			frame6.setSize(600, 400);
			frame6.setLocation(600, 300);
			frame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame6.setResizable(false);
			frame6.setDefaultLookAndFeelDecorated(true);
			JOptionPane.showMessageDialog(frame6, "Konto zablokowane");

			oknoBankomat(klient, bank, "Podaj ID konta: ", new JFrame());

			klient.getKonto(id).dezaktywuj();
			return;
		}


		 String pinString = JOptionPane.showInputDialog("Podaj"+ n +"PIN: ");
		 if(pinString == null) {
			 oknoBankomat(klient, bank, "Podaj ID konta: ", new JFrame());
			 return;
		 }
		 int pin = Integer.parseInt(pinString);
		 
		 //tutaj trzeba zmienic sciezke do pliku bedacego baza danych klientow
		 File file = new File("baza.txt");
		 
		 BufferedReader br = new BufferedReader(new FileReader(file)); 
		 boolean x = false;
		 String st; 
		 boolean wplata = true;
		 
		 while ((st = br.readLine()) != null) {

		 	 long lineNumer = Long.parseLong(st.split(";")[0]);

			 int linePin = Integer.parseInt(st.split(";")[2]);

		 	 if (lineNumer!=klient.getKonto(id).getNumerKonta()) continue;


			 if (pin==linePin&&klient.getKonto(id).getCzyAktywowane()==true) {

				 JFrame frame = new JFrame("Bankomat");
				 JFrame frame1 = new JFrame("Bankomat");
				 JPanel panel = new JPanel();  
			     panel.setLayout(new FlowLayout());  
			     JPanel panel1 = new JPanel();  
			     panel1.setLayout(new FlowLayout());  
			     JButton button1 = new JButton();  
			     button1.setText("Wp³ata");  		     
			     JButton button2 = new JButton();  
			     button2.setText("Wyp³ata");
				 JButton button3 = new JButton();
				 button3.setText("Wroc");

				 JLabel labelSrodki = new JLabel("Œrodki na koncie "+klient.getNazwisko()+": "+klient.getKonto(id).getSrodki()+" zl");
				 labelSrodki.setBounds(20, 50, 400, 40);
				 frame.add(labelSrodki);

				 JLabel label = new JLabel("Podaj kwotê: ");
			     panel.add(button1);  
			     panel.add(button2);
			     panel.add(button3);
			     frame.add(panel);  
			     frame1.add(panel1);
			     frame1.setSize(400,140);  
			     frame.setSize(400,140);  
			     frame.setLocationRelativeTo(null);  
			     frame1.setLocationRelativeTo(null); 
			     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			     frame.setVisible(true);  
			     frame1.setVisible(false);
			     panel1.add(label);    
			     JTextField input = new JTextField(16);
			     panel1.add(input);
			     JButton ok = new JButton();  
			     ok.setText("OK");
			     panel1.add(ok); 
			     
			     
			     JFrame frame2 = new JFrame("Bankomat");
			     JPanel panel2 = new JPanel();  
			     panel2.setLayout(new FlowLayout());  
			     frame2.add(panel2); 
			     frame2.setSize(400,140);  
			     frame2.setLocationRelativeTo(null);
			     frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			     frame2.setVisible(false);
			     panel2.add(label);  
			     JTextField input1 = new JTextField(16);
			     panel2.add(input1);
			     JButton ok1 = new JButton();
			     ok1.setText("OK");
			     panel2.add(ok1); 
			     
			     button1.addActionListener(new ActionListener(){  
			    	    public void actionPerformed(ActionEvent e){  
			    	            frame1.setVisible(true); 
			    	            frame.setVisible(false);  
			    	    }  
			     });  
			     button2.addActionListener(new ActionListener(){  
			    	    public void actionPerformed(ActionEvent e){  
			    	            frame2.setVisible(true); 
			    	            frame.setVisible(false);  
			    	    }  
			     });
				 button3.addActionListener(new ActionListener(){
					 public void actionPerformed(ActionEvent e){
						 frame.dispose();
						 okno(klient, bank, frame);
					 }
				 });
				 ok.addActionListener(new ActionListener(){
			    	    public void actionPerformed(ActionEvent e){
			    	    		if(!input.getText().equals("")) {
									int inp = Integer.parseInt(input.getText());
									frame1.setVisible(false);
									try {
										klient.getKonto(id).zmianaSrodkow(inp);
									} catch (IOException ioException) {
										ioException.printStackTrace();
									}
									try {
										labelSrodki.setText("Œrodki na koncie "+klient.getNazwisko()+": "+klient.getKonto(id).getSrodki()+" zl");
									} catch (IOException ioException) {
										ioException.printStackTrace();
									}
									input.setText("");
									frame.setVisible(true);
								}
			    	    }  
			     }); 
			     ok1.addActionListener(new ActionListener(){  
			    	    public void actionPerformed(ActionEvent e){
							if(!input1.getText().equals("")) {
								int inp = Integer.parseInt(input1.getText());
								try {
									if(klient.getKonto(id).getSrodki()<inp) {
										input1.setText("");
										frame.setVisible(true);
										frame2.setVisible(false);
										return;
									}
								} catch (IOException ioException) {
									ioException.printStackTrace();
								}
								frame2.setVisible(false);
								try {
									klient.getKonto(id).zmianaSrodkow(-inp);
								} catch (IOException ioException) {
									ioException.printStackTrace();
								}
								try {
									labelSrodki.setText("Œrodki na koncie "+klient.getNazwisko()+": "+klient.getKonto(id).getSrodki()+" zl");
								} catch (IOException ioException) {
									ioException.printStackTrace();
								}
								frame.setVisible(true);
								input1.setText("");
							}

			    	    }  
			     }); 

				 x = true;
				 break;
			 }
		 }
		 if(x==false) {
		 	licznik++;
		 	menu(" poprawny ", klient, id, bank, licznik);
		 }
	}

	
}
