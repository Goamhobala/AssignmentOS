//M. M. Kuttel 2025 mkuttel@gmail.com
package barScheduling;


import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*class for the patrons at the bar*/

public class Patron extends Thread {
	
	private Random random;// for variation in Patron behaviour
	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int numberOfDrinks;
	private Timing timeData;

	private DrinkOrder [] drinksOrder;
	
	Patron( int ID,  CountDownLatch startSignal, Barman aBarman, long seed) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		this.numberOfDrinks=5; // number of drinks is fixed
		this.timeData = new Timing();
		drinksOrder=new DrinkOrder[numberOfDrinks];
		if (seed>0) random = new Random(seed);// for consistent Patron behaviour
		else random = new Random();
	}
	
	
//this is what the threads do	
 @Override
	public void run() {
		try {
			
			//Do NOT change the block of code below - this is the arrival times
			// arrival time

			startSignal.countDown(); //this patron is ready
			timeData.arrivalTime = System.nanoTime();
			startSignal.await(); //wait till everyone is ready
			
			int arrivalTime = ID*50; //fixed arrival for testing
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			// start time
			timeData.arrivalTime = System.nanoTime();
			
			System.out.println("+new thirsty Patron "+ this.ID +" arrived"); //Patron has actually arrived
			//End do not change
			
	        for(int i=0;i<numberOfDrinks;i++) {

	        	drinksOrder[i]=new DrinkOrder(this.ID); //order a drink (=CPU burst)	        
	        	//drinksOrder[i]=new DrinkOrder(this.ID,i); //fixed drink order (=CPU burst), useful for testing
				System.out.println("Order placed by " + drinksOrder[i].toString()); //output in standard format  - do not change this
				theBarman.placeDrinkOrder(drinksOrder[i]);
				timeData.prepStartTimes.add(System.nanoTime());
				drinksOrder[i].waitForOrder();
				timeData.prepEndTimes.add(System.nanoTime());
				System.out.println("Drinking patron " + drinksOrder[i].toString());
				sleep(drinksOrder[i].getImbibingTime()); //drinking drink = "IO"
			}

			timeData.finishTime = System.nanoTime();
			timeData.writeOut(String.format("./data/patron%d.csv", this.ID));
			System.out.println("Patron "+ this.ID + " completed ");
			
		} catch (InterruptedException e1) {  //do nothing
		}
}
	public Timing getTimeData(){
		return this.timeData;
	}
}
	

