package outlierdetection;


import java.io.*;
import java.util.List;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

// $example on$
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import scala.Tuple2;
// $example off$





//in this file we will make a basic outlier detection method using mean value and standar Standard deviation

public class OutlierStandarDetection {

    public static void main(String[] args) throws FileNotFoundException {
        String current = System.getProperty("user.dir");
        String csvFile = current + "/src/data/in/updatenewyork.txt";
      


        JavaSparkContext jsc = new JavaSparkContext("local", "Anomaly Detection");

        //Varible for reading the csv file
        JavaRDD<Vector> kddRDD = jsc.textFile(csvFile).map(new Function<String, Vector>() {
            public Vector call(String line) throws Exception {
                String[] kddArr = line.split(",");

                double[] values = new double[71];
                for (int i = 0; i < 71; i++) {
                    values[i] = Double.parseDouble(kddArr[25]);
                }
                return Vectors.dense(values);
            }
        }).cache();


        System.out.println("KDD data row size : " + kddRDD.count());
        System.out.println("Example data : " + kddRDD.first());


        JavaDoubleRDD firstColumn = kddRDD.mapToDouble(new DoubleFunction<Vector>() {
            public double call(Vector t) throws Exception {
                // TODO Auto-generated method stub
                return t.apply(0);
            }
        });

        final double mean = firstColumn.mean();
        final double stdev = firstColumn.stdev();

        System.out.println("Meaning value : " + mean + " Standard deviation : " + stdev + " Max : " + firstColumn.max() + " Min : " + firstColumn.min());



        JavaRDD<Vector> filteredKddRDD = kddRDD.filter(new Function<Vector, Boolean>() {

            public Boolean call(Vector v1) throws Exception {
                double src_bytes = v1.apply(0);
                if (src_bytes > (mean - 2 * stdev) && src_bytes < (mean + 2 * stdev)) {
                    return true;
                }
                return false;
            }
        }).cache();


        System.out.println("Filtered data ...  Count : " + filteredKddRDD.count());
        System.out.println("Example data : " + filteredKddRDD.first());
        final int numClusters = 10;
        final int numIterations = 20;
        final KMeansModel clusters = KMeans.train(filteredKddRDD.rdd(), numClusters, numIterations);


        /**
         * Take cluster centers
         */
        final Vector[] clusterCenters = clusters.clusterCenters();

        JavaPairRDD<Double,Vector> result1 = kddRDD.mapToPair(new PairFunction<Vector, Double,Vector>() {
            public Tuple2<Double, Vector> call(Vector point) throws Exception {
                int centroidIndex = clusters.predict(point);  //find centroid index
                Vector centroid = clusterCenters[centroidIndex]; //get cluster center (centroid) for given point
                //calculate distance
                double preDis = 0;
                for(int i = 0 ; i < centroid.size() ; i ++){
                    preDis = Math.pow((centroid.apply(i) - point.apply(i)), 2);

                }
                double distance = Math.sqrt(preDis);
                return new Tuple2<Double, Vector>(distance, point);
            }
        });

        List<Tuple2<Double, Vector>> result = result1.sortByKey(false).take(10);

        //Print top ten points
        for(Tuple2<Double, Vector> tuple : result){
            System.out.println("Distance " + tuple._1());
        }

    }
    }