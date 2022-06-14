// CalculateFriendsByAge.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/15/2022
// Trevor Reynen

// This lab will compute the average number of friends by age in a social network.
// Each row in our dataset contains ID, first name, age, and number of friends.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext


object CalculateFriendsByAge {

    // A function that splits a line of input into (age, numFriends) tuples.
    // Please implement this function by yourself.
    def parseLine(line: String): (Int, Int) = {
        // Split the line by commas.
        val element = line.split(",")

        // Extract the age and numFriends fields and convert to integers.
        val age = element(2).toInt
        val numFriends = element(3).toInt

        // Return key/value pairs. Key is age and value is numFriends.
        (age, numFriends)
    }


    // Our main function where the application happens.
    def main(args: Array[String]): Unit = {

        // Set log level to only print errors. Otherwise, it will print too much information.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create SparkContext using every core of the local machine.
        val sc = new SparkContext("local[*]", "FriendsByAge")

        // Load each line of the source data into an RDD.
        val lines = sc.textFile("./assets/friends.csv")

        // Use our parseLines function to convert each line to (age, numFriends) tuples.
        // Finally we produce key/value RDD.
        // Key is age, value is numFriends.
        val rdd = lines.map(parseLine)

        // We are starting with an RD of form (age, numFriends).
        // Where age is KEY and numFriends is VALUE.
        // We use mapValues to convert each numFriends value to a tuple of (numFriends, 1)
        // Then we use reduceByKey to sum up the total numFriends and total instances for each age.
        val totalsByAge = rdd
            .mapValues(x => (x, 1))
            .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

        // So now we have tuples of (age, (totalFriends, totalInstances)).
        // Age is key, tuple (totalFriends, totalInstances) is value.

        // To compute the average number of friends, we divide totalFriends / totalInstances
        // for each age.
        val averageByAge = totalsByAge.mapValues(x => x._1 / x._2)

        // Collect the results from the RDD.
        // (This kicks off computing the DAG and actually executes the job).
        val results = averageByAge.collect()

        // Sort and print the final results.
        results.sorted.foreach(println)
        // Output:
        // (18,343)
        // (19,213)
        // (20,165)
        // ...
        // ...
        // (67,214)
        // (68,269)
        // (69,235)



        // ==========<  EXERCISE  >==========
        // Show the average number of friends by the first name.

        def parseLineExercise(line: String): (String, Int) = {
            // Split the line by commas.
            val element = line.split(",")

            // Extract the age and numFriends fields and convert to integers.
            val name = element(1).toString
            val numFriends = element(3).toInt

            // Return key/value pairs. Key is age and value is numFriends.
            (name, numFriends)
        }

        val exerciseRDD = lines.map(parseLineExercise)

        val totalsByName = exerciseRDD
            .mapValues(x => (x, 1))
            .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

        val averageByName = totalsByName.mapValues(x => x._1 / x._2)

        val exerciseResults = averageByName.collect()

        println("\nExercise - Avg num of friends by name:")
        exerciseResults.sorted.foreach(println)
        // Output:
        // (Ben,287)
        // (Beverly,306)
        // (Brunt,218)
        // ...
        // ...
        // (Weyoun,245)
        // (Will,279)
        // (Worf,296)


    }
}

