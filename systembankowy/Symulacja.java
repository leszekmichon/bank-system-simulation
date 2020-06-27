package systembankowy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Symulacja {

    private static void oknoPrzelew(Klient klient, Bank bank, JFrame prev) {

        JFrame frame = new JFrame();
        frame.setTitle("Placowka banku");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel info = new JLabel("");
        info.setBounds(160, 10, 400, 60);
        frame.add(info);

        JLabel numer = new JLabel("Numer konta:");
        numer.setBounds(48, 60, 450, 30);
        frame.add(numer);
        JTextField nr = new JTextField();
        nr.setBounds(160, 60, 250, 30);
        frame.add(nr);

        JLabel kwota = new JLabel("Kwota:");
        kwota.setBounds(48, 130, 450, 30);
        frame.add(kwota);
        JTextField kw = new JTextField();
        kw.setBounds(160, 130, 250, 30);
        frame.add(kw);

        JLabel id = new JLabel("ID Twojego konta:");
        id.setBounds(48, 200, 450, 30);
        frame.add(id);
        JTextField idd = new JTextField();
        idd.setBounds(160, 200, 250, 30);
        frame.add(idd);

        JButton wyslij = new JButton("Wyslij przelew");
        wyslij.setBounds(80, 270, 200, 60);
        frame.add(wyslij);
        wyslij.addActionListener(e -> {
            try {
                if (Integer.parseInt(kw.getText())>klient.getKonto(Integer.parseInt(idd.getText())).getSrodki()) {
                    throw new NullPointerException();
                }

                klient.getKonto(Integer.parseInt(idd.getText())).zmianaSrodkow(-Long.parseLong(kw.getText()));

                //wplata
                BufferedReader br = new BufferedReader(new FileReader("skarbiec.txt"));
                String st;
                ArrayList<String> lines = new ArrayList<>();
                while ((st = br.readLine()) != null) {

                    String tab[] = st.split(";");

                    if(tab[0].equals(Long.toString(Long.parseLong(nr.getText())))) {
                        long stanKonta = Long.parseLong(tab[1]);
                        stanKonta = stanKonta + Long.parseLong(kw.getText());

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
                //

                info.setText("Przelew wyslany pomyslnie.");


            } catch (NullPointerException | FileNotFoundException | NumberFormatException x) {
                info.setText("Niepoprawne dane/niewystarczajace srodki na koncie");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        });
        JButton wroc = new JButton("Wroc");
        wroc.setBounds(300, 270, 200, 60);
        frame.add(wroc);
        wroc.addActionListener(e -> {
            frame.dispose();
            try {
                oknoBank(klient, bank, frame);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);

    }

    private static void oknoZaloz(Klient klient, Bank bank, JFrame prev) {
        JFrame frame = new JFrame();
        frame.setTitle("Placowka banku");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel miejsce = new JLabel("Podaj PIN - zostanie on przypisany do Twojego konta (ID = "+(klient.getKontoSize()+1)+"): ");
        miejsce.setBounds(58, 80, 450, 30);
        frame.add(miejsce);

        JLabel info = new JLabel("");
        info.setBounds(170, 120, 300, 30);
        frame.add(info);

        JTextField input = new JTextField();
        input.setBounds(58, 120, 100, 30);
        frame.add(input);

        JButton aktywuj = new JButton("Zaloz konto");
        aktywuj.setBounds(50, 180, 200, 50);
        frame.add(aktywuj);
        JButton aktywuj1 = new JButton("Wroc");
        aktywuj1.setBounds(250, 180, 200, 50);
        frame.add(aktywuj1);
        aktywuj.addActionListener(e -> {
            try {
                bank.zalozKonto(klient, Integer.parseInt(input.getText()), false);
                miejsce.setText("Zalozono konto o ID = "+(klient.getKontoSize())+"! Nr konta: "+klient.getKonto(klient.getKontoSize()).getNumerKonta());
                input.setEnabled(false);
                aktywuj.setEnabled(false);
            } catch (IOException | NumberFormatException x) {
                info.setText("Niepoprawny format danych");
            }

        });
        aktywuj1.addActionListener(e -> {
            frame.dispose();
            try {
                oknoBank(klient, bank, frame);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void oknoAktywuj(Klient klient, Bank bank, JFrame prev) {
        JFrame frame = new JFrame();
        frame.setTitle("Placowka banku");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel miejsce = new JLabel("Podaj ID konta ktore chcesz aktywowac lub dezaktywowac");
        miejsce.setBounds(58, 80, 450, 30);
        frame.add(miejsce);

        JTextField input = new JTextField();
        input.setBounds(58, 120, 100, 30);
        frame.add(input);

        JLabel info = new JLabel("");
        info.setBounds(170, 120, 300, 30);
        frame.add(info);


        JButton aktywuj = new JButton("Aktywuj/dezaktywuj");
        aktywuj.setBounds(50, 180, 200, 50);
        frame.add(aktywuj);
        JButton aktywuj1 = new JButton("Wroc");
        aktywuj1.setBounds(250, 180, 200, 50);
        frame.add(aktywuj1);
        aktywuj.addActionListener(e -> {
            try {
                if (klient.getKonto(Integer.parseInt(input.getText())).getCzyAktywowane()) {
                    klient.getKonto(Integer.parseInt(input.getText())).dezaktywuj();
                    miejsce.setText("Konto dezaktywowane");
                } else {
                    klient.getKonto(Integer.parseInt(input.getText())).aktywuj();
                    miejsce.setText("Konto aktywowane");
                }
            } catch (NumberFormatException | NullPointerException x) {
                info.setText("Niepoprawne dane");
            }

        });
        aktywuj1.addActionListener(e -> {
            frame.dispose();
            try {
                oknoBank(klient, bank, frame);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void oknoBank(Klient klient, Bank bank, JFrame prev) throws IOException {
        JFrame frame = new JFrame();
        frame.setTitle("Placowka banku");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel miejsce = new JLabel("Wybierz usluge: ");
        miejsce.setBounds(58, 80, 300, 30);
        frame.add(miejsce);

        JButton zaloz = new JButton("Zaloz konto");
        zaloz.setBounds(50, 120, 210, 50);
        frame.add(zaloz);
        zaloz.addActionListener(e -> {
            frame.dispose();
            oknoZaloz(klient, bank, frame);
        });

        JButton aktywuj = new JButton("Aktywuj/dezaktywuj konto");
        aktywuj.setBounds(50, 180, 210, 50);
        frame.add(aktywuj);
        aktywuj.addActionListener(e -> {
            frame.dispose();
            oknoAktywuj(klient, bank, frame);
        });

        JButton aktywuj1 = new JButton("Wroc");
        aktywuj1.setBounds(350, 240, 200, 50);
        frame.add(aktywuj1);
        aktywuj1.addActionListener(e -> {
            frame.dispose();
            okno(klient, bank, frame);
        });

        JButton przelew = new JButton("Przelew");
        przelew.setBounds(50, 240, 210, 50);
        frame.add(przelew);
        przelew.addActionListener(e -> {
            frame.dispose();
            oknoPrzelew(klient, bank, frame);
        });


        ////spis kont bankowych
        Map<Integer, Konto> spisKontKlienta = new HashMap<>();
        spisKontKlienta = klient.spisKont;
        JLabel konta = new JLabel();
        konta.setBounds(340, 20, 200, 300);
        frame.add(konta);
        String s = "<html><p>Twoje konta:</p>";
        String e = "</html>";
        for (int i=1; i<=spisKontKlienta.size(); i++) {
            String kolor;
            if (spisKontKlienta.get(i).getCzyAktywowane()) kolor = "<p style=\"color:green\">";
            else kolor = "<p style=\"color:red\">";

            s = s+"<br/>"+kolor+i+".&nbsp;&nbsp;"+spisKontKlienta.get(i).getNumerKonta()+"&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;"+spisKontKlienta.get(i).getSrodki()+" zl</p>";
        }
        konta.setText(s+e);
        ////

        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void oknoBankomat(Klient klient, Bank bank, String tekst, JFrame prev) throws IOException {
        try {
            String idString = JOptionPane.showInputDialog(tekst);
            if(idString == null)
            {
                okno(klient, bank, prev);
                return;
            }
            int id = Integer.parseInt(idString);
            if (!klient.getKonto(id).getCzyAktywowane()) {
                oknoBankomat(klient, bank, "Konto jest nieaktywne", prev);
                return;
            }
            Bankomat bankomat = new Bankomat();
            bankomat.menu("", klient, id, bank, 0);
        } catch (NumberFormatException | NullPointerException e) {
            oknoBankomat(klient, bank, "Nieprawidlowe ID", prev);
        }
    }

    public static void okno(Klient klient, Bank bank, JFrame prev){
        JFrame frame = new JFrame();
        frame.setTitle("Symulacja korzystania z uslugi");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel witaj = new JLabel("Witaj "+klient.getNazwisko()+"!");
        witaj.setBounds(158, 50, 300, 30);
        frame.add(witaj);

        JLabel miejsce = new JLabel("Wybierz miejsce do ktorego chcesz sie udac: ");
        miejsce.setBounds(158, 80, 300, 30);
        frame.add(miejsce);

        JButton bankk = new JButton("Bank");
        bankk.setBounds(140, 150, 120, 50);
        frame.add(bankk);
        bankk.addActionListener(e -> {
            frame.dispose();
            try {
                oknoBank(klient, bank, prev);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        JButton bankomat = new JButton("Bankomat");
        bankomat.setBounds(320, 150, 120, 50);
        frame.add(bankomat);
        bankomat.addActionListener(e -> {
            frame.dispose();
            try {
                JFrame prevv = new JFrame();
                prevv.setLocation(frame.getX(), frame.getY());
                oknoBankomat(klient, bank, "Podaj ID konta: ", prevv);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void program(Bank bank, JFrame prev) throws IOException {

        File baza = new File("baza.txt");
        File bazaklientow = new File("bazaklientow.txt");
        File skarbiec = new File("skarbiec.txt");
        if(!(baza.exists()&&bazaklientow.exists()&&skarbiec.exists())) {
            baza.createNewFile();
            bazaklientow.createNewFile();
            skarbiec.createNewFile();
        }

        JFrame frame = new JFrame();
        frame.setTitle("Symulacja korzystania z uslugi");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JButton log = new JButton("Logowanie");
        log.setBounds(140, 150, 120, 50);
        frame.add(log);
        log.addActionListener(e -> {
            frame.dispose();
            oknoLogowanie(bank, frame);
        });

        JButton rej = new JButton("Rejestracja");
        rej.setBounds(320, 150, 120, 50);
        frame.add(rej);
        rej.addActionListener(e -> {
            frame.dispose();
            oknoRejestracja(bank, frame);
        });


        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void oknoRejestracja(Bank bank, JFrame prev){
        JFrame frame = new JFrame();
        frame.setTitle("Symulacja korzystania z uslugi");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel info = new JLabel("");
        info.setBounds(160, 10, 400, 40);
        frame.add(info);

        JLabel imie = new JLabel("Imie:");
        imie.setBounds(48, 40, 450, 30);
        frame.add(imie);
        JTextField im = new JTextField();
        im.setBounds(160, 40, 250, 30);
        frame.add(im);

        JLabel kwota = new JLabel("Nazwisko:");
        kwota.setBounds(48, 80, 450, 30);
        frame.add(kwota);
        JTextField kw = new JTextField();
        kw.setBounds(160, 80, 250, 30);
        frame.add(kw);

        JLabel id = new JLabel("Pesel:");
        id.setBounds(48, 120, 450, 30);
        frame.add(id);
        JTextField idd = new JTextField();
        idd.setBounds(160, 120, 250, 30);
        frame.add(idd);

        JLabel haslo = new JLabel("Haslo:");
        haslo.setBounds(48, 160, 450, 30);
        frame.add(haslo);
        JTextField has = new JTextField();
        has.setBounds(160, 160, 250, 30);
        frame.add(has);

        JButton wyslij = new JButton("OK");
        wyslij.setBounds(80, 250, 200, 50);
        frame.add(wyslij);
        wyslij.addActionListener(e -> {
            try {
                if (im.getText().equals("") || kw.getText().equals("") || idd.getText().equals("") || has.getText().equals("")) {
                    info.setText("Nieprawidlowe dane");
                } else {

                    Long k = Long.parseLong(idd.getText());

                    Klient klient = new Klient(im.getText(), kw.getText(), idd.getText(), has.getText(), bank);
                    info.setText("Zostales zarejestrowany");
                    wyslij.setEnabled(false);
                    im.setEnabled(false);
                    kw.setEnabled(false);
                    idd.setEnabled(false);
                    has.setEnabled(false);
                }

            } catch (IOException | NullPointerException | NumberFormatException x) {
                info.setText("Nieprawidlowe dane");
            }
        });

        JButton wroc = new JButton("Wroc");
        wroc.setBounds(300, 250, 200, 50);
        frame.add(wroc);
        wroc.addActionListener(e -> {
            frame.dispose();
            try {
                program(bank, frame);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void oknoLogowanie(Bank bank, JFrame prev){
        JFrame frame = new JFrame();
        frame.setTitle("Symulacja korzystania z uslugi");
        frame.setSize(600, 400);
        frame.setLocation(prev.getX(), prev.getY());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(true);

        JLabel info = new JLabel("");
        info.setBounds(160, 10, 400, 60);
        frame.add(info);

        JLabel imie = new JLabel("Pesel:");
        imie.setBounds(48, 60, 450, 30);
        frame.add(imie);
        JTextField im = new JTextField();
        im.setBounds(160, 60, 250, 30);
        frame.add(im);

        JLabel haslo = new JLabel("Haslo:");
        haslo.setBounds(48, 100, 450, 30);
        frame.add(haslo);
        JPasswordField has = new JPasswordField();
        has.setBounds(160, 100, 250, 30);
        frame.add(has);

        JButton wyslij = new JButton("OK");
        wyslij.setBounds(80, 270, 200, 60);
        frame.add(wyslij);
        wyslij.addActionListener(e -> {
            try {

                BufferedReader br = new BufferedReader(new FileReader("bazaklientow.txt"));
                String st;
                while ((st = br.readLine()) != null) {
                    String tab[] = st.split(";");

                    if(tab[2].equals(im.getText())&&tab[3].equals(has.getText())) {

                        //
                        Klient klient = new Klient(tab[0], tab[1], tab[2], tab[3], bank);

                        BufferedReader konta = new BufferedReader(new FileReader("baza.txt"));
                        String s;
                        int i = 0;
                        while ((s = konta.readLine()) != null) i++;
                        BufferedReader kontaa = new BufferedReader(new FileReader("baza.txt"));
                        String sa;
                        while (i>0) {
                            sa = kontaa.readLine();
                            String liniaKonta[] = sa.split(";");
                            if (liniaKonta[3].equals(klient.getPesel())) {
                                String numerKonta = liniaKonta[0];
                                int idKonta = Integer.parseInt(liniaKonta[1]);
                                int pinKonta = Integer.parseInt(liniaKonta[2]);
                                long srodkiNaKoncie = 0;
                                BufferedReader skarb = new BufferedReader(new FileReader("skarbiec.txt"));
                                String sss;
                                while ((sss = skarb.readLine()) != null) {
                                    String tabbb[] = sss.split(";");
                                    if (tabbb[0].equals(numerKonta)) {
                                        srodkiNaKoncie = Long.parseLong(tabbb[1]);
                                    }
                                }
                                Konto konto = new Konto(klient, pinKonta, true, Long.parseLong(numerKonta), srodkiNaKoncie);

                            }
                            i--;
                        }

                        //

                        okno(klient, bank, frame);
                        frame.dispose();

                    } else info.setText("Nieprawidlowe dane");

                }

            } catch (IOException | NullPointerException | NumberFormatException x) {
                info.setText("Nieprawidlowy format danych");
            }
        });

        JButton wroc = new JButton("Wroc");
        wroc.setBounds(300, 270, 200, 60);
        frame.add(wroc);
        wroc.addActionListener(e -> {
            frame.dispose();
            try {
                program(bank, frame);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }


    public static void main(String[] args) throws IOException {

        Bank mbank = new Bank();
        
        program(mbank, new JFrame());
        program(mbank, new JFrame());

    }
}
