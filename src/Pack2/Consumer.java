package Pack2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

// Clasa Consumer ce extinde ThreadClass si reprezinta al 3-lea nivel de mostenire
public class Consumer extends ThreadClass {
	
	// Camp Buffer
	private Buffer buff;
	
	// Path-ul fisierului destinatie
	private String path;
	
	// Factorul de zoom
	private int factor;
	
	// Modul de zoom (true - zoomIn; false - zoomOut)
	private boolean mode;
	
	// Constructor fara parametri
	public Consumer(){}
	
	// Constructor cu parametri- tipul Threadului, bufferul, path-ul fisierului destinatie, factorul de zoom, modul de zoom 
	public Consumer(boolean type, Buffer buff, String path, int factor, boolean mode){
		
		// Apelarea constructorului din clasa parinte, in functie de tip
		super(type);
		
		// Atribuirea celorlalte campuri proprii
		this.buff = buff;
		this.path = path;
		this.factor = factor;
		this.mode = mode;
	}
	
	// Metoda start mostenita din clasa ThreadClass, suprascrisa
	@Override
	public void start(){
		
		// Apelarea metodei start din clasa parinte
		super.start();
	}
	
	// Metoda run mostenita din clasa ThreadClass, suprascrisa
	@Override
	public void run(){ 
		
		// Apelarea metodei run din clasa parinte
		super.run();
		
		// Variabila pentru marcarea inceperii inregistrarii timpului de executie a procesarii imaginii
		long startTime = System.nanoTime();
		
		// Array de pixeli ce primeste cate un sfert de imagine
		int[] pixelArrayTemp;
		
		// Array de pixeli ce primeste toti pixelii, de marimea pixelilor pozei+4 (din cauza decalajului creat de construirea vectorului sfert cu sfert
		int[] pixelArray = new int[Producer.getWidth()*Producer.getHeight()+4];
		
		// Pentru fiecare dintre cele 4 regiuni ale pozei
		for( int step = 0; step < 4; step++)
		{
			// Se afiseaza regiunea consumata
			System.out.println("Consumer step "+ (step+1) +"/4");
			
			// Se ia array-ul din Buffer
			pixelArrayTemp = buff.get();
			
			// Se copiaza array-ul ce contine sfertul de imagine, in array-ul total astfel:
			// Array-ul cu sfertul se copiaza de la pozitia 0 pana la capatul lui in array-ul total, de la pozitia
			// step*(lungimeArray)+step. Necesitatea +step pentru a nu suprascrie bitii, dar cauzeaza un decalaj de +1
			// la fiecare ciclu
			System.arraycopy(pixelArrayTemp, 0, pixelArray, step*(pixelArrayTemp.length)+step, pixelArrayTemp.length);
			
			// Intarzierea Thread-ului cu o secunda si tratarea posibilelor exceptii
			try {
				sleep (1000);
			} catch (InterruptedException e){}
			
		}
		
		// Crearea matricii de pixeli, trimitand ca parametri elementele array-ului de pixeli 
		int[][] pixelMatrix = toPixelMatrix(Arrays.copyOfRange(pixelArray, 0, pixelArray.length));
		
		// Variabila in care se stocheaza numarul de zoom-uri
		int cycles = 0;
		
		// Daca se executa zoom in
		if(mode){
			
			// Cat timp poza nu a fost marita de factor ori
			while(cycles!=factor){
				
				// Zoom in pe linii
				pixelMatrix = rowZoomIn(pixelMatrix);
				
				// Zoom in pe coloane
				pixelMatrix = colZoomIn(pixelMatrix);
				
				// Incrementarea numarului de cicli
				cycles++;
			}
		}
		
		// Daca se executa zoom out
		else{
			
			// Cat timp poza nu a fost micsorata de factor ori
			while(cycles!=factor){
				
				// Zoom out pe linii
				pixelMatrix = rowZoomOut(pixelMatrix);
				
				// Zoom out pe coloane
				pixelMatrix = colZoomOut(pixelMatrix);
				
				// Incrementarea numarului de cicli
				cycles++;
			}
		}
		
		// Crearea imaginii rezultate in urma procesarii din matricea de pixeli
		BufferedImage result = new BufferedImage(pixelMatrix.length,pixelMatrix[0].length,BufferedImage.TYPE_INT_RGB);
		
		// Atribuirea pixelilor matricii catre pixelii imaginii
		for(int j = 0; j < pixelMatrix[0].length; j++)
			for(int i = 0; i < pixelMatrix.length; i++)
				result.setRGB(i, j, pixelMatrix[i][j]);
		
		// Variabila pentru marcarea terminarii inregistrarii timpului de executie a procesarii imaginii
		long endTime = System.nanoTime();
		
		// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde(/1000000)
		long duration = (endTime - startTime)/1000000;
		
		// Afisarea timpului de executie al procesarii imaginii
		System.out.println("Image Processing: "+duration+"ms");
		
		// Scrierea imaginii in fisier
		writeImg(result);
		
	//  Afisaj ce semnaleaza inchiderea Threadului	 	
	//	System.out.println("Going dead "+ super.getThreadName());
	}
	
