//M. M. Kuttel 2025 mkuttel@gmail.com
package barScheduling;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/*
 Barman Thread class.
 */

public class Barman extends Thread {
	

	private CountDownLatch startSignal;
	private BlockingQueue<DrinkOrder> orderQueue;
	int schedAlg =0;
	int q=10000; //really big if not set, so FCFS
	private int switchTime;
	private Timing timeData;
	
	
	Barman(  CountDownLatch startSignal,int sAlg) {
		//which scheduling algorithm to use
		this.schedAlg=sAlg;
		this.timeData = new Timing();
		if (schedAlg==1) this.orderQueue = new PriorityBlockingQueue<>(5000, Comparator.comparingInt(DrinkOrder::getExecutionTime)); //SJF
		else this.orderQueue = new LinkedBlockingQueue<>(); //FCFS & RR
	    this.startSignal=startSignal;
	}
	
	Barman(  CountDownLatch startSignal,int sAlg,int quantum, int sTime) { //overloading constructor for RR which needs q
		this(startSignal, sAlg);
		q=quantum;
		switchTime=sTime;
	}

	public void placeDrinkOrder(DrinkOrder order) throws InterruptedException {
        orderQueue.put(order);
    }
	
 @Override
	public void run() {
		int interrupts=0;
		try {
			DrinkOrder currentOrder;
			// arrival time
			startSignal.countDown(); //barman ready
			;

			// start time
			//check latch - don't start until told to do so 
			startSignal.await();
			timeData.arrivalTime = System.nanoTime();

			// boolean first = true;
			int processCounter = 0;
			if ((schedAlg==0)||(schedAlg==1)) { //FCFS and non-preemptive SJF
				while(true) {
					currentOrder=orderQueue.take();
					
					timeData.waitingStartTimes.add(System.nanoTime());
					System.out.println("---Barman preparing drink for patron "+ currentOrder.toString());
					
					sleep(currentOrder.getExecutionTime()); //processing order (="CPU burst")
					// if (first){
					// 	// first response
					// 	timeData.firstResponseTime = System.nanoTime();
					// 	first = false;
					// }
					timeData.waitingEndTimes.add(System.nanoTime());
					System.out.println("---Barman has made drink for patron "+ currentOrder.toString());
					currentOrder.orderDone();
					sleep(switchTime);//cost for switching orders
				}
			}
			else { // RR 
				int burst=0;
				int timeLeft=0;
				System.out.println("---Barman started with q= "+q);

				while(true) {
					System.out.println("---Barman waiting for next order ");
					// if (first) timeData.startTime = System.nanoTime();
					currentOrder=orderQueue.take();
					timeData.waitingStartTimes.add(System.nanoTime());

					System.out.println("---Barman preparing drink for patron "+ currentOrder.toString() );
					burst=currentOrder.getExecutionTime();
					if(burst<=q) { //within the quantum
						sleep(burst); //processing complete order ="CPU burst"
						System.out.println("---Barman has made drink for patron "+ currentOrder.toString());
						currentOrder.orderDone();
					}
					else {
						sleep(q);
						timeLeft=burst-q;
						System.out.println("--INTERRUPT---preparation of drink for patron "+ currentOrder.toString()+ " time left=" + timeLeft);
						interrupts++;
						currentOrder.setRemainingPreparationTime(timeLeft);
						orderQueue.put(currentOrder); //put back on queue at end
					}
					sleep(switchTime);//switching orders
				}
			}
		
				
		} catch (InterruptedException e1) {
			System.out.println("---Barman is packing up ");
			System.out.println("---number interrupts="+interrupts);
			timeData.finishTime = System.nanoTime();
		}
		timeData.writeOut("./data/barman.csv");
	}
}


