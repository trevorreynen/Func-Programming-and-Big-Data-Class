// DataFrameOperations.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/22/2022
// Trevor Reynen

// DataFrame operation can use either Scala syntax or SQL syntax.
// In this lecture, we discuss the filter operation.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import Utilities._


object DataFrameOperations {
    def main(args: Array[String]): Unit = {

        // Teacher was using the setupLogging() function from Utilities.scala, but I realized
        // it doesn't work. So, instead I just add the line below it which does the same thing.
        //setupLogging()
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Start a simple Spark Session.
        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()

        val df = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/CitiGroup2006_2008")

        println("DataFrame Schema")
        df.printSchema()

        import spark.implicits.StringToColumn

        println("The Filtering Operation")
        println("Grabbing all rows where a column meets a condition.\n")

        // There are two main ways to filter out data.
        // One way is to use Spark SQL syntax, the other way is to use Scala syntax.
        // We mainly stick to Scala notation.


        // Get rows with closing price greater than $480.
        println("Get rows with Close column price greater than $480." + "\n")
        //println("Using Scala Notation")
        df.filter($"Close" > 480).show(5)

        // Can also use SQL notation.
        //println("Using SQL Notation")
        //df.filter("Close > 480").show(5)


        // Filter is transformation on DataFrame. Count is action on DataFrame.
        // After filter operation, we will count how many rows we get.
        println("Count how many results: " + df.filter($"Close" > 480).count() + "\n")


        // Note the use of triple ===, this may change in the future.
        println("Get only one row satisfying a certain condition.")
        df.filter($"High" === 484.40).show(4)
        //df.filter("High = 484.40").show() // SQL Notation.


        // Multiple Filters.
        println("Multiple filters with multiple conditions.")
        println("Using Scala Notation")
        df.filter($"Close" < 480 && $"High" < 480).show(5)

        // Can also use SQL notation.
        println("Using SQL Notation")
        df.filter("Close < 480 AND High < 484.40").show(5)


        println("Collect results into a Scala Object (Array).")
        // Row object can be indexed to access each column.
        val High484 = df.filter($"High" === 484.40).collect()

        High484.foreach(println)
        println()

        import org.apache.spark.sql.functions.corr

        // Operations and Useful Functions.
        // https://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/functions$.html

        // High and low are highly correlated (0.999)
        // Within certain days, high price and low price are roughly going up and down together.
        println("Pearson Correlation between two columns:")
        df.select(corr("High", "Low")).show()

    }
}

