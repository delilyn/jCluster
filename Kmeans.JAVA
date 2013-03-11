package edu.lsus.streetman.kmeans;
/*****************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

This class is a generic D-attibute K-means algorithm.

Required Input: <string filename> <int K> <int I>
*****************************************************************************************************************/
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Kmeans {

	private static int K;			//The user-specified number of clusters
	private static int I;			//The user-specified number of iterations to run
	private static int N;			//The file-specified number of points
	private static int D;			//The file-specified number of attributes
	private static double totalSSE;	//Global holder for the total SSE of clustering
	private static String fName;	//The user-specified input file name
	private static Clustering c1;	//A collection of clusters, each with a centroid and points
	private static Clustering c2;	//A second external criteria to compare the first to.
	private static ArrayList< double[] > dPoints;	//The current data points to be clustered
	private static ArrayList< double[] > raw;		//Raw data points parsed from file
	private static ArrayList< int[] > randomInit;	//Indices of initial centroids. Each double array is the list of random points to be the initial centroids
	
	/***************************************************************************************************************
	Method: main
	Description: This method currently coordinates all aspects of the k-means code. It is being migrated to a more
	OOP-friendly format, in the form of an object with portable methods. It will allow the programmer to add the
	k-means class to sort any list of double, float, int, and long values into any number of clusters, iteratively.
	***************************************************************************************************************/
	public static void main( String[] args ) {
		
		fName = args[ 0 ];					
		K = Integer.parseInt( args[ 1 ] );	
		I = Integer.parseInt( args[ 2 ] );	
		int[] lbl = null;					//This array holds the ordered list of class labels
		raw = new ArrayList< double[] >();	
		//Parse the data from text into Point objects
		FileReader file = null;			
		Scanner scan;					
		try {
			file = new FileReader( fName );	
			scan = new Scanner( file );		
			scan.useDelimiter( "\n" );		
			while( scan.hasNext() ) {
				String[] param = scan.next().trim().split( "\\s" );
				N = Integer.parseInt( param[ 0 ] );			
				D = Integer.parseInt( param[ 1 ] ) - 1;		
				K = Integer.parseInt( param[ 2 ].trim() );	
				//Variables to pull class labels from file
				int count = 0;
				lbl = new int[ N ];
				//Read the rest of the lines as points
				while( scan.hasNext() ) {
					String[] c = scan.next().trim().split( "[\\s]+" );	
					double[] coords = new double[ D ];					
					for( int i = 0; i < D; i++ ) {
						coords[ i ] = Double.parseDouble( c[ i ] );
					}
					lbl[ count ] = Integer.parseInt( c[ D ].trim() );	//Save the class label
					raw.add( coords );
					count ++;			
				}
			}
			initPoints();
			//Create external criteria
			c2 = new Clustering( N );
			c2.createLabel( lbl );		//This 'clustering' only contains the order class labels from file
			System.out.println( "K-Means Algorithm Results\nNumber of Clusters: " + K + "\tNumber of Iterations: "
					+ I + "\nNumber of Points: " + N + "\tDimensionality: " + D + "\n" );
			//Do 10 times: For each run, cluster each normalization method on same random initial centroids
			for( int i = 0; i < 10; i++ ) {
				//Cluster with raw data
				dPoints = raw;
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Raw Final Total SSE: " + totalSSE );
				System.out.println( "Raw ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.minMax( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "MinMax Final Total SSE: " + totalSSE );
				System.out.println( "MinMax ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.zScore( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Z-Score Final Total SSE: " + totalSSE );
				System.out.println( "Z-Score ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.rankedNorm( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Ranked Final Total SSE: " + totalSSE );
				System.out.println( "Ranked ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.maxNorm( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Max-Norm Final Total SSE: " + totalSSE );
				System.out.println( "Max-Norm ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.rangeNorm( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Range Final Total SSE: " + totalSSE );
				System.out.println( "Range ARI: " + aRI( c1, c2 ) );
				//Cluster with normalized data
				dPoints = Normalize.euclidNorm( raw );
				c1 = new Clustering( N );
				cluster( c1, i );
				//Print output
				System.out.println( "Euclidean Norm Final Total SSE: " + totalSSE );
				System.out.println( "Euclidean Norm ARI: " + aRI( c1, c2 ) + "\n" );
			}
		}
		catch( IOException e ) {
			System.out.println( "There was an error parsing! " + e.getMessage() );
		}
		finally {
			if( file != null ) {
				try {
					file.close();
				}
				catch( IOException e ) {
					System.out.println( "There was an error closing file! " + e.getMessage() );
				}
			}
		}
	}
	
	/***************************************************************************************************************
	Method: cluster
	Description: This method will create a clustering and return it. It must know which run this is, to properly
	seed centroids.
	***************************************************************************************************************/
	private static void cluster( Clustering c, int run ) {
		initCentroids( c, run );
		//Start clustering
		for( int iter = 0; iter < I; iter++ ) {
			for( int i = 0; i < K; i++ ) {
				c.getCluster( i ).clearPoints();
			}
			assignPoints( c );
			double sum = 0.0;
			for( int i = 0; i < K; i++ ) {
				sum += sSE( c.getCluster( i ) );	
				updateCentroid( c.getCluster( i ) );
			}
			totalSSE = sum;
		}
	}
	
	/***************************************************************************************************************
	Method: assignPoints
	Description: This method will assign a data point to the cluster of the closest centroid. It will go through 
	each point, calculate the Euclidean distance between that point and every centroid, and then assign the point to 
	that cluster.
	***************************************************************************************************************/
	private static void assignPoints( Clustering c ) {
		for( int i = 0; i < N; i ++ ) {
			double dist;
			double min = euclidDist( dPoints.get( i ),c.getCluster( 0 ).centroid() );
			int mIndex = 0;	
			for( int j = 1; j < K; j++ ) {
				dist = euclidDist( dPoints.get( i ), c.getCluster( j ).centroid() );
				if( dist < min ) {
					mIndex = j;		
					min = dist;    
				}
			}
			c.getCluster( mIndex ).addPoint( dPoints.get( i ) );//Add the point to the closest centroid
			c.setLabel( i, mIndex );							//Add the class label to the clustering
		}
	}
	
	/***************************************************************************************************************
	Method: initCentroids
	Description: This method will initialize the k centroids from randomly selected points. Points have been 
	selected previously, so this method must know which run to use.
	***************************************************************************************************************/
	private static void initCentroids( Clustering c, int run ) {
		//Seed each cluster with an initial centroid
		for( int i = 0; i < K; i++ ) {
			//This will 1) select a random int in the range of the list of data points, 2) get the data point at
			//that index, 3) creates a new cluster with that point as the centroid, 4) and adds that cluster to
			//a list
			c.addCluster( new Cluster ( dPoints.get( randomInit.get( run )[ i ] ) ) );
		}		
	}
	
	/***************************************************************************************************************
	Method: updateCentroid
	Description: This method will calculate the new centroid of the cluster by averaging the values of each
	attribute of the all the points in the cluster. It will then set this new point as the current centroid, and 
	clear the list of points in the cluster.
	***************************************************************************************************************/
	private static void updateCentroid( Cluster c ) {
		int m = c.size();					//Number of points in the cluster
		double sum;							//Total of values of each attribute d
		double[] newCoord = new double[ D ];//This array represents the coordinates of the new centroid
		for( int i = 0; i < D; i++ ) {
			sum = 0.0;
			for( int j = 0; j < m; j++ ) {
				sum += c.point( j )[ i ];	//This should give i-th coord of the j-th point
			}
			newCoord[ i ] = ( sum / m );	//Then average the values of that attribute across all points
		}
		c.newCentroid( newCoord );	//Update current centroid
	}
	
	/****************************************************************************************************************
	Method: euclidDist
	Description: This method will accept two points and return their Euclidean distance in the form of a double. It 
	assumes both points have coordinates equal in length.
	****************************************************************************************************************/
	private static double euclidDist( double[] a, double[] b ) {
		double sum = 0.0;
		for( int i = 0; i < D; i++ ) {
			double x = a[ i ] - b[ i ];
			sum += x * x;				//(x1-x2)^2,(y1-y2)^2,...
		}
		return sum;
	}
	
	/****************************************************************************************************************
	Method: sSE
	Description: This method will take a cluster and measure its SSE. The SSE in the cluster is the total error, or 
	distance from the centroid, of each point in the cluster.
	****************************************************************************************************************/
	private static double sSE( Cluster c ) {
		double sse = 0.0;
		for( int i = 0; i < c.size(); i ++ ) {
			sse += euclidDist( c.point( i ), c.centroid() );	//Add the distance from point to centroid
		}
		return sse;
	}
	
	/****************************************************************************************************************
	Method: aRI
	Description: This method will take accept two clusterings and determine the adjusted Rand Index, which is the
	accuracy between a labeled clustering and a clustering resulting from k-means. The higher the number, the more
	similar the clusterings.
	****************************************************************************************************************/
	private static double aRI( Clustering c1, Clustering c2 ) {
		int a = 0;	
		int b = 0;	
		int c = 0;	
		int n = 0;	//Corresponds to n: sum of the sum of matches in each row
		double d;	
		int count = dPoints.size();				
		int[] rowSum = new int[ K ];			
		int[] colSum = new int[ K ];
		int[][] contingency = new int[ K ][ K ];//Contingency table
		//Calculate the contingency table for two clusterings
		for( int i = 0; i < count; i++ ) {
			contingency[ c1.label( i ) ][ c2.label( i ) ] += 1;
		}
		//Find the sums of rows and columns of the contingency table
		for( int i = 0; i < K; i++ ) {
			for( int j = 0; j < K; j++ ) {
				int tmp = contingency[ i ][ j ];
				colSum[ i ] += tmp;	
				rowSum[ j ] += tmp;	
				a += tmp * tmp;
			}
		}
		//Calculate variables a-d.
		for( int i = 0; i < K; i++ ) {
			b += ( rowSum[ i ] * ( rowSum[ i ] - 1 ) ) / 2;
			n += rowSum[ i ];
			c += ( colSum[ i ] * ( colSum[ i ] - 1 ) ) / 2;	
		}
		a = ( a - n ) / 2;
		b = b - a;	
		c = c - a;	
		double n2 = ( n * ( n - 1 ) ) / 2;	
		d = n2 - a - b - c;					
		//Calculate the numerator for the adjusted Rand index
		double ariNum = ( n2 * ( a + d ) ) - ( ( a + b ) * ( a + c ) + ( c + d ) * ( b + d ) ); 
		//Calculate the denominator for the adjusted Rand index
		double ariDenom = ( n2 * n2 ) - ( ( a + b ) * ( a + c ) + ( c + d ) * ( b + d ) );
		return ariNum / ariDenom;
	}
	
	/****************************************************************************************************************
	Method: initPoints
	Description: This method create a table of 10 lists of k randomly selected points to be initial centroids, so 
	same initial conditions can be used between normalization methods.
	****************************************************************************************************************/
	private static void initPoints() {
		Random rand;							
		randomInit = new ArrayList< int[] >();	//10 int arrays, each integer is the index of a point for centroid
		//Create table
		for( int i = 0; i < 10; i++ ) {
			rand = new Random();		
			int[] run = new int[ K ];	
			for( int c= 0; c < K; c++ ) {
				run[ c ] = rand.nextInt( raw.size() );	//Pick a point for the initial centroid
			}
			randomInit.add( run );	
		}
	}
}
