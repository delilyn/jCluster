package edu.lsus.streetman.kmeans;
/*****************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

This class represents a cluster of data points and its centroid.
*****************************************************************************************************************/
import java.util.ArrayList;

public class Cluster {
	
	private double[] centroid;		//Double array representing current centroid coordinates
	private ArrayList< double[] > points;	//List of double arrays representing points
	
	/***************************************************************************************************************
	Method: Constructor
	Description: Requires a double[] that represents the coordinates of a centroid.
	***************************************************************************************************************/
	public Cluster( double[] c ) {
		points = new ArrayList< double[] >();	
		centroid = c;			//Assign initial centroid
	}
	
	/***************************************************************************************************************
	Method: centroid
	Description: This method will return the coordinates of the point representing the centroid of the cluster
	***************************************************************************************************************/
	public double[] centroid() {
		return centroid;
	}
	
	/****************************************************************************************************************
	Method: newCentroid
	Description: This method accepts the coordinates for the new centroid
	****************************************************************************************************************/
	public void newCentroid( double[] coord ) {
		centroid = coord;		
	}
	
	/***************************************************************************************************************
	Method: addPoint
	Description: This method will accept a point and add it to the list of data points in this cluster.
	***************************************************************************************************************/
	public void addPoint( double[] p ) {
		points.add( p );
	}
	
	/***************************************************************************************************************
	Method: point
	Description: This method will return the coordinates of the Point at the provided index in the cluster.
	***************************************************************************************************************/
	public double[] point( int i ) {
		return points.get( i );
	}
	
	/***************************************************************************************************************
	Method: size
	Description: This method will return the number of points in the cluster.
	***************************************************************************************************************/
	public int size() {
		return points.size();
	}
	
	/***************************************************************************************************************
	Method: clearPoints
	Description: This method will clear the list of data points in the cluster. This must be conducted after each
	iteration of the k-means.
	***************************************************************************************************************/
	public void clearPoints() {
		points.clear();
	}
}
