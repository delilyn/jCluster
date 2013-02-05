//package edu.lsus.streetman.kmeans;

/********************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

Class: Clustering
Description: This class represents a clustering of data points. It stores the centroids of the clusters, labels of
the data points, etc.
********************************************************************************************************************/
import java.util.ArrayList;

public class Clustering {
  
	int k;				                //The number of clusters in this clustering
	int numPoints;
	int[] label;			            //The integer class labels of each point
	ArrayList< Cluster > cluster;	//The list of Cluster objects
	
	/************************************************************************************************************
	The default constructor for a Clustering object. Takes the number of points to initialize the list of labels
	************************************************************************************************************/
	public Clustering( int n )
	{
		numPoints = n;
		cluster = new ArrayList< Cluster >();	//Initialize list of Cluster objects
		label = new int[ numPoints ];		      //Initialize list of labels for each point
	}
	
	/************************************************************************************************************
	This method returns the class label of the point at the specified index
	************************************************************************************************************/
	public int label( int index )
	{
		return label[ index ];
	}
	
	/************************************************************************************************************
	This method will accept a point's index and label, and save that label to the Clustering's array
	************************************************************************************************************/
	public void setLabel( int point, int lbl )
	{
		label[ point ] = lbl;
	}
	
	/************************************************************************************************************
	This method will clear the Clustering's labels
	************************************************************************************************************/
	public void clearLabel()
	{
		label = new int [ numPoints ];
	}
	
	
	/************************************************************************************************************
	This method will accept a list of labels pulled from an external file
	************************************************************************************************************/
	public void createLabel( int[] lbl )
	{
		label = lbl;
	}
	
	/************************************************************************************************************
	This method returns the specified Cluster from the Clustering's collection
	************************************************************************************************************/
	public Cluster getCluster( int index )
	{
		return cluster.get( index );
	}
	
	/************************************************************************************************************
	This method accepts a cluster and adds it to the current list of clusters
	************************************************************************************************************/
	public void addCluster( Cluster c )
	{
		cluster.add( c );
	}
}
