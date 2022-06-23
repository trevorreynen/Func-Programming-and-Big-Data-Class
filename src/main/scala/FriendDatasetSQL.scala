// FriendDatasetSQL.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/23/2022
// Trevor Reynen

// In this lab, we will practice SQL functions for a Dataset. We will use our friends.csv file as
// the example. friend.csv has four columns (id, name, age, num of friends).


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._


object FriendDatasetSQL {

    // Case class is used to define Schema for our database.
    // Each field corresponds to the column name and column type in our database. This is required
    // to convert DataFrame to Dataset for compile time type check and optimization.
    case class Person(id: Int, name: String, age: Int, friends: Int)

    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        val spark = SparkSession
            .builder()
            .appName("SparkSQL")
            .master("local[*]")
            .getOrCreate()

        // Convert our csv file to a Dataset, using our Person case class to infer the schema.
        // This is used to implicitly infer schema.
        import spark.implicits._
        val friendDataset = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/friends-header.csv")
            .as[Person] // Convert DataFrame to Dataset.

        // There are a lot of other ways to make a DataFrame/Dataset.
        // For example, spark.read.json("json file path") or sqlContext.table("Hive table name").
        println("Here is our inferred schema:")
        friendDataset.printSchema()

        println("Let's select the name column:")
        friendDataset.select("name").show()

        println("Filter out anyone over 21:")
        // In Scala, you can pass the expression as a parameter to the function.
        friendDataset.filter(friendDataset("age") < 21).show()

        println("Group by age and count num of friends by age:")
        // SQL functions are much more simpler than RDD solution.
        friendDataset.groupBy("age").count().show()

        println("Make everyone 10 years older:")
        friendDataset.select(friendDataset("name"), friendDataset("age") + 10).show()

        // Stop Spark Session when we are done.
        spark.stop()

    }
}

