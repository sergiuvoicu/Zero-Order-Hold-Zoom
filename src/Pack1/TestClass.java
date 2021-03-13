package Pack1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import Pack2.*;

// Clasa in care sunt introduse date si aplicatia este testata
public class TestClass {


	public static void main(String[] args) {
		
		// Daca programul se executa din linia de comanda si are destule argumente
		if(args.length == 4){
			
			// Variabila pentru marcarea inceperii inregistrarii timpului de executie a citirii informatiilor despre sursa si destinatie
			long startTime = System.nanoTime();
			
			// Buffer folosit pentru sincronizarea threadurilor
			Buffer buff = new Buffer();
			
			// Path-ul catre fisierul sursa
			String srcPath = args[0];
			
			// Path-ul catre fisierul destinatie
			String dstPath = args[1];
			
			// Factorul de zoom 
			int factor = Integer.parseInt(args[2]);
			
			// Modul de zoom (In/Out - True/False)
			boolean mode = Boolean.parseBoolean(args[3]);
			
			// Variabila folosita pentru marcarea terminarii inregistrarii timpului de executie a citirii informatiilor despre sursa si destinatie
			long endTime = System.nanoTime();
			
			// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde
			// (/1000000)
			long duration = (endTime - startTime)/1000000;
			
			// Afisarea timpului de executie pentru citirea informatiilor despre sursa si destinatie
			System.out.println("Source and destination information: "+duration+"ms");
			
			// Thread Producer: argumente: tip de thread (true-Producer;false-Consumer), buffer, String ce contine path-ul catre
			// fisierul sursa
			Producer p = new Producer(true, buff, srcPath);
			
			// Thread Consumer: argumente: tip de thread (true-Producer;false-Consumer), buffer, String ce contine path-ul catre
			// fisierul destinatie, Int ce determina factorul de multiplicare al algoritmului (de cate ori se da zoom), 
			// Boolean ce determina tipul de zoom (in-true;out-false)
			Consumer c = new Consumer(false, buff, dstPath,factor,mode);
			
			// Pornire Producer
			p.start();
			
			// Pornire Consumer
			c.start();
		}
		// Bloc de instructiuni pentru inserarea informatiilor de catre utilizator, de la tastatura
		else{
			
			// Lista tip coada pentru introducerea mai multor fisiere sursa
			Queue<String> sourceQueue = new LinkedList<String>();
			
			// Lista tip coada pentru introducerea mai multor fisiere destinatie
			Queue<String> destQueue = new LinkedList<String>();
			
			// String ce inregistreaza inputul utilizatorului pentru fisier sursa
			String sourceInput = new String("");
			
			// Counter pentru a retine cate fisiere sursa au fost introduse
			int counterSrc = 0;
			
			// Counter pentru a retine cate fisiere destinatie au fost introduse
			int counterDst = 0;
			
			// Variabila pentru marcarea inceperii inregistrarii timpului de executie a citirii informatiilor despre sursa
			long startTime = System.nanoTime();
			
			// Variabila scanner pentru a prelua inputul utilizator, de la tastatura
			Scanner scan = new Scanner(System.in);
			
			// Mesaj pentru a instiinta in legatura cu conditia de oprire
			System.out.println("Insert source images' paths, insert 'stop' to move further");
			
			// Introducerea fisierelor sursa pana se introduce 'stop'
			do {
				// Preluarea inputului de la utilizator
				sourceInput = scan.nextLine();
				
				// Conditie ca inputul sa nu ia valoarea stop
				if(!sourceInput.equals("stop")){
					
				// Adaugarea inputului la lista
					sourceQueue.add(sourceInput);
					
				// Incrementarea numarului de surse
					counterSrc++;
				}
			} while (!sourceInput.equals("stop"));
			
			// Variabila folosita pentru marcarea terminarii inregistrarii timpului de executie a citirii informatiilor despre sursa 
			long endTime = System.nanoTime();
			
			// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde (/1000000)
			long duration = (endTime - startTime)/1000000;
			
			// Afisarea timpului de executie pentru citirea informatiilor despre sursa
			System.out.println("Source information: "+duration+"ms");
			
			// Variabila pentru marcarea inceperii inregistrarii timpului de executie a citirii informatiilor despre destinatie
			startTime = System.nanoTime();
			
			// Mesaj pentru a instiinta in legatura cu numarul de destinatii ce trebuie introdus
			System.out.println("You have inserted " + counterSrc + " images, insert as many destination images' paths, zoom factors(powers of two) and modes(in/out)");
			
			// Cat timp sunt mai putine destinatii decat surse, se introduc destinatii
			while(counterDst != counterSrc){
				
				// Mesaj pentru a instiinta in legatura cu numarul de seturi de informatii ramas
				System.out.println("Insert " + (counterSrc-counterDst) + " more set(s) of information");
				
				// Mesaj pentru adaugarea Stringului de destinatie si adaugarea lui in lista
				System.out.print("Destination: ");
				destQueue.add(scan.nextLine());
				
				// Mesaj pentru adaugarea factorului de zoom si adaugarea lui in lista
				System.out.print("Zoom factor: ");
				destQueue.add(scan.nextLine());
				
				// Mesaj pentru adaugarea modului de zoom si adaugarea lui in lista
				System.out.print("Zoom In/Out (true/false): ");
				destQueue.add(scan.nextLine());
				
				// Incrementarea numarului de fisiere destinatie
				counterDst++;
			}
			// Variabila folosita pentru marcarea terminarii inregistrarii timpului de executie a citirii informatiilor despre destinatie
			endTime = System.nanoTime();
			
			// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde (/1000000)
			duration = (endTime - startTime)/1000000;
			
			// Afisarea timpului de executie pentru citirea informatiilor despre destinatie
			System.out.println("Destination information: "+duration+"ms");
			
			// Inchiderea scannerului
			scan.close();
			
			// Initializarea Bufferului
			Buffer buff = new Buffer();
			
			// Initializarea fara parametri a Producerului
			Producer p = new Producer();
			
			// Initializarea fara parametri a Consumerului
			Consumer c = new Consumer();
			
			// Cat timp exista elemente in lista surselor, se initializeaza Threadurile cu variabilele din lista destinatie
			try{
				synchronized(sourceQueue){
					while(!sourceQueue.isEmpty())
					{
						System.out.println("");
						
						// Initializarea Producerului cu head-ul listei sursa (primul element introdus in lista)
						p = new Producer(true, buff, sourceQueue.remove());
						
						// Path-ul fisierului destinatie
						String destPath = destQueue.remove();
						
						// Factorul de zoom convertit la int
						Integer factor = Integer.parseInt(destQueue.remove());
						
						// Modul convertit la boolean
						Boolean mode = Boolean.parseBoolean(destQueue.remove());
						
						// Initializarea Consumerului cu parametri specifici, care sunt in ordine: Destinatie, factor, mod
						c = new Consumer(false, buff, destPath, factor, mode);
						
						// Pornire Producer
						p.start();
						
						// Pornire Consumer
						c.start();
						
						// Delay impus listei de surse, astfel incat imaginea ce ocupa Threadurile sa poata fi procesata si scrisa
						if(factor < 3) factor+=3-factor;
						sourceQueue.wait((long)Math.pow(2, factor)*1000);
					}
				}
			}
			// Tratarea posibile aparute la sincronizarea listei
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}	
		}

		
	  /*  // Initializarea Producerului si Consumerului cu valori hardcodate pentru a pune in evidenta usor toate functionalitatile introduse
		Buffer buff = new Buffer();
		Producer p = new Producer(true, buff, "butterfly.bmp");
		Consumer c = new Consumer(false, buff,"butterflytest.bmp",2,true);
		p.start();
		c.start(); */


	}

}
