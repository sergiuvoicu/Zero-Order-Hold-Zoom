package Pack2;

// Clasa ce extinde clasa abstracta AbstractThreadClass
public class ThreadClass extends AbstractThreadClass {
	
	// Numele threadului (Producer/Consumer)
	private String name;
	
	// Constructor fara parametri
	public ThreadClass(){}
	
	// Constructor cu parametri- type (Producer/Consumer)
	public ThreadClass(boolean type){
		
	//  Numele este atribuit Threadului in functie de tip - true = Producer, false = Consumer
		this.name = type ? "Producer" : "Consumer";
		
	//  Afisarea Constructorului specific
	//	System.out.println("Constructor " + this.name);
		
	}
	
	// Metoda start mostenita din clasa abstracta
	public void start(){ 
		
	//  Afisarea pornirii Threadului specific	
	//	System.out.println("Start " + this.name);
		
	//  Apelarea metodei start din clasa parinte
		super.start();
	}
	
	// Metoda run implementata din clasa abstracta
	public void run(){
		
	//  Afisarea inceperii rularii Threadului specific
	//	System.out.println("Run " + this.name);
	}
	
	// Getter al numelui Threadului
	public String getThreadName(){
		return this.name;
	}
	

}
