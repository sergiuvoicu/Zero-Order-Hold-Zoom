package Pack2;

// Clasa abstracta ce extinde clasa Thread
abstract public class AbstractThreadClass extends Thread{
	
	// Metoda non-abstracta start, ce apeleaza metoda start parinte
	public void start(){
		super.start();
	}
	
	// Metoda abstracta run
	abstract public void run();
}
