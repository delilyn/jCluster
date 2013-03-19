jCluster (Maintenance)
==================================

Includes Java implementations of clustering algorithms, data normalization methods, and the adjusted Rand index. This
project consists of the following files:<ol>
<li>Kmeans.JAVA - The k-means algorithm takes an integer k from the user, along with a data set of n points with d 
dimensions, and forms k clusters around k centroids (points). Remaining points are assigned to the cluster of 
nearest (Euclidean) centroid. A new centroid is calculated by averaging each dimension of each point in the cluster.
This iteration continues until the valus of the centroids do not change between clusterings.</li>
<li>Normalize.JAVA  - Contains methods which accept data and normalize them based on the specified method. These
can be used separately, if desired.</li>
<li>Cluster.JAVA  - Represents a single data cluster, with a centroid and nearest data points. Required by 
Clustering.JAVA.</li>
<li>Clustering.JAVA - Represents a group of Cluster objects created by the k-means clustering on a data set. Required
by Kmeans.JAVA</li></ol>

Milestones:
===========
- Improve the random clustering initialization method to check for duplicate centroids.
- Add a clustering initialization method to choose initial centroids based on furthest distance.
- Move adjusted Rand index logic to separate class.
- Remove testing and I/O logic from K-means class.
- Optimize sorting algorithm used in ranking normalization method
- Add more normalization methods to test.

WARNING
=======
The last known good version of Kmeans.Java was written for testing of the accuracy of the Adjusted Rand Index. The
Adjusted Rand Index will be split off into a separate file, and the Kmeans.Java file will be re-written for 
utilitarian purposes.

Use
===
The I/O operations require a *.txt file, containing N data points with D dimensions to be separated into K clusters.
The file needs to be integer/float values separated by whitespace (but CSV requires only a small tweak), with each
data point on a single line. The header is as follows:<br>
<# Data Points> <# Dimensions + 1 (The last dimension of each line is Class Label> <# Clusters, K><br>
The data sets used for this implementation are part of the UC Irvine Machine Learning Repository.

