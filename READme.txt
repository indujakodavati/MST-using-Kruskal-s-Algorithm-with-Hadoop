Project: Kruskals Algorithm

Problem Statement:
	You are given a large sparse weighted graph containing millions of node and you need to extract a minimum spanning tree from the graph. 
	You decided to use a Kruskals algorithm as the graph is sparse in nature. However, due to the size of the graph, a single user system 
	is not good enough to perform the task. You decided to use a map reduce framework to solve the problem. Write the map-reduce framework for 
	the Kruskal algorithm to find the minimum spanning tree. 

Dataset:
	the input graph is stored in the provided file in the following format. 
	<sourcenodeid>,<weight>,<destinationnode> 

Requirements:
	- Hadoop: hadoop-3.2.1
	- Java: openjdk version "1.8.0_352"

Contents:
	- Java Code: KruskalAlgorithm.java
	- jar file: KruskalAlgorithmj.jar
	- Folder "KA" containing .class files
	- input_folder: kruskalinput (has input.txt)
	- output_file: output.txt
	- Readme.txt file
	
Executed Commands:
	in sbin of hadoop:
	- ./start-dfs.sh
	- ./start-yarn.sh
	- hdfs dfs -mkdir /myKruskalinput
	- hdfs dfs -put /home/aslesha/Documents/SEM6/Kruskal_MST/kruskalinput /myKruskalinput
	
	- cd /home/aslesha/Documents/SEM6/Kruskal_MST/
	
	to create .class files
	- javac -classpath $HADOOP_HOME/share/hadoop/common/hadoop-common-3.2.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-3.2.1.jar:$HADOOP_HOME/share/hadoop/common/lib 		
	commons-cli-1.2.jar -d /home/aslesha/Documents/SEM6/Kruskal_MST *.java

	to create jar file
	- jar -cvf KruskalAlgorithmj.jar -C /home/aslesha/Documents/SEM6/Kruskal_MST/KA .
	
	in sbin of hadoop:
	- hadoop jar /home/aslesha/Documents/SEM6/Kruskal_MST/KruskalAlgorithmj.jar KruskalAlgorithm /myKruskalinput/kruskalinput/ output
	- download the output file (part-r-00000) from (http://localhost:9870/explorer.html#/user/aslesha/output)
	- ./stop-dfs.sh
	- ./stop-yarn.sh

Output Format:
	the output has the source and destination nodes of the MST.
	- <sourcenodeid> <destinationnode>
	
	
	
	
