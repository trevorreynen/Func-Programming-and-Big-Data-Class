// MovieRatingsCounter.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/15/2022
// Trevor Reynen

// We will get real movie rating data and use Spark to analyze it and produce a histogram of
// distribution of movie ratings.

// How many people rate movies five stars vs one stars.

// We will focus on u.data. u.data contains actual movie rating data.
// Each row has userID, movieID, ratings, time stamps.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext


object MovieRatingsCounter {
    def main(args: Array[String]): Unit = {

        // Set log level to only print errors. Otherwise, it will print too much information.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create SparkContext using every core of the local machine.
        // Master will specify which cluster you will run your big data application.
        val sc = new SparkContext("local[*]", "MovieRatingCounter")

        // Load up each line of ratings data into RDD.
        // RDD is called resilient distributed dataset.
        // Resilient means if some of machines fail, we can still recover the lost data.
        // Each element of RDD is one line of data.
        // Each line has userID, movieID. ratings, time stamps, split by tab.
        val linesRDD = sc.textFile("./assets/ml-100k/u.data")

        // For declarative style programming, implementation detail will depend on your app.
        // Split each line by tabs and extract their field (actual rating itself).
        val ratingsRDD = linesRDD.map(x => x.split("\t")(2))

        // Count up how many times each value (rating) occurs.
        val results = ratingsRDD.countByValue()

        // Sort the resulting map of (rating, count) tuples.
        // Map does not have sorting capability which is why we will convert map to Seq.
        //val sortedResults = results.toSeq.sortBy(x => x._1)
        val sortedResults = results.toSeq.sortBy { case (rating, count) => rating }

        // Map is transformation operation, which is lazy evaluation.
        // countByValue is action. Only action can trigger lazy evaluation.

        // Print results.
        sortedResults.foreach(println)
        // Output:
        // (1,6110)
        // (2,11370)
        // (3,27145)
        // (4,34174)
        // (5,21201)


    }
}

