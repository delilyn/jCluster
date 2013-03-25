package edu.lsus.streetman.kmeans;
/**********************************************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012 - Updated MAR 2013

This class is accepts an ArrayList< double[] > representing a list of data points, which it clusters with the k-means

Required Input: <string filename> <int K> <int I>
**********************************************************************************************************************************************/
import java.util.ArrayList;
import java.util.Random;

public class Kmeans {

	private int K;				//The user-specified number of clusters to form
	private int I;				//The number of iterations until the centroids stop moving
	private int N;				//The number of points in the data set
	private int D;				//The number of attributes, or dimensionality, of each point.
	private double totalSSE;	//Global holder for the total SSE of clustering
	private Clustering c1;		//A collection of clusters, each with a centroid and points
	private Clustering c2;		//A second external criteria to compare the first to.
	private ArrayList< double[] > dataPts;	//The list of data points to be clustered with Kmeans
	private ArrayList< double[] > raw;		//Raw data points parsed from file
	
	/******************************************************************************************************************************************
	Method: Constructor
	Description: This method accepts a data set in the form of an array list of double[], where each double[] holds
	the coordinates of a point.
	******************************************************************************************************************************************/
	public Kmeans( ArrayList< double[] > data, int k ) {
		K = k;	
		I = 0;
		N = data.size();			
		D = data.get( 0 ).length;		
		//Defensively copy the data set passed. Using enhanced for-loop for now
		dataPts = new ArrayList< double[] >();
		for( double[] pt : data ) {
					dataPts.add( pt.clone() );
		}
		//int[] lbl = new int[ N ];					//This array holds the ordered list of class labels
		//Create external criteria
		//c2 = new Clustering( N );
		//c2.createLabel( lbl );		//This 'clustering' only contains the order class labels from file
		//System.out.println( "K-Means Algorithm Results\nNumber of Clusters: " + K + "\tNumber of Iterations: "
					//+ I + "\nNumber of Points: " + N + "\tDimensionality: " + D + "\n" );
		
	}
	
	/******************************************************************************************************************************************
	Method: cluster
	Description: This method will create a clustering and return it. It must know which run this is, to properly
	seed centroids.
	******************************************************************************************************************************************/
	public void cluster( Clustering c, int run ) {
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
	
	/******************************************************************************************************************************************
	Method: assignPoints
	Description: This method will assign a data point to the cluster of the closest centroid. It will go through 
	each point, calculate the Euclidean distance between that point and every centroid, and then assign the point to 
	that cluster.
	******************************************************************************************************************************************/
	public void assignPoints( Clustering c ) {
		for( int i = 0; i < N; i ++ ) {
			double dist;
			double min = euclidDist( dataPts.get( i ),c.getCluster( 0 ).centroid() );
			int mIndex = 0;	
			for( int j = 1; j < K; j++ ) {
				dist = euclidDist( dataPts.get( i ), c.getCluster( j ).centroid() );
				if( dist < min ) {
					mIndex = j;		
					min = dist;    
				}
			}
			c.getCluster( mIndex ).addPoint( dataPts.get( i ) );//Add the point to the closest centroid
			c.setLabel( i, mIndex );							//Add the class label to the clustering
		}
	}
	
	/******************************************************************************************************************************************
	Method: initCentroids
	Description: This method will initialize the k centroids from randomly selected points. Points have been 
	selected previously, so this method must know which run to use.
	******************************************************************************************************************************************/
	public void initCentroids( Clustering c, int run ) {
		//Seed each cluster with an initial centroid
		for( int i = 0; i < K; i++ ) {
			//This will 1) select a random int in the range of the list of data points, 2) get the data point at
			//that index, 3) creates a new cluster with that point as the centroid, 4) and adds that cluster to
			//a list
			//c.addCluster( new Cluster ( dataPts.get( randomInit.get( run )[ i ] ) ) );
		}		
	}
	
	/******************************************************************************************************************************************
	Method: updateCentroid
	Description: This method will calculate the new centroid of the cluster by averaging the values of each
	attribute of the all the points in the cluster. It will then set this new point as the current centroid, and 
	clear the list of points in the cluster.
	******************************************************************************************************************************************/
	public void updateCentroid( Cluster c ) {
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
	
	/******************************************************************************************************************************************
	Method: euclidDist
	Description: This method will accept two points and return their Euclidean distance in the form of a double. It 
	assumes both points have coordinates equal in length.
	******************************************************************************************************************************************/
	private double euclidDist( double[] a, double[] b ) {
		double sum = 0.0;
		for( int i = 0; i < D; i++ ) {
			double x = a[ i ] - b[ i ];
			sum += x * x;				//(x1-x2)^2,(y1-y2)^2,...
		}
		return sum;
	}
	
	/******************************************************************************************************************************************
	Method: sSE
	Description: This method will take a cluster and measure its SSE. The SSE in the cluster is the total error, or 
	distance from the centroid, of each point in the cluster.
	******************************************************************************************************************************************/
	private double sSE( Cluster c ) {
		double sse = 0.0;
		for( int i = 0; i < c.size(); i ++ ) {
			sse += euclidDist( c.point( i ), c.centroid() );	//Add the distance from point to centroid
		}
		return sse;
	}
	
	/******************************************************************************************************************************************
	Method: randInitPoints
	Description: This method will randomly choose k unique points from the data set to act as initial centroids.
	******************************************************************************************************************************************/
	public void randInitPoints() {
		Random rand;							
		//randomInit = new ArrayList< int[] >();	//10 int arrays, each integer is the index of a point for centroid
		//Create table
		for( int i = 0; i < 10; i++ ) {
			rand = new Random();		
			int[] run = new int[ K ];	
			for( int c= 0; c < K; c++ ) {
				run[ c ] = rand.nextInt( raw.size() );	//Pick a point for the initial centroid
			}
			//randomInit.add( run );	
		}
	}
	
	/******************************************************************************************************************************************
	Method: distInitPoints
	Description: This method will randomly choose 1 unique point from the data set to act as an initial centroid. It
	will then find the furthest point in the data set, and select this point as another centroid, and continue until 
	k centroids have been determined.
	******************************************************************************************************************************************/
	public void distInitPoints() {
		
	}
}
