package edu.lsus.streetman.kmeans;
/*****************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

This class represents a clustering of data points. It stores the centroids of the clusters, labels of the data
points, etc.
*****************************************************************************************************************/
import java.util.ArrayList;

public class Clustering {
	
	private int numPoints;
	private int[] label;			//The integer class labels of each point
	private ArrayList< Cluster > cluster;	//The list of Cluster objects
	
	/************************************************************************************************************
	Method: Constructor
	Description: Accepts the number of points to initialize the list of labels. The labels are for comparing
	labeled output to actual output in the adjusted Rand index.
	************************************************************************************************************/
	public Clustering( int n ) {
		numPoints = n;
		cluster = new ArrayList< Cluster >();	//Initialize list of Cluster objects
		label = new int[ numPoints ];		//Initialize list of labels for each point
	}
	
	/************************************************************************************************************
	Method: label
	Description: This method returns the class label of the point at the specified index.
	************************************************************************************************************/
	public int label( int index ) {
		return label[ index ];
	}
	
	/************************************************************************************************************
	Method: setLabel
	Description: This method will accept a point's index and label, and save that label to the Clustering's array
	************************************************************************************************************/
	public void setLabel( int point, int lbl ) {
		label[ point ] = lbl;
	}
	
	/************************************************************************************************************
	Method: clearLabel
	Description: This method will clear the labels of all the points in the Clustering.
	************************************************************************************************************/
	public void clearLabel() {
		label = new int [ numPoints ];
	}
	
	/************************************************************************************************************
	Method: createLabel
	Description: This method will accept a list of labels pulled from an external file. These are in the orignal
	order.
	************************************************************************************************************/
	public void createLabel( int[] lbl ) {
		label = lbl;
	}
	
	/************************************************************************************************************
	Method: getCluster
	Description: This method returns the specified Cluster object from the current Clustering.
	************************************************************************************************************/
	public Cluster getCluster( int index ) {
		return cluster.get( index );
	}
	
	/************************************************************************************************************
	Method: addCluster
	Description: This method accepts a cluster and adds it to the current Clustering.
	************************************************************************************************************/
	public void addCluster( Cluster c ) {
		cluster.add( c );
	}
}
