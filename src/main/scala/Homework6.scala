// Homework6.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/29/2022
// Trevor Reynen

// ==========<  Homework 6  >==========
// Count up how many of each star rating exists in the MovieLens 100k data set.

// Create a case class with Schema of u.data.
// (userID: Int, movieID: Int, rating: Int, timestamp: Long)


// 1. Create Schema when reading u.data.
/*
val userRatingsSchema = new StructType()
    .add("userID", IntegerType, nullable =true)
    .add("movieID", IntegerType, nullable = true)
    .add("rating", IntegerType, nullable = true)
    .add("timestamp", LongType, nullable = true)
*/
// 2. Load up the data into a Spark Dataset.
// 3. Use tab as separator "\t", load Schema from userRatingsSchema and force case class to read
// it as Dataset.
// 4. Select only ratings column (The file format is userID, movieID, rating, timestamp).
// 5. Count up how many times each value (rating) occurs using groupBy and count.
// 6. Sort the resulting Dataset by count column.
// 7. Print results from the Dataset.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.types.{ IntegerType, LongType, StructType }


object Homework6 {

    // Case class is used to define Schema for our database.
    // Each field corresponds to the column name and column type in our database. This is required
    // to convert DataFrame to Dataset for compile time type check and optimization.
    case class Movie(userID: Int, movieID: Int, rating: Int, timestamp: Long)


    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        val spark = SparkSession
            .builder()
            .appName("Homework6")
            .master("local[*]")
            .getOrCreate()


        // 1. Create Schema when reading u.data.
        val userRatingsSchema = new StructType()
            .add("userID", IntegerType, nullable =true)
            .add("movieID", IntegerType, nullable = true)
            .add("rating", IntegerType, nullable = true)
            .add("timestamp", LongType, nullable = true)


        // 2. Load up the data into a Spark Dataset.
        //val movieObjectRDD = spark.sparkContext
        //    .textFile("./assets/ml-100k/u.data")
        //    .map(x => Movie(x.split("\t")(1).toInt))


        // 3. Use tab as separator "\t", load Schema from userRatingsSchema and force case class
        // to read it as Dataset.


        // 4. Select only ratings column (The file format is userID, movieID, rating, timestamp).


        // 5. Count up how many times each value (rating) occurs using groupBy and count.


        // 6. Sort the resulting Dataset by count column.


        // 7. Print results from the Dataset.

    }
}

