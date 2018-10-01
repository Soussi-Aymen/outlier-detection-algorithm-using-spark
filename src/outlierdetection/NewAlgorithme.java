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

public class NewAlgorithme {

	public static void main(String[] args) {
			//step-1 the data path
	        String current = System.getProperty("user.dir");
	        //final String inputPath =  current + "/src/data/in/updatenewyork.txt";
	        final String inputPath =  current + "/src/data/out/updatenewyork.csv";

	       


	        // Step-2: create a spark context and then read input and create the first RDD
	        // create a context object, which is used
	        // as a factory for creating new RDDs
	        JavaSparkContext context = new JavaSparkContext("local", "New Outlier Detection Algorithm");
	        //read input (as categorical dataset) and create the first RDD
	        JavaRDD<String> data = context.textFile(inputPath);
	        final String header = data.first();
	        JavaRDD<String>  records = data.filter(line -> !line.contains(header));    
	       
	        records.cache(); // cache it: since we are going to use it again

        
        
        
        // Step-8: done & close the spark context
        context.close();
		
		
	}

}
