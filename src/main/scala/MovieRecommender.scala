// MovieRecommender.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/30/2022
// Trevor Reynen

// Today we will discuss the recommendation system. Recommendation system is very important for any
// E-commerce website.

// In this lab, we will run ALS (alternative least square) recommendations with Spark machine
// learning library.

//We will use MovieLens Dataset to perform movie recommendations.


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

    }
}

