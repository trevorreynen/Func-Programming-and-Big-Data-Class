// PopularMovieDataset.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/27/2022
// Trevor Reynen

// In this lab, we use Datasets instead of RDD.
// Dataset can optimize SQL query better than plain old RDD (Resilient Distributed Dataset).
// Dataset can represent data more efficiently than an RDD when data is in the structure format.
// Structure format means data is organized by column (such as excel or database file).
// Example of unstructured data is text, voice file, or picture.
// We convert our previous RDD solution to the Dataset solution.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import scala.io.{ Codec, Source }
import java.nio.charset.CodingErrorAction


object popularMovieDataset {

    // Load up a map of movies IDs to movie names.
    def loadMovieNames: Map[Int, String] = {

        // This will handle bad encoding.
        implicit val codec = Codec("utf-8")

        codec.onMalformedInput(CodingErrorAction.REPLACE)
        codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

        // Create a map in Ints to Strings and populate it from u.ite. Each line of u.item has
        // movieID and movie title. Map is immutable, but your reference is mutable.
        var movieNames: Map[Int, String] = Map()
        // Reading line by line from u.item. Each element of lines is one line.
        val lines = Source.fromFile("assets/ml-100k/u.item").getLines()

        // Going through each line and populate the map.
        for (line <- lines) {
            val field = line.split('|')

            // Confirm there is data in that row (no blank lines).
            if (field.length > 1) {
                // Mutable reference for each iteration. Create new Map object for each time.
                movieNames += (field(0).toInt -> field(1))
            }
        }

        movieNames // The Map object.
    }

    // Here, we use case class so that we can get a column name for our movieID. Final means
    // there is no subclass. Movie has one immutable instance variable called movieID.
    final case class Movie(movieID: Int)


    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create a Spark Session using every core of the local machine.
        val spark = SparkSession
            .builder()
            .appName("PopularMoviesDataset")
            .master("local[*]")
            .getOrCreate()

        // We extract movieID and construct RDD of Movie objects. We have to do it the old fashion
        // way using RDD since data is unstructured.

        // If we have JSON data or actual database to import data in. We can produce a Dataset
        // directly without the intermediate step.
        val movieObjectRDD = spark.sparkContext
            .textFile("./assets/ml-100k/u.data")
            .map(x => Movie(x.split("\t")(1).toInt))

        println("Collect sample data for movieObjectRDD.")
        movieObjectRDD.takeSample(false, 4).foreach(println)

        // Convert RDD into a Dataset.
        import spark.implicits._
        val movieDS = movieObjectRDD.toDS()

        // This is SQL-style magic to sort all movies by popularity in one line.
        // Remember for RDD, we get key-value RDD then use reduce operation.

        // Group the movie and count number of movies per group, then sort movie based on number.
        val topMovieIDs = movieDS.groupBy("movieID")
            .count()
            .orderBy(desc("count"))
            .cache()

        topMovieIDs.show()

        // Grab top 10 movies.
        val top10 = topMovieIDs.take(10)

        // Load up the movie ID -< name map.
        val names = loadMovieNames

        val res0 = names.take(4)
        println("Print 4 samples for movie dictionary map.")
        res0 foreach { case (movieID, name) => println (movieID + " --> " + name) }

        // Print the results for top 10 movies.
        for (result <- top10) {
            // Result is just a row at this point. We need to cast it back to Int.
            println(names(result(0).asInstanceOf[Int]) + ": " + result(1))
        }

    }
}

