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

	private int K;								//The number of clusters to form
	private int I;								//The number of iterations until the centroids stop moving
	private int N;								//The number of points in the data set
	private int D;								//The number of attributes, or dimensionality, of each point.
	private double totalSSE;					//The total SSE of the clustering
	private ArrayList< Cluster > clustering;	//A collection of clusters, each with a centroid and points
	private ArrayList< double[] > dataPts;		//The list of data points to be clustered with Kmeans
	private ArrayList< double[] > raw;			//Raw data points parsed from file
	
	/******************************************************************************************************************************************
	Method: Constructor
	Description: This method accepts a data set in the form of an array list of double[], where each double[] holds the coordinates of a point.
	It will create its own copy of the data set and retrieve the necessary parameters. It also accepts a char denoting which centroid
	initialization method to use for the clustering.
	******************************************************************************************************************************************/
	public Kmeans( ArrayList< double[] > data, int k, char i ) {
		K = k;	
		I = 0;
		N = data.size();			
		D = data.get( 0 ).length;		
		//Defensively copy the data set passed. Using enhanced for-loop for now
		dataPts = new ArrayList< double[] >();
		for( double[] pt : data ) {
					dataPts.add( pt.clone() );
		}
		//Create the list of clusters
		clustering = new ArrayList< Cluster >();
		//Determine which initialization method the user wants to use. It defaults to "randomInitialization" in the case of unrecognized chars
		switch( i ) {
		case 'd':
			break;
		case 'r':
		default:
			randInitPoints();
			break;
		}
	}
	
	/******************************************************************************************************************************************
	Method: cluster
	Description: This method will create a clustering and return it. It must know which run this is, to properly
	seed centroids.
	******************************************************************************************************************************************/
	public void cluster(  ) {
		//Clear the points in each cluster
		for( Cluster c : clustering ) {
			c.clearPoints();
		}
		assignPoints();
		//totalSSe = 0.0;
		for( Cluster c : clustering ) {
			//sum += sSE( c.get( i ) );	
			updateCentroid( c );
		}
		//totalSSE = sum;
	}
	
	/******************************************************************************************************************************************
	Method: assignPoints
	Description: This method will assign a data point to the cluster of the closest centroid. It will go through 
	each point, calculate the Euclidean distance between that point and every centroid, and then assign the point to 
	that cluster.
	******************************************************************************************************************************************/
	public void assignPoints( ) {
		for( int i = 0; i < N; i ++ ) {
			double dist;
			double min = euclidDist( dataPts.get( i ), clustering.get( 0 ).centroid() );
			int mIndex = 0;	
			for( int j = 1; j < K; j++ ) {
				dist = euclidDist( dataPts.get( i ), clustering.get( j ).centroid() );
				if( dist < min ) {
					mIndex = j;		
					min = dist;    
				}
			}
			clustering.get( mIndex ).addPoint( dataPts.get( i ) );//Add the point to the closest centroid
			//c.setLabel( i, mIndex );							//Add the class label to the clustering
		}
	}
	
	/******************************************************************************************************************************************
	Method: updateCentroid
	Description: This method will calculate the new centroid of the cluster by averaging the values of each
	attribute of the all the points in the cluster. It will then set this new point as the current centroid, and 
	clear the list of points in the cluster.
	******************************************************************************************************************************************/
	public void updateCentroid( Cluster c ) {
		int m = c.size();
		double sum;
		double[] newCoord = new double[ D ];
		for( int i = 0; i < D; i++ ) {
			sum = 0.0;
			for( int j = 0; j < m; j++ ) {
				sum += c.point( j )[ i ];
			}
			newCoord[ i ] = ( sum / m );
		}
		c.newCentroid( newCoord.clone() );
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
	private void randInitPoints() {
		int[] indices = new int[ K ];	//List of previously selected indices, which cannot be selected again
		//Randomly choose K centroids for the K clusters
		int i = 0;
		while( i < K ) {
			Random gen = new Random();
			int rand = gen.nextInt( N );
			boolean selected = false;
			for( int c = 0; c < i; c++ ) {
				if( indices[ c ] == rand ) {
					selected = true;
				}
			}
			if( selected == false ) {
				indices[ i ] = rand;
				i++;
			}
		}
		//Assign the points at the selected indices as the centroids of the clusters
		for( int j = 0; j < K; j++ ) {
			clustering.add( new Cluster( dataPts.get(  indices[ j ] ).clone() ) );
		}
	}
	
	/******************************************************************************************************************************************
	Method: distInitPoints
	Description: This method will randomly choose 1 unique point from the data set to act as an initial centroid. It
	will then find the furthest point in the data set, and select this point as another centroid, and continue until 
	k centroids have been determined.
	******************************************************************************************************************************************/
	private void distInitPoints() {
		Random rand;
		
	}
}
