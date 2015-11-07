/* SimpleApp.java */
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SubgraphIsomorphism {
  public static void main(String[] args) {
    String logFile = "/home/iva/programming/subgraphIsomorphism/log"; // Should be some file on your system
    SparkConf conf = new SparkConf().setAppName("Simple Application");
    new SuperComplicatedVeryHard();
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> logData = sc.textFile(logFile).cache();

    long numAs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("c"); }
    }).count();

    long numBs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("b"); }
    }).count();

    System.out.println("Lines with c: " + numAs + ", lines with b: " + numBs);
  }
}
