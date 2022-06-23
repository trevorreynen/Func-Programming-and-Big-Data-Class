// CalculateFriendsByAgeDataset.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/23/2022
// Trevor Reynen

// In this lab, we will use SQL functions in the Dataset to compute the average number of friends
// by age in a social network.
// In the previous lab, we used the RDD to solve the same problem.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object CalculateFriendsByAgeDataset {

    // Create case class to define schema of Friends.
    case class Friends(id: Int, name: String, age: Int, friends: Long)

    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create a Spark Session using every core of the local machine.
        val spark = SparkSession
            .builder()
            .appName("FriendsByAge")
            .master("local[*]")
            .getOrCreate()

        // Load each line of friends-header.csv into a Dataset.
        import spark.implicits._
        val ds = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/friends-header.csv")
            .as[Friends] // Convert DataFrame to Dataset.

        // Select only age and numFriends columns.
        val friendsByAge = ds.select("age", "friends")

        // From friendsByAge, we group by "age" and then compute average. Here, we use one line of
        // SQL code to finish the complex job using RDD. This example shows that SQL functions of
        // a dataset can make your tasks much simpler, but under the hood, everything is
        // implemented by the RDD function in the lower level.
        friendsByAge.groupBy("age").avg("friends").show()

        // Sort by age.
        friendsByAge.groupBy("age").avg("friends").sort("age").show()

        // Round the average number to 2 decimal places to produce better format.
        friendsByAge.groupBy("age").agg(round(avg("friends"), 2)).sort("age").show()

        // We can customize the column name as well using alias.
        friendsByAge.groupBy("age").agg(round(avg("friends"), 2).alias("friends_avg")).sort("age").show()

        spark.stop()

    }
}

