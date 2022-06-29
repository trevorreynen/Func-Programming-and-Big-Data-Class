// Homework6.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/29/2022
// Trevor Reynen

// ==========<  Homework 6  >==========
// Count up how many of each star rating exists in the MovieLens 100k data set.

// 1. Create a case class with Schema of u.data.   ~~~DONE~~~
// 2. Create Schema when reading u.data.   ~~~DONE~~~
/*
val userRatingsSchema = new StructType()
    .add("userID", IntegerType, nullable = true)
    .add("movieID", IntegerType, nullable = true)
    .add("rating", IntegerType, nullable = true)
    .add("timestamp", LongType, nullable = true)
*/
// 3. Load up the data into a Spark Dataset.   ~~~DONE~~~
// 4. Use tab as separator "\t", load Schema from userRatingsSchema and force case class to read
// it as Dataset.   ~~~DONE~~~
// 5. Select only ratings column (The file format is userID, movieID, rating, timestamp).   ~~~DONE~~~
// 6. Count up how many times each value (rating) occurs using groupBy and count.
// 7. Sort the resulting Dataset by count column.
// 8. Print results from the Dataset.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.types.{ IntegerType, LongType, StructType }


object Homework6 {

    // 1. Create a case class with Schema of u.data.
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


        // 2. Create Schema when reading u.data.
        val userRatingsSchema = new StructType()
            .add("userID", IntegerType, nullable = true)
            .add("movieID", IntegerType, nullable = true)
            .add("rating", IntegerType, nullable = true)
            .add("timestamp", LongType, nullable = true)


        // 3. Load up the data into a Spark Dataset.
        // 4. Use tab as separator "\t", load Schema from userRatingsSchema and force case class
        // to read it as Dataset.
        import spark.implicits._
        val userRatingsDS = spark.read
            .option("sep", "\t")
            .schema(userRatingsSchema)
            .csv("./assets/ml-100k/u.data")
            .as[Movie]


        // 5. Select only ratings column (The file format is userID, movieID, rating, timestamp).
        val ratingsColumn = userRatingsDS.select("rating")

        // 6. Count up how many times each value (rating) occurs using groupBy and count.
        val ratingsCount = ratingsColumn
            .groupBy("rating")
            .count()


        // 7. Sort the resulting Dataset by count column.
        val ratingsCountSorted = ratingsCount.sort("count")

        // 8. Print results from the Dataset.
        ratingsCountSorted.show(ratingsCountSorted.count.toInt)

        /* Output:

        +------+-----+
        |rating|count|
        +------+-----+
        |     1| 6110|
        |     2|11370|
        |     5|21201|
        |     3|27145|
        |     4|34174|
        +------+-----+

        */

    }
}