	// Metoda varargs ce primeste elementele unui array de pixeli si returneaza o matrice de pixeli
	private int[][] toPixelMatrix(int ... pixelArray){
		
		// Initializarea matricei
		int[][] pixelMatrix = new int [Producer.getWidth()][Producer.getHeight()];
		
		// Initializarea indexului linie al matricei
		int j = 0;
		
		// Initializarea indexului coloana al matricei
		int i = 0;
		
		// Pentru fiecare element
		for(int e:pixelArray)
		{
				// Matricea ia valoarea elementului
				pixelMatrix[i][j] = e;
				
				// Indexul coloana se incrementeaza
				j++;
				
				// Daca indexul coloana a ajuns la valoarea inaltimii matricei
				if(j == Producer.getHeight())
				{
					// Indexul coloana se reseteaza
					j = 0;
					
					// Daca indexul linie nu a ajuns la ultima linie
					if(i < Producer.getWidth()-1)
						
						// Indexul linie se incrementeaza
						i++;
				}
		}
		
		// Returneaza matricea de pixeli
		return pixelMatrix;
	}
	
	// Metoda de scriere a imaginii in fisier
	private void writeImg(BufferedImage result){
		
		// Variabila pentru marcarea inceperii inregistrarii timpului de executie a scrierii imaginii
		long startTime = System.nanoTime();
		
		// Scrierea imaginii in fisierul de la path-ul specificat
		try {
			ImageIO.write(result, "BMP", new File(path));
			
		// Tratarea posibilelor exceptii
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Variabila pentru marcarea terminarii inregistrarii timpului de executie a scrierii imaginii
		long endTime = System.nanoTime();
		
		// Variabila folosita pentru calculul diferentei dintre cele doua puncte (timpul de exceutie) si conversia in milisecunde(/1000000)
		long duration = (endTime - startTime)/1000000;
		
		// Afisarea timpului de executie al scrierii imaginii
		System.out.println("Image writing: "+duration+"ms");
	}
	
	// Metoda de zoom in pe linii
	private int[][] rowZoomIn(int[][] originalMatrix)
	{
		// Initializarea noii matrici
		int[][] pixelMatrix = new int [2*(originalMatrix.length)-1][originalMatrix[0].length];
		
		// Pentru fiecare coloana
		for(int j = 0; j < originalMatrix[0].length; j++)
			
			// Pentru fiecare linie
			for(int i = 0; i < originalMatrix.length-1; i++)
			{
				// Valoarea pixelului noii matrici cu index de linie par; ia valoarea curenta a matricii originale
				pixelMatrix[2*i][j] = originalMatrix[i][j];
				
				// Culoarea pixelului de pe pozitia curenta din matricea originala
				Color cP = new Color(originalMatrix[i][j]);
				
				// Culoarea pixelului de pe pozitia urmatoare a matricei original
				Color cF = new Color(originalMatrix[i+1][j]);
				
				// Culoarea pixelului noii matrici, care are culoarea mediei culorilor pixelilor alaturati din matricea original
				Color cM = new Color(((cP.getRed()+cF.getRed())/2),((cP.getGreen()+cF.getGreen())/2),((cP.getBlue()+cF.getBlue())/2));
				
				// Valoarea pixelului noii matrici cu index de linie impar; ia valoarea mediei pixelilor alaturati
				pixelMatrix[2*i+1][j] = cM.getRGB();
				
			}
		
		// Atribuirea ultimei linii- for-ul merge pana la penultima
		pixelMatrix[pixelMatrix.length-1] = originalMatrix[originalMatrix.length-1];
		
		// Returneaza matricea marita pe linii
		return pixelMatrix;
	}
	
	// Metoda de zoom in pe linii
	private int[][] colZoomIn(int[][] originalMatrix)
	{
		// Initializarea noii matrici
		int[][] pixelMatrix = new int [originalMatrix.length][2*(originalMatrix[0].length)-1];
		
		// Pentru fiecare linie
		for(int i = 0; i < originalMatrix.length; i++)
			
			// Pentru fiecare coloana
			for(int j = 0; j < originalMatrix[0].length-1; j++)
			{
				// Valoarea pixelului noii matrici cu index de coloana par; ia valoarea curenta a matricii originale
				pixelMatrix[i][2*j] = originalMatrix[i][j];
				
				// Culoarea pixelului de pe pozitia curenta din matricea originala
				Color cP = new Color(originalMatrix[i][j]);
				
				// Culoarea pixelului de pe pozitia urmatoare a matricei original
				Color cF = new Color(originalMatrix[i][j+1]);
				
				// Culoarea pixelului noii matrici, care are culoarea mediei culorilor pixelilor alaturati din matricea original
				Color cM = new Color(((cP.getRed()+cF.getRed())/2),((cP.getGreen()+cF.getGreen())/2),((cP.getBlue()+cF.getBlue())/2));
				
				// Valoarea pixelului noii matrici cu index de coloana impar; ia valoarea mediei pixelilor alaturati
				pixelMatrix[i][2*j+1] = cM.getRGB();
			}
		
		// Atribuirea ultimei coloane - for-ul merge pana la penultima
		for(int i = 0; i < originalMatrix.length; i++)
			pixelMatrix[i][pixelMatrix[0].length-1] = originalMatrix[i][originalMatrix[0].length-1];

		
		// Returneaza matricea marita pe coloane
		return pixelMatrix;
	}
	
	// Metoda de zoom out pe linii
	private int[][] rowZoomOut(int[][] originalMatrix)
	{
		// Initializarea noii matrici
		int[][] pixelMatrix = new int [(originalMatrix.length+1)/2][originalMatrix[0].length];
		
		// Pentru fiecare coloana
		for(int j = 0; j < originalMatrix[0].length; j++){
			
			// Contorizator al indexului linie al noii matrici
			int count = 0;
			
			// Pentru fiecare linie
			for(int i = 0; i < originalMatrix.length; i++)
			{
				// Daca indexul de linie din matricea original este par
				if(i%2 == 0){
					
					// Pixelul este atribuit noii matrici - se exclud pixelii cu indecsi de linie impari
					pixelMatrix[count][j] = originalMatrix[i][j];
					
					// Incrementarea indexului
					count++;
				}
			}
		}
		
		// Returneaza noua matrice
		return pixelMatrix;
	}
	
	// Metoda de zoom out pe coloane
	private int[][] colZoomOut(int[][] originalMatrix)
	{
		// Initializarea noii matrici
		int[][] pixelMatrix = new int [originalMatrix.length][(originalMatrix[0].length+1)/2];
		
		// Pentru fiecare linie
		for(int i = 0; i < originalMatrix.length; i++)
		{
			// Contorizator al indexului coloana al noii matrici
			int count = 0;
			
			// Pentru fiecare coloana
			for(int j = 0; j < originalMatrix[0].length; j++)
			{
				// Daca indexul de coloana din matricea original este par
				if(j%2==0){
					
					// Pixelul este atribuit noii matrici - se exclud pixelii cu indecsi de coloana impari
					pixelMatrix[i][count] = originalMatrix[i][j];
					
					// Incrementarea indexului
					count++;
				}
				
			}
		}
		
		// Returneaza noua matrice
		return pixelMatrix;
	}
	
}
