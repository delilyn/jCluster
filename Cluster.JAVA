//package edu.lsus.streetman.kmeans;

/***********************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

Class: Cluster
Description: This class represents a cluster of data points and its centroid. It has methods for storing a centroid and
data points, and clearing these points.
***********************************************************************************************************************/
import java.util.ArrayList;

public class Cluster {
  
	private double[] centroid;				    //Double array representing current centroid coordinates
	private ArrayList< double[] > points;	//List of double arrays representing points
	
	/***************************************************************************************************************
	This is the default constructor for a Cluster object. A centroid must be provided.
	***************************************************************************************************************/
	public Cluster( double[] c )
	{
		points = new ArrayList< double[] >();	//Initialize points arraylist
		centroid = c;							            //Assign initial centroid
	}
	
	/***************************************************************************************************************
	This method will return the Point representing the centroid of the cluster
	***************************************************************************************************************/
	public double[] centroid()
	{
		return centroid;
	}
	
	/****************************************************************************************************************
	This method accepts a list of coordinates for the new centroid
	****************************************************************************************************************/
	public void newCentroid( double[] coord )
	{
		centroid = coord;		
	}
	
	/***************************************************************************************************************
	This method will accept a point and add it to the list of data points in this cluster
	***************************************************************************************************************/
	public void addPoint( double[] p )
	{
		points.add( p );
	}
	
	/***************************************************************************************************************
	This method will return the coordinates at the provided index
	***************************************************************************************************************/
	public double[] point( int i )
	{
		return points.get( i );
	}
	
	/***************************************************************************************************************
	This method will return the number of points in the cluster
	***************************************************************************************************************/
	public int size()
	{
		return points.size();
	}
	
	/***************************************************************************************************************
	This method will clear the list of data points in the cluster. This way, data points can be re-added with a new
	centroid.
	***************************************************************************************************************/
	public void clearPoints()
	{
		points.clear();
	}
}
