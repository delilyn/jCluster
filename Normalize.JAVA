//package edu.lsus.streetman.kmeans;

/*******************************************************************************************************************************
Robert Streetman
LSUS - CST 790 (Dr Celebi)
Fall 2012

Class: Normalize
Description: This class holds functions for normalizing data ( ArrayList< double[] > dataPoints ) in several methods.
Included is Min-Max, Z-Score, ...
*******************************************************************************************************************************/
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Normalize {
  
	private static int dimen;	              //Number of attributes per point	
	private static int size;	              //Number of data points
	private static double[][] rankings;

	/**************************************************************************************************************************
	The constructor is private because no Normalize object is meant to exist. Just call methods.
	**************************************************************************************************************************/
	private Normalize() {
	}
	
	/**************************************************************************************************************************
	----------------------------------------MIN-MAX NORMALIZATION--------------------------------------------------------------
	This method accepts a list of data points, finds the min and max of each attribute, and returns a list of the normalized
	points
	**************************************************************************************************************************/
	public static ArrayList< double[] > minMax( ArrayList< double [] > d ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();   
		double[] tmp = d.get( 0 );									                
		double[] min = new double[ tmp.length ];	                  
		double[] max = new double[ tmp.length ];	                  
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < tmp.length; a++ ) {
			min[ a ] = max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < d.size(); p++ ) {
			tmp = d.get( p );	                                   //Hold data for new point
			for( int a = 0; a < tmp.length; a++ ) {
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
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			double[] newPoint = new double[ tmp.length ];
			for( int a = 0; a < newPoint.length; a++ ) {
				if( min[ a ] == max[ a ] ) {
					newPoint[a] = 0;
				} else {
					newPoint[ a ] = ( tmp[ a ] - min[ a ] ) / ( max[ a ] - min[ a ] );
				}
			}
			norm.add( newPoint );	                               //Add the normalized point
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	 * -----------------------------------------Z-SCORE NORMALIZATION----------------------------------------------------------
	This method accepts a list of data points, finds the mean and standard deviation of each attribute, and returns a 
	list of the normalized points
	**************************************************************************************************************************/
	public static ArrayList< double[] > zScore( ArrayList< double[] > d ) {
		ArrayList< double[] > norm = new ArrayList< do  uble[] >();	//List of normalized points
		int dimension = d.get( 0 ).length;				      //Set a variable to make lines shorter
		double[] stdDev = new double[ dimension ];		  //Standard deviation of each attribute
		double[] mean = new double[ dimension ];		    //Mean value of each attribute
		double[] sums = new double[ dimension ];		    //Sum of values of each attribute
		double[] sumSquared = new double[ dimension ];	//Sum of squared variance of each attribute
		double[] tmp = new double[ dimension ];		    	//Temporary point
		//Find the sum of each attribute of the original data
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			for( int a = 0; a < tmp.length; a++ ) {
				sums[ a ] += tmp[ a ];	                    //Add to the sum of each attribute
			}
		}
		//Calculate the mean of each attribute
		for( int a = 0; a < tmp.length; a++ ) {
			mean[ a ] = sums[ a ] / d.size();	            //Set the mean of each attribute
		}
		//Find the sum of squared variance of each attribute of original data
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			for( int a = 0; a < tmp.length; a++ ) {
				//Add to the sum of squared variance of each attribute
				sumSquared[ a ] += ( tmp[ a ] - mean[ a ] ) * ( tmp[ a ] - mean[ a ] );
			}
		}
		//Calculate standard deviation of each attribute
		for( int a = 0; a < tmp.length; a++ ) {
			stdDev[ a ] = Math.sqrt( sumSquared[ a ] / ( d.size() - 1 ) );
		}
		//Create an ArrayList of normalized data
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			double[] newPoint = new double[ tmp.length ];
			for( int a = 0; a < tmp.length; a++ ) {
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
	----------------------------------------RANKED NORMALIZATION---------------------------------------------------------------
	This method accepts a list of data points, ranks the values of each attribute, and returns normalized points
	**************************************************************************************************************************/
	public static ArrayList< double[] > rankedNorm( ArrayList< double[] > d ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();
		double[] tmp;					
		double[] newPoint;				
		dimen = d.get( 0 ).length;	                        //Variable to keep lines short
		size = d.size();			
		rankings = new double[ dimen ][ size ];
		tmp = d.get( 0 );
		for( int a = 0; a < dimen; a++) {
			rankings[ a ][ 0 ] = tmp[ a ];
		}
		//Rank the data with an insertion sort. This code will fill out a D x N table of double values, in ascending order.
		for( int p = 1; p < size; p++ ) {
			tmp = d.get( p );	//Placeholder for raw point
			for( int a = 0; a < dimen; a++ ) {
				//Insertion Sort based on pseudocode in Intro to Algorithms 3rd Ed. (Cormen, et al.) p.18
				double key = tmp[ a ];	                        //The value to be inserted
				int i = p - 1;			                            //This corresponds to 'j - 1'.
				while( i >= 0 && rankings[ a ][ i ] > key) {
					rankings[ a ][ i + 1 ] = rankings[ a ][ i ];	//Move this element up one rank
					i--;											                    //Decrement the index
				}
				rankings[ a ][ i + 1 ] = key;	//Set the key in its new correct position
			}
		}
		//Now that values are ranked, go through each raw point and normalize its attributes
		for( int p = 0; p < size; p++ ) {
			tmp = d.get( p );				  
			newPoint = new double[ dimen ];	//Initialize new normalized point
			int rank;						                            //Lowest rank with this value
			int maxRank;					                          //Highest rank with this value
			double raw;						                          //Raw attribute value
			for( int a = 0; a < dimen; a++ ) {
				raw = tmp[ a ];	
				rank = 0;		                                
				maxRank = 0;	
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
					newPoint[ a ] = ( rank );	                  //Set normalized attribute
				} else {
					double sum = 0.0;	                          //Sum the ranks
					int count = 0;		                          //Count how many ranks there are
					for( int i = 0; i <  ( maxRank - rank ); i++ ) {
						sum += ( rank + i );	                    
						count++;			  	                        //Count the number of rankings with value
					}
					newPoint[ a ] = sum / count;              	//Set normalized attribute
				}
			}
			norm.add( newPoint );	                         //Add the normalized point to new data set
		}
		printRank();
		printNorm( norm );
		return norm;
	}
	
	/**************************************************************************************************************************
	----------------------------------------MAX NORMALIZATION------------------------------------------------------------------
	This method accepts a list of data points, normalizes the values in relation to that attribute's max value, and returns
	the normalized points.
	**************************************************************************************************************************/
	public static ArrayList< double[] > maxNorm( ArrayList< double [] > d ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	//This will hold the normalized data and be returned
		double[] tmp = d.get( 0 );									
		double[] max = new double[ tmp.length ];					//List of the highest value in each attribute
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < tmp.length; a++ ) {
			max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < d.size(); p++ ) {
			tmp = d.get( p );	//Hold data for new point
			for( int a = 0; a < tmp.length; a++ ) {
				if( tmp[ a ] > max[ a ] ) {
					max[ a ] = tmp[ a ];
				}
			}
		}
		//Create the normalized list of data points
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );								
			double[] newPoint = new double[ tmp.length ];	  //Normalized point
			for( int a = 0; a < newPoint.length; a++ ) {
				if( max[a] == 0 ) {
					newPoint[a] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / max[ a ];        //x_norm = x / x_max - x_min
				}
			}
			norm.add( newPoint );	                          //Add the normalized point
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	----------------------------------------RANGE NORMALIZATION----------------------------------------------------------------
	This method accepts a list of data points, normalizes the values in relation to that attribute's range, and returns the
	normalized points.
	**************************************************************************************************************************/
	public static ArrayList< double[] > rangeNorm( ArrayList< double [] > d ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	
		double[] tmp = d.get( 0 );									
		double[] min = new double[ tmp.length ];				        	//List of the lowest value in each attribute
		double[] max = new double[ tmp.length ];				      	  //List of the highest value in each attribute
		//Set the min and max of each attribute to the first point in the set
		for( int a = 0; a < tmp.length; a++ ) {
			min[ a ] = max[ a ] = tmp[ a ];
		}
		for( int p = 1; p < d.size(); p++ ) {
			tmp = d.get( p );	//Hold data for new point
			for( int a = 0; a < tmp.length; a++ ) {
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
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			double[] newPoint = new double[ tmp.length ];
			for( int a = 0; a < newPoint.length; a++ ) {
				if( min[ a ] == max[ a ] ) {
					newPoint[ a ] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / ( max[ a ] - min[ a ] ); //x_norm = x / x_max - x_min
				}
			}
			norm.add( newPoint );	                                  //Add the normalized point
		}
		return norm;
	}
	
	/**************************************************************************************************************************
	----------------------------------------VECTOR NORMALIZATION---------------------------------------------------------------
	This method accepts a list of data points, normalizes the values in relation to that attribute's Euclidean norm, and 
	returns the normalized points.
	**************************************************************************************************************************/
	public static ArrayList< double[] > euclidNorm( ArrayList< double [] > d ) {
		ArrayList< double[] > norm = new ArrayList< double[] >();	
		double[] tmp = d.get( 0 );									
		double[] euclidNorm = new double [ tmp.length ];			    //List of the Euclidean norm of each attribute
		//Set the Euclidean norm of each attribute
		for( int a = 0; a < tmp.length; a++ ) {
			double sumSquare = 0.0;	                                //Sum of squared attribute value
			double att;				                                      //Value of attribute for current raw point
			for( int p = 0; p < d.size(); p++ ) {
				att = d.get( p )[ a ];
				sumSquare += att * att;
			}
			euclidNorm[ a ] = Math.sqrt( sumSquare );
		}
		//Create the normalized list of data points
		for( int p = 0; p < d.size(); p++ ) {
			tmp = d.get( p );
			double[] newPoint = new double[ tmp.length ];
			for( int a = 0; a < newPoint.length; a++ ) {
				if( euclidNorm[ a ] == 0 ) {
					newPoint[ a ] = 0;
				} else {
					newPoint[ a ] = tmp[ a ] / euclidNorm[ a ];         //x_norm = x / x_euclid
				}
			}
			norm.add( newPoint );	                                  //Add the normalized point
		}
		return norm;
	}
	
	/****************************************************************************************************************
	This method will print out a list of the ranked attributes from the rankedNorm method to a file. Used to verify
	proper ranking of attributes.
	****************************************************************************************************************/
	private static void printRank() {
		/**************************************************************************************************************
		CHANGE OUTPUT FILENAME HERE
		**************************************************************************************************************/
		String file = "rank.txt";
		FileWriter write = null;	                                //Initialize FileWriter to null before try...catch
		try {
			write = new FileWriter( file );						              //Initialize with file name
			BufferedWriter buffer = new BufferedWriter( write );    //Feed the File Writer to BufferedWriter
			//Construct the header for rankings table
			String head = "Rank|Dimen:";
			String line = "=============";
			for( int a = 0; a < dimen; a++ ) {
				head += "\t" + a;
				line += "========";
				//Add newline char at end of entry
				if( a == ( dimen - 1 ) ) {
					head += "\n";
					line += "\n";
				}
			}
			buffer.write( head );
			buffer.write( line );
			//Print the table a row at a time
			for( int r = 0; r < size; r++ ) {
				line = new String( "\t" + ( r ) );	                   //Start with a clean line
				for( int a = 0; a < dimen; a++ ) {
					line += "\t" + rankings[ a ][ r ];
					if( a == ( dimen - 1 ) )
						line += "\n";
				}
				buffer.write( line );
			}
			buffer.close();
		}
		catch( IOException e ) {
			System.out.println( "Error writing to file!: " + e.getMessage() );
		}
		finally {
			if( write != null ) {
				try{
					write.close();
				}
				catch( IOException e ) {
					System.out.println( "Error closing file writing: " + e.getMessage() );
				}
			}
		}
	}
	
	/****************************************************************************************************************
	This method will print out a list of the normalized points to a file. Used to verify accuracy of normalization.
	****************************************************************************************************************/
	private static void printNorm( ArrayList< double[] > d ) {
		/**************************************************************************************************************
  	CHANGE OUTPUT FILENAME HERE
		**************************************************************************************************************/
		String file = "norm.txt";
		FileWriter write = null;	                               //Initialize FileWriter to null before try...catch
		try {
			write = new FileWriter( file );
			BufferedWriter buffer = new BufferedWriter( write );
			String line;
			//Print out the points line by line
			for( int p = 0; p < size; p++ ) {
				line = "";
				for( int a = 0; a < dimen; a ++ ) {
					line += d.get( p )[ a ] + "\t";
					if( a == ( dimen - 1 ) ) {
						line += "\n";
					}
				}
				buffer.write( line );
			}
			buffer.close();
		}
		catch( IOException e ) {
			System.out.println( "Error printing to file: " + e.getMessage() );
		}
		finally {
			if( write != null ) {
				try{
					write.close();
				}
				catch( IOException e ) {
					System.out.println( "Error closing writer: " + e.getMessage() );
				}
			}
		}
	}
}
