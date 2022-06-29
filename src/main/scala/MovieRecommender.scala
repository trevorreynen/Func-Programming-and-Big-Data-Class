// MovieRecommender.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/30/2022
// Trevor Reynen

// Today we will discuss the recommendation system. Recommendation system is very important for any
// E-commerce website.

// In this lab, we will run ALS (alternative least square) recommendations with Spark machine
// learning library.

// We will use MovieLens Dataset to perform movie recommendations.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.functions._
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession


object MovieRecommender {
    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()

        // Load DataFrame from movie_ratings.csv
        val ratings = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/movie_ratings.csv")

        // Show create training and testing Dataset.
        ratings.head()
        ratings.printSchema()

        // Create training and testing Dataset.
        val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

        // Build the recommendation model using ALS on the training data.
        val als = new ALS()
            .setMaxIter(5)
            .setRegParam(0.01)
            .setUserCol("userId")
            .setItemCol("movieId")
            .setRatingCol("rating")

        // Fit our model on the training set.
        val model = als.fit(training)

        // Produce our prediction of user ratings using testset.
        val predictions = model.transform(test)

        // Display our predicted rating.
        predictions.show()

        import spark.implicits.StringToColumn
        // Import to use abs().

        // To calculate difference between predicted value and real rating.
        val error = predictions.select(abs($"rating" - $"prediction"))

        // Drop null value and show statistics of our error data.
        error.na.drop().describe().show()

        spark.stop()

        /* Output:

        root
         |-- userId: integer (nullable = true)
         |-- movieId: integer (nullable = true)
         |-- rating: double (nullable = true)

        +------+-------+------+----------+
        |userId|movieId|rating|prediction|
        +------+-------+------+----------+
        |   575|    148|   4.0|       NaN|
        |   534|    463|   4.0| 3.4229622|
        |   311|    463|   3.0|  3.038836|
        |   548|    471|   4.0|  3.440305|
        |   602|    471|   3.0| 4.5417967|
        |   274|    471|   5.0| 4.3988004|
        |   299|    471|   4.5| 4.4454627|
        |   309|    471|   4.0|  3.934402|
        |   607|    471|   4.0|  3.751693|
        |   358|    471|   5.0| 3.2136781|
        |   502|    471|   4.0| 4.1851597|
        |    73|    471|   4.0| 3.9701378|
        |   195|    471|   3.0|  2.503516|
        |   497|    496|   2.0| 3.1306243|
        |   296|    833|   4.5| 2.3329756|
        |   463|   1088|   3.0| 2.7709346|
        |   607|   1088|   2.0| 2.8879004|
        |   358|   1088|   3.0| 3.0559888|
        |   505|   1088|   4.0| 3.1469698|
        |   387|   1088|   4.0| 2.5958874|
        +------+-------+------+----------+
        only showing top 20 rows

        +-------+--------------------------+
        |summary|abs((rating - prediction))|
        +-------+--------------------------+
        |  count|                     19199|
        |   mean|        0.8409131571311524|
        | stddev|        0.7388450651247095|
        |    min|        6.4849853515625E-5|
        |    max|         8.100225925445557|
        +-------+--------------------------+


        */

    }
}

