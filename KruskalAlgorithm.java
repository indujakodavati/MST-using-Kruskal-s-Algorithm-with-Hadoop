import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class KruskalAlgorithm {
    
    // Custom data types for edges and nodes
    private static class Edge implements Comparable<Edge> {
        int u, v, weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    private static class Node {
        int parent, rank;

        public Node(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    // Mapper for reading in edges and emitting them with their weights as keys
    public static class EdgeMapper extends Mapper<Object, Text, IntWritable, Text> {
        private IntWritable weight = new IntWritable();
        private Text edge = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[2]);
            int weight = Integer.parseInt(parts[1]);

            // Emit edge with weight as key
            this.weight.set(weight);
            this.edge.set(String.format("%d %d", u, v));
            context.write(this.weight, this.edge);
        }
    }

    // Reducer for processing edges and outputting the minimum spanning tree
    public static class MSTReducer extends Reducer<IntWritable, Text, NullWritable, Text> {
        private Map<Integer, Node> nodes = new HashMap<Integer, Node>();

        public void reduce(IntWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            // Iterate over edges with the same weight
            for (Text edge : values) {
                String[] parts = edge.toString().split("\\s+");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);

                // Find parent nodes of u and v
                int pu = find(u);
                int pv = find(v);

                // If u and v are in different components, add edge to minimum spanning tree
                if (pu != pv) {
                    nodes.get(pu).parent = pv;
                    context.write(NullWritable.get(), edge);
                }
            }
        }

        // Helper function to find the parent of a node and perform path compression
        private int find(int x) {
            if (!nodes.containsKey(x)) {
                nodes.put(x, new Node(x, 0));
            }

            if (nodes.get(x).parent != x) {
                nodes.get(x).parent = find(nodes.get(x).parent);
            }

            return nodes.get(x).parent;
        }
    }

    // Main function to set up and run the job
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Kruskal's Algorithm");

        // Set job classes
    job.setJarByClass(KruskalAlgorithm.class);
    job.setMapperClass(EdgeMapper.class);
    job.setReducerClass(MSTReducer.class);

    // Set input and output paths
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    // Set output key and value classes
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);

    // Run job and wait for completion
    System.exit(job.waitForCompletion(true) ? 0 : 1);
}

}

