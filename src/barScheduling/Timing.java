package barScheduling;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*
A struct datatype to store timing information
*/
public class Timing {
    protected int patronID;
    // Ready
    protected long startTime;
    // Start 
    // protected long startTime;
    // First execution
    // protected long firstResponseTime;
    // Finished task
    protected long finishTime;
    protected long waitingTime;
    protected long lastRecordedTime;
    // protected ArrayList<Long> prepStartTimes;
    // protected ArrayList<Long> prepEndTimes;
    protected long firstResponseTime;

    public Timing(int id){
        this.patronID = id;
        // this.prepStartTimes = new ArrayList<Long>();
        // this.prepEndTimes = new ArrayList<Long>();
    }

    public void writeOut(String output){
        try{
        File file = new File(output);
        BufferedWriter fr = new BufferedWriter(new FileWriter(file, true));
        // fr.write(String.format("%d, %d, [%s, %s]\n", startTime, finishTime,);
        // test waiting time
        fr.write(String.format("%d\n", waitingTime));
        fr.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}