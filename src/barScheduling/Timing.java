package barScheduling;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*
A struct datatype to store timing information
*/
public class Timing {
    // Ready
    protected long arrivalTime;
    // Start 
    protected long startTime;
    // First execution
    protected long firstResponseTime;
    // Finished task
    protected long finishTime;

    public void writeOut(String output){
        try{
        File file = new File(output);
        BufferedWriter fr = new BufferedWriter(new FileWriter(file, true));
        fr.write(String.format("%d, %d, %d, %d\n", arrivalTime, startTime, firstResponseTime, finishTime));
        fr.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}