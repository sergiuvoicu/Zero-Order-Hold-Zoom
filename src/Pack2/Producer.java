package Pack2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// Clasa Producer, extinde clasa parinte ThreadClass si implementeaza interfata RunnableClass
public class Producer extends ThreadClass implements RunnableClass  {
	
	// Initializarea campurilor private
	
	// Bufferul pentru sincronizare
	private Buffer buff = null;
	
	// Imaginea citita
	private BufferedImage image = null;
	
	// Inaltimea imaginii
	private static int height = -1;
	
	// Latimea imaginii
	private static int width = -1;
	
	// Path-ul fisiserului sursa
	private String path = null;

	// Constructor fara parametri
	public Producer(){}
	
	// Constructor cu parametri: type- tipul de Thread(Producer/Consumer), mostenit de la ThreadClass, Buffer, path-ul catre fisierul sursa
	public Producer(boolean type, Buffer buff, String path){
		
		// Apelarea constructorului parinte
		super(type);
		
		// Atribuirea parametrilor catre campurile clasei
		this.buff = buff;
		this.path = path;
		
	}
	
	// Metoda start, mostenita din ThreadClass, implementata din interfata RunnableClass, suprascrisa
	@Override
	public void start(){
		// Variabila pentru marcarea inceperii inregistrarii timpului de executie a citirii sursei
		long startTime = System.nanoTime();

		try {
			// Citirea imaginii din fisierul de la locatia indicata de path-ul primit
			image = ImageIO.read(new File(path));
			
			// Dimensiunile imaginii
			height = image.getHeight();
			width = image.getWidth();
			
		// Tratarea exceptiilor ce pot aparea la citirea fisierului
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		// Apelarea metodei start din clasa parinte ThreadClass
		super.start();
		
		// Variabila pentru marcarea terminarii inregistrarii timpului de executie a citirii sursei
		long endTime = System.nanoTime();
		
		// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde(/1000000)
		long duration = (endTime - startTime)/1000000;
		
		// Afisarea timpului de executie al citirii sursei
		System.out.println("Source reading: "+duration+"ms");
		
	}
	
	// Metoda run, mostenita din ThreadClass, implementata din interfata RunnableClass, suprascrisa
	@Override
	public void run(){
		
		// Apelarea metodei run din clasa parinte ThreadClass
		super.run();
		
		// Array de dimensiunea unui sfert din pixelii imaginii
		int[] pixelArray = new int[((height*width)/4)];
		
		// Variabila ce contorizeaza al catelea sfert de imagine este transmis
		int step = 0;
		
		// Variabila ce contorizeaza pozitia din array
		int counter = 0;
		
		// Pentru fiecare coloana a imaginii
		for(int i = 0; i < width; i++)
			
			// Pentru fiecare rand a imaginii
			for(int j = 0; j < height; j++)
			{
				// Daca pozitia din array nu a ajuns la un sfert din numarul de pixeli ai imaginii
				if(counter<(height*width)/4)
					
					// Pozitia din array ia valoarea pixelului (pe 24 de biti, codul RGB fiind convertit in INT) curent al imaginii
					// Pozitia din array se incrementeaza dupa atribuire
					pixelArray[counter++]= image.getRGB(i, j);
				
				// Daca pozitia a ajuns la sfertul numarului de pixeli
				else{
					// Incrementarea regiunii
					step++;
					
					// Afisarea regiunii
					System.out.println("Producer step "+step+"/4");
					
					// Plasarea array-ului in buffer 
					buff.put(pixelArray);
					
					// Resetarea pozitiei din array
					counter = 0;
					
					// Intarzierea Thread-ului cu o secunda si tratarea posibilelor exceptii
					try {
						sleep (1000);
					} catch (InterruptedException e){
						e.printStackTrace();
					}
					
				}
			}
		
		// La iesirea din for-uri, array-ul este populat cu ultimul sfert din imagine
		// Incrementarea regiunii
		step++;
		
		// Afisarea regiunii
		System.out.println("Producer step "+step+"/4");
		
		// Punerea array-ului pe buffer
		buff.put(pixelArray);
		
		// Resetarea counterului
		counter = 0;
		
		// Intarzierea Thread-ului cu o secunda si tratarea posibilelor exceptii
		try {
			sleep (1000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
	//  Afisaj ce semnaleaza inchiderea Threadului	
	//	System.out.println("Going dead "+ super.getThreadName());
		
	}
	
	// Getter pentru latimea imaginii
	public static int getWidth(){
		return width;
	}
	
	// Getter pentru inaltimea imaginii
	public static int getHeight(){
		return height;
	}
}



