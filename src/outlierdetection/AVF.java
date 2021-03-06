package outlierdetection;




import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.Serializable;
//scala imports
import java.util.Iterator;
import scala.Tuple2;    
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;




//in this file we will apply AVF Algorithme on our data 
@SuppressWarnings("deprecation")
public class AVF {

    private static final Logger THE_LOGGER = Logger.getLogger(AVF.class);


    static class TupleComparatorAscending
            implements Comparator<Tuple2<String,Double>>, Serializable {
        final static TupleComparatorAscending INSTANCE = new TupleComparatorAscending();
        // sort descending based on the double value
        @Override
        public int compare(Tuple2<String,Double> t1,
                           Tuple2<String,Double> t2) {
            return (t1._2.compareTo(t2._2)); // sort based on AVF Score
        }

    }

    static class TupleComparatorDescending
            implements Comparator<Tuple2<String,Double>>, Serializable {
        final static TupleComparatorDescending INSTANCE = new TupleComparatorDescending();
        // sort descending based on the double value
        @Override
        public int compare(Tuple2<String,Double> t1,
                           Tuple2<String,Double> t2) {
            return -(t1._2.compareTo(t2._2)); // sort based on AVF Score
        }
    }


    /**
     * @param args arguments to run the program
     *
     * args[0] = K > 0, select K smallest outliers
     * args[1] = input path
     */
    public static void main(String[] args) throws Exception {
        //
        // Step-1: handle input parameters
        // make sure we have 2 arguments

        final int K = 70;
        String current = System.getProperty("user.dir");
        
        final String inputPath =  current + "/src/data/out/updatenewyork.csv";
       // final String inputPath =  current + "/src/data/out/newdata.csv";

        THE_LOGGER.info("K="+K);
        THE_LOGGER.info("inputPath="+inputPath);


        // Step-2: create a spark context and then read input and create the first RDD
        // create a context object, which is used
        // as a factory for creating new RDDs
        JavaSparkContext context = new JavaSparkContext("local", "OUtlier detection finally");
        //read input (as categorical dataset) and create the first RDD
        JavaRDD<String> data = context.textFile(inputPath);
        final String header = data.first();
        JavaRDD<String>  records = data.filter(line -> !line.contains(header));    
       
        records.cache(); // cache it: since we are going to use it again


   
        
        // Step-3: perform the map() for each RDD element
        // for each input record of: <record-id><,><data1><,><data2><,><data3><,>...
        //    emit(data1, 1)
        //    emit(data2, 1)
        //    emit(data3, 1)
        //    ...
        //
        // PairFunction<T, K, V>
        // T => Tuple2<K, V>
        JavaPairRDD<String,Integer> ones = records.flatMapToPair(new PairFlatMapFunction<
                                String,       // T = input record
                                String,       // K = categorical data value
                                Integer       // V = 1
                                >() {
            @Override
            public Iterator<Tuple2<String,Integer>> call(String rec) {
                //
                List<Tuple2<String,Integer>> results = new ArrayList<Tuple2<String,Integer>>();
                // rec has the following format:
                // <record-id><,><data1><,><data2><,><data3><,>...
                String[] tokens = StringUtils.split(rec, ",");
                //for (int i=23; i < tokens.length; i++) {
                for (int i=1; i < tokens.length; i++) {

                    results.add(new Tuple2<String,Integer>(tokens[i], 1));
                }
                return results.iterator();
            }
        });

        // Step-4: find frequencies of all categirical data (keep categorical-data as String)
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        // Step-5: build an associative array to be used for finding AVF Score
        // public java.util.Map<K,V> collectAsMap()
        // Return the key-value pairs in this RDD to the master as a Map.
        final Map<String, Integer> map = counts.collectAsMap();


        // Step-6: compute AVF Score using the built associative array
        JavaPairRDD<String,Double> avfScore = records.mapToPair(new PairFunction
                <
                        String,        // T = input record
                        String,        // K = record-id
                        Double         // V = avf score
                        >() {
            @Override
            public Tuple2<String,Double> call(String rec) {
                //
                // rec has the following format:
                // <record-id><,><data1><,><data2><,><data3><,>...
                String[] tokens = StringUtils.split(rec, ",");
                String recordID = tokens[0];
                int sum = 0;
               // for (int i=25; i < tokens.length; i++) {
                for (int i=1; i < tokens.length; i++) {
                sum += map.get(tokens[i]);
                }
                double m = (double) (tokens.length -1);
                double avfScore = ((double) sum) / m;
                return new Tuple2<String,Double>(recordID, avfScore);
            }
        });

        // Step-7: take the lowest K AVF scores
        // java.util.List<T> takeOrdered(int K)
        // Returns the first K (smallest) elements from this RDD using
        // the natural ordering for T while maintain the order.
        List<Tuple2<String,Double>> outliers = avfScore.takeOrdered(K, TupleComparatorAscending.INSTANCE);
        System.out.println("--------------");
        System.out.println("Ascending AVF Score:");
        System.out.println(outliers);

        

        //System.out.println("--------------");
        //List<Tuple2<String,Double>> outliers2 = avfScore.takeOrdered(K, TupleComparatorDescending.INSTANCE);
        //System.out.println("descending");
       //System.out.println(outliers2);
      

        // Step-8: done & close the spark context
        context.close();
    }

}
