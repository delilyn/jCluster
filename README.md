jCluster (Completed - Maintenance)
==================================

Includes Java implementations of clustering algorithms, data normalization methods, and the adjusted Rand index. This
project consists of the folowing files:<ol>
<li>Kmeans.JAVA - Heart of the program. Uses the other objects to cluster data points from a text file. Also includes 
code for the adjusted Rand Index.</li>
<li>Normalize.JAVA  - Contains methods to copy input datat point and create ordered, normalized points as output.</li>
<li>Cluster.JAVA  - Represents a single data cluster, with a centroid and nearest data points. Required by all other
classes.</li>
<li>Clustering.JAVA - Represents a group of Cluster objects created by the k-means clustering on a data set. Required
by all other classes except for Cluster.JAVA</li></ol>

Use
===
The I/O operations require a *.txt file, containing N data points with D dimensions to be separated into K clusters.
The file needs to be integer/float values separated by whitespace (but CSV requires only a small tweak), with each
data point on a single line. The header is as follows:<br>
<# Data Points> <# Dimensions + 1 (The last dimension of each line is Class Label> <# Clusters, K><br>
The data sets used for this implementation are part of the UC Irvine Machine Learning Repository.

