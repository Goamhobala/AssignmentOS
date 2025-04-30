package barScheduling;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/*
A struct datatype to store timing information
*/
public class Timing {
    // Ready
    protected long arrivalTime;
    // Start 
    // protected long startTime;
    // First execution
    // protected long firstResponseTime;
    // Finished task
    protected long finishTime;
    protected ArrayList<Long> waitingStartTimes;
    protected ArrayList<Long> waitingEndTimes;

    public Timing(){
        this.waitingStartTimes = new ArrayList<Long>();
        this.waitingEndTimes = new ArrayList<Long>();
    }

    public void writeOut(String output){
        try{
        File file = new File(output);
        BufferedWriter fr = new BufferedWriter(new FileWriter(file, true));
        fr.write(String.format("%d, %d, [%s, %s]\n", arrivalTime, finishTime, waitingStartTimes.toString(), waitingEndTimes.toString()));
        fr.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}