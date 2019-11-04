import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class JobQueue {
	
    private int numWorkers;                   // Number of available workers
    private int[] jobs;                       // Array of incoming jobs, m of these

    private int[] assignedWorker;             // Array tracking job allocations
    private long[] startTime;                 // Array 

    private Queue<Worker> status;             // A priority queue to keep track of threads 
    
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
    	/*
    	 * Read space separated integers representing...
    	 * 		1. n, The number of threads available.
    	 * 		2. m, The number of incoming jobs.
    	 * 		3. a series of m space separated integers representing
    	 * 		   run-times for m simulated jobs.
    	 */
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
        	jobs[i] = in.nextInt();
        }   
    }

    private void writeResponse() {
    	/*
    	 * Write line separated pairs of integers representing...
    	 * 		1. The id of the thread assigned the ith task.
    	 * 		2. The time at which work starts on the thread for the ith job.
    	 */
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }
    
    
    private void assignJobs() {
    	/*
    	 * Assign the jobs in the optimal manner among the threads using a priority queue 
    	 */
        assignedWorker = new int[jobs.length];                      
        startTime = new long[jobs.length];
        status = new PriorityQueue<Worker>(numWorkers, compareWorkers);       
        for (int i = 0; i < numWorkers; i++) {
        	status.add(new Worker(i,0));                 // label workers, none occupied yet
        }      
        for(int i = 0;i<jobs.length;i++){
            int duration = jobs[i];                      // get the duration
            Worker w = status.poll();                    // find the free worker
            assignedWorker[i] = w.id;                    // assign it 
            startTime[i] = w.occupied_until;
            w.processAndClear(duration);
            status.add(w);
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }
    
    static class Worker{
    	/*
    	 * A class that describes a worker by it's id natural integer label
    	 * and the next time at which it is unoccupied.
    	 */
    	
        private int id;
        public long occupied_until;
        
        Worker(int id,long t ){
            this.id = id;
            this.occupied_until = t;
        }
        
        private void processAndClear(long job_duration){
            this.occupied_until += job_duration;
        }

    }
    Comparator<Worker> compareWorkers = new Comparator<Worker>() {
        @Override
        public int compare(Worker u, Worker v) {
        	
        	if(u.occupied_until == v.occupied_until){
                if(u.id < v.id) { return -1; }
                else if(u.id > v.id) { return 1; }
                else { return 0; }
            }
            else if(u.occupied_until < v.occupied_until)
                return -1;
            else if(u.occupied_until > v.occupied_until)
                return 1;
            else
                return 0;
        }
    };
    

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
