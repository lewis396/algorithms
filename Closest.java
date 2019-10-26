import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Closest {

	
    static class Point{    	
    	/*
    	 * A class describing a point in the Euclidean plane.
    	 *
    	 * */
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }
    
    
    private static double distance(Point p, Point q) {
    	/*
    	 * Computes the standard Euclidean distance between two points.
    	 * 
    	 * */
    	return sqrt(pow((p.x - q.x), 2) 
				+ pow((p.y - q.y), 2));
    }
    
    
    private static double bruteForceMinimalDistance(ArrayList<Point> points) {  
    	/*
    	 * Computes the minimal pairwise distance between points in an array
    	 * Does this in polynomial time
    	 * 
    	 * */
    	double res = Double.POSITIVE_INFINITY;
    	int end = points.size();
    	for(int i=0; i<end; i++) {
    		for(int j=i+1; j<end; j++) {
    			double dist = distance(points.get(i),points.get(j));
    			if(dist < res) {
    				res = dist;
    			}
    		}
    	}
    	return res;
    }
    
    
    private static ArrayList<Point> filter(ArrayList<Point> all_points, int mid_idx, double dist){
    	/*
    	 * Returns an arraylist of points within dist of some point (mid_idx)
    	 * 
    	 * */
    	ArrayList<Point> filtered_points = new ArrayList<Point>();
    	double left_edge = all_points.get(mid_idx).x - dist;
    	double right_edge = all_points.get(mid_idx).x + dist;
    	
    	int total = all_points.size();
    	for (int i = 0; i<total; i++){
    		Point p = all_points.get(i);
			if (left_edge < p.x){
				filtered_points.add(p);
			}
			if(p.x > right_edge) {
				break;
			}
		}
    	return filtered_points;
    }
    
    private static double withinStripMinimalDistance(ArrayList<Point> all_points, 
    		int mid_idx, double distance) {
    	/*
    	 * Computes the minimal pairwise distance between points in the infinite
    	 * vertical strip with boundaries at mid_idx-dist and mid_idx+dist.
    	 * It may be shown that it is sufficient to consider only eight
    	 * neighbouring points. 
    	 * 
    	 * */
    	double ans = distance;
     	ArrayList<Point> subset = filter(all_points, mid_idx, distance);
     	int size = subset.size();
    	
     	// Sort the subset by y-coordinates
    	Comparator<Point> yComparator = new Comparator<Point>(){
            @Override
            public int compare(final Point l, final Point r){
                return Double.valueOf(l.y).compareTo(Double.valueOf(r.y));
            }
        };
        Collections.sort(subset, yComparator);
        
        // Check the eight neighbouring points and return the minimum distance
        for(int i=0; i<size; i++) {
        	for(int j=i+1; j<min(i+7,size); j++) {
        		Point p = subset.get(i), q = subset.get(j);
        		double d = distance(p,q);
        		if(d < ans){
        			ans =  d;
        		}
        	}
        }
    	return ans;
    }

    
    static double minimalDistance(ArrayList<Point> all_points) {
    	/*
    	 * This function computes the minimal pairwise distance in a set of points in the
    	 * Euclidean plane in O(n logn) time complexity.
    	 * The procedure is
    	 * 
    	 * 		1. Order the points in terms of x-coordinates
    	 * 		2. Compute the midpoint of the ordered points and induce
    	 * 		   a partition.
    	 * 		3. Compute the minimum distance on either side of the midpoint via
    	 * 		   recursive calls of this function.
    	 * 		4. Take the minimum(d) of the two minima.
    	 * 		5. Consider a strip of width 2d centred around the midpoint.
    	 * 		6. Find the minimum pairwise distance for the points in the strip.
    	 * 		7. Return the overall minimum. 
    	 * 
    	 */
  
        double ans = Double.POSITIVE_INFINITY;
        int n = all_points.size();
        if(n<4) {
        	// In the case of three or less points, use brute force
        	return bruteForceMinimalDistance(all_points);
        }
        
        // Sort Points by x-coordinate
        Comparator<Point> xComparator = new Comparator<Point>(){
            @Override
            public int compare(final Point l, final Point r){
                return Double.valueOf(l.x).compareTo(Double.valueOf(r.x));
            }
        };
        Collections.sort(all_points, xComparator);
        
        // Find the midpoint, and partition the points
        int mid_idx = (int)(n/2);
        ArrayList<Point> left_points = new ArrayList<Point>();
        ArrayList<Point> right_points = new ArrayList<Point>(); 
        for(int i=0; i<mid_idx; i++) {
        	left_points.add(all_points.get(i));
        }
        for(int i=mid_idx; i<n; i++) {
        	right_points.add(all_points.get(i));
        }
        
        // Recursively call this function on both sets of points
        double min1 = minimalDistance(left_points); 
        double min2 = minimalDistance(right_points); 
        
        // Min of mins
        ans = min(min1, min2);
        
        // Find minimum in the central strip
        double min_within = withinStripMinimalDistance(all_points, mid_idx, ans);    
        
        // Return the overall minimum
        return min(ans, min_within);

    }
    
    

    public static void main(String[] args) throws Exception {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < n; i++) {
            points.add(new Point(nextInt(), nextInt()));
        }
        System.out.println(minimalDistance(points));
        writer.close();
    }

    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");


    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }
}
