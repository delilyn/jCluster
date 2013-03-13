package edu.lsus.streetman.kmeans;
/*******************************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012
Class: Normalize
Description: This class holds functions for normalizing data ( ArrayList< double[] > dataPoints ) in several methods.
Included is Min-Max, Z-Score, ...
*******************************************************************************************************************************/
import java.util.ArrayList;

public class Normalize {
	
	/**************************************************************************************************************************
	Method: Constructor
	Descr: The constructor is private because no Normalize object is meant to exist. Just call methods.
	**************************************************************************************************************************/
	private Normalize() {
	}
	
	/**************************************************************************************************************************
	Method: minMax
	Descr: This method accepts a list of data points, finds the min and max of each attribute, and returns a list of the 
	normalized points. The points are normalized by subtracting each point by the min, then dividing by the range (min - max).
	**************************************************************************************************************************/
	public static ArrayList< double[] > minMax( ArrayList< double [] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//This will hold the normalized data and be returned
		double[] tmp = data.get( 0 );
		int dimen = tmp.length;
		double[] min = new double[ dimen ];	//List of the lowest value in each attribute
		double[] max = new double[ dimen ];	//List of the highest value in each attribute
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < dimen; a++ ) {
			min[ a ] = max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < data.size(); p++ ) {
			tmp = data.get( p );	
			for( int a = 0; a < dimen; a++ ) {
				//See if the min-max values need to be adjusted
				if( tmp[ a ] < min[ a ] ) {
					min[ a ] = tmp[ a ];
				}
				if( tmp[ a ] > max[ a ] ) {
					max[ a ] = tmp[ a ];
				}
			}
		}
		//Create the normalized list of data points
		for( int p = 0; p < dimen; p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];
			for( int a = 0; a < dimen; a++ ) {
				if( min[ a ] == max[ a ] ) {
					newPoint[a] = 0;
				} else {
					newPoint[ a ] = ( tmp[ a ] - min[ a ] ) / ( max[ a ] - min[ a ] );
				}
			}
			norm.add( newPoint );
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	Method: zScore
	Descr: This method accepts a list of data points, finds the mean and standard deviation of each attribute, and returns a 
	list of the normalized points. Points are normalized by each attribute, where the value of that attribute is subtracted by
	the mean of all attribute values, and this difference is divided by the standard deviation of all attributes.
	**************************************************************************************************************************/
	public static ArrayList< double[] > zScore( ArrayList< double[] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//List of normalized points
		double[] tmp = data.get( 0 );
		int dimen = tmp.length;
		double[] stdDev = new double[ dimen ];		//Standard deviation of each attribute
		double[] mean = new double[ dimen ];		//Mean value of each attribute
		double[] sums = new double[ dimen ];		//Sum of values of each attribute
		double[] sumSquared = new double[ dimen ];	//Sum of squared variance of each attribute
		//Find the sum of each attribute of the original data
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			for( int a = 0; a < dimen; a++ ) {
				sums[ a ] += tmp[ a ];
			}
		}
		//Calculate the mean of each attribute
		for( int a = 0; a < dimen; a++ ) {
			mean[ a ] = sums[ a ] / data.size();
		}
		//Find the sum of squared variance of each attribute of original data
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			for( int a = 0; a < dimen; a++ ) {
				//Add to the sum of squared variance of each attribute
				sumSquared[ a ] += ( tmp[ a ] - mean[ a ] ) * ( tmp[ a ] - mean[ a ] );
			}
		}
		//Calculate standard deviation of each attribute
		for( int a = 0; a < dimen; a++ ) {
			stdDev[ a ] = Math.sqrt( sumSquared[ a ] / ( data.size() - 1 ) );
		}
		//Create an ArrayList of normalized data
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];
			for( int a = 0; a < dimen; a++ ) {
				if( stdDev[ a ] == 0.0 ) {
					newPoint[ a ] = 0.0;
				} else {
					newPoint[ a ] = ( tmp[ a ] - mean[ a ] ) / stdDev[ a ];
				}
			}
			norm.add( newPoint );
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	Method: rankedNorm
	Descr: This method accepts a list of data points, ranks the values of each attribute, and returns normalized points. The
	values of the attributes of the new point are the ranks of each of those values, in decreasing? order. If there are ties
	among values of an attributes, all points with tying ranks are assigned the average of the sum of the ranks.
	**************************************************************************************************************************/
	public static ArrayList< double[] > rankedNorm( ArrayList< double[] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//List of normalized points
		double[] tmp = data.get( 0 );
		int dimen = tmp.length;
		int size = data.size();
		double[][] rankings = new double[ dimen ][ size ];
		for( int a = 0; a < dimen; a++) {
			rankings[ a ][ 0 ] = tmp[ a ];
		}
		//Rank the data with an insertion sort. This code will fill out a D x N table of double values, in ascending order.
		for( int p = 1; p < size; p++ ) {
			tmp = data.get( p );	//Placeholder for raw point
			for( int a = 0; a < dimen; a++ ) {
				//Use insertion sort to put attribute in its place. Based on pseudocode in Intro to Algorithms 3rd Ed. (Cormen, et al.) p.18
				double key = tmp[ a ];	//The value to be inserted
				int i = p - 1;			//This corresponds to 'j - 1'.
				while( i >= 0 && rankings[ a ][ i ] > key ) {
					rankings[ a ][ i + 1 ] = rankings[ a ][ i ];	//Move this element up one rank
					i--;											//Decrement the index
				}
				rankings[ a ][ i + 1 ] = key;	//Set the key in its new correct position
			}
		}
		//Now that values are ranked, go through each raw point and normalize its attributes
		for( int p = 0; p < size; p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];	//Initialize new normalized point
			int rank;					//Lowest rank with this value
			int maxRank;					//Highest rank with this value
			double raw;					//Raw attribute value
			for( int a = 0; a < dimen; a++ ) {
				raw = tmp[ a ];
				rank = 0;		//Lowest index with matching value
				maxRank = 0;	//Highest index with matching value
				//Find the lowest rank matching that attribute value
				while( rank < size && raw > rankings[ a ][ rank ] ) {
					rank++;
					maxRank++;
				}
				while( maxRank < size && raw == rankings[ a ][ maxRank ] ) {
					maxRank++;
				}
				//If only one rank has that value...
				if( maxRank == rank ) {
					newPoint[ a ] = ( rank );
				} else {
					double sum = 0.0;	//Sum the ranks
					int count = 0;		//Count how many ranks there are
					for( int i = 0; i <  ( maxRank - rank ); i++ ) {
						sum += ( rank + i );
						count++;
					}
					newPoint[ a ] = sum / count;
				}
			}
			norm.add( newPoint );	//Add the normalized point to new data set
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	Method: maxNorm
	Descr: This method accepts a list of data points, normalizes the values in relation to that attribute's max value, and 
	returns the normalized points. For the new point, the value of each attribute is the value of the old attribute divided by
	the max value found for that attribute.
	**************************************************************************************************************************/
	public static ArrayList< double[] > maxNorm( ArrayList< double [] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//This will hold the normalized data and be returned
		int dimen = data.get( 0 ).length;
		double[] tmp = data.get( 0 );
		double[] max = new double[ dimen ];					//List of the highest value in each attribute
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < dimen; a++ ) {
			max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < data.size(); p++ ) {
			tmp = data.get( p );	//Hold data for new point
			for( int a = 0; a < dimen; a++ ) {
				if( tmp[ a ] > max[ a ] ) {
					max[ a ] = tmp[ a ];
				}
			}
		}
		//Create the normalized list of data points
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];
			for( int a = 0; a < dimen; a++ ) {
				if( max[a] == 0 ) {
					newPoint[a] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / max[ a ]; //x_norm = x / x_max - x_min
				}
			}
			norm.add( newPoint );	//Add the normalized point
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	Method: rangeNorm
	Descr: This method accepts a list of data points, normalizes the values in relation to that attribute's range, and returns 
	the normalized points. For the new point, the value of each attribute is the value of the old attribute, divided by the
	difference between the max and min values for that attribute ( range ).
	**************************************************************************************************************************/
	public static ArrayList< double[] > rangeNorm( ArrayList< double [] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//This will hold the normalized data and be returned
		int dimen = data.get( 0 ).length;
		double[] tmp = data.get( 0 );
		double[] min = new double[ dimen ];				//List of the lowest value in each attribute
		double[] max = new double[ dimen ];				//List of the highest value in each attribute
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < dimen; a++ ) {
			min[ a ] = max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < data.size(); p++ ) {
			tmp = data.get( p );
			for( int a = 0; a < dimen; a++ ) {
				//See if the min-max values need to be adjusted
				if( tmp[ a ] < min[ a ] ) {
					min[ a ] = tmp[ a ];
				}
				if( tmp[ a ] > max[ a ] ) {
					max[ a ] = tmp[ a ];
				}
			}
		}
		//Create the normalized list of data points
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];
			for( int a = 0; a < dimen; a++ ) {
				if( min[ a ] == max[ a ] ) {
					newPoint[ a ] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / ( max[ a ] - min[ a ] ); //x_norm = x / x_max - x_min
				}
			}
			norm.add( newPoint );
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	Method: euclidNorm
	Descr: This method accepts a list of data points, normalizes the values in relation to that attribute's Euclidean norm, and 
	returns the normalized points. For the new point, the value of each attribute is the old value divided by the Euclidean
	norm for that point, which is the square root of the sum of all the values, squared, of that attribute for each point.
	**************************************************************************************************************************/
	public static ArrayList< double[] > euclidNorm( ArrayList< double [] > data ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();
		double[] tmp = data.get( 0 );
		int dimen = tmp.length;
		double[] euclidNorm = new double [ dimen ];	//The Euclidean norm of each attribute
		//
		//Find the Euclidean norm of each attribute
		//
		for( int a = 0; a < dimen; a++ ) {
			double sumSquare = 0.0;	//Sum of squared attribute value;
			double att;				//Value of attribute for current raw point
			for( int p = 0; p < data.size(); p++ ) {
				att = data.get( p )[ a ];
				sumSquare += att * att;
			}
			euclidNorm[ a ] = Math.sqrt( sumSquare );
		}
		//
		//Create the normalized list of data points
		//
		for( int p = 0; p < data.size(); p++ ) {
			tmp = data.get( p );
			double[] newPoint = new double[ dimen ];
			for( int a = 0; a < dimen; a++ ) {
				if( euclidNorm[ a ] == 0 ) {
					newPoint[ a ] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / euclidNorm[ a ]; //x_norm = x / x_euclid
				}
			}
			norm.add( newPoint );
		}
		return norm;
	}
}
