package Pack2;

// Clasa Buffer ce sincronizeaza Threadurile Producer si Consumer
public class Buffer {
	
	// Array de int-uri- contine pixelii unui sfert de imagine
	private int[] number;
	
	// Boolean ce transmite daca bufferul este ocupat
	private boolean available = true ;
	
	// Metoda de populare a Bufferului cu array-ul
	public synchronized void put (int[] number ) {
		
		// Cat timp nu a fost facut un get la buffer (bufferul este ocupat)
		while ( !available ) {
			// Threadul asteapta
				try {
					wait ();
			// Tratarea exceptiilor
				}catch (InterruptedException e){	
					e.printStackTrace(); 
				}
			}
		
		// Daca bufferul nu este ocupat, pe el se pune valoarea primita
			this.number = number;
			
		// Bufferul devine ocupat
			available = false;
			
		// Transmite celorlalte Threaduri starea bufferului
			notifyAll ();
		}
	
	// Metoda de depopulare a Bufferului
	public synchronized int[] get () {
		
		// Cat timp bufferul nu este ocupat
		while (available) {
			
				// Threadul asteapta
				try {
					wait ();
				// Tratarea exceptiilor
				}catch (InterruptedException e){ 
					e.printStackTrace(); 
				}
			}
		// Daca bufferul este ocupat, de pe el se ia valoarea
		
		// Bufferul devine liber
			available = true ;
			
		// Transmite celorlalte Threaduri starea bufferului
			notifyAll ();
			
		// Returneaza valoarea din Buffer, array-ul de pixeli
		return number ;
	}


}
