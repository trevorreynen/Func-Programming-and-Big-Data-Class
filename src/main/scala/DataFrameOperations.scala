// DataFrameOperations.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/22/2022
// Trevor Reynen

// DataFrame operation can use either Scala syntax or SQL syntax.
// In this lecture, we discuss the filter operation.


// Imports.
import org.apache.spark.sql.SparkSession
import Utilities._


object DataFrameOperations {
    def main(args: Array[String]): Unit = {

        // Start a simple Spark Session.
        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()

        setupLogging()

        val df = spark.read.option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/CitiGroup2006_2008")

        println("Schema")
        df.printSchema()
        println()

        // This import is needed to use the $-notation abstract class SQLImplicits.
        import spark.implicits.StringToColumn

        println("Filtering Operation")
        println("Grabbing all rows where a column meets a condition")

        // There are two main ways to filter out the data.
        // One way is to use Spark SQL syntax, the other way is to use Scala syntax.
        // We mainly stick to Scala notation.

        // Get rows with closing price greater than $480.
        df.filter($"Close" > 480).show(5)
        println()

        println("SQL Notation")
        // Can also use SQL notation (you pass in where condition).
        df.filter("Close > 480").show(5)
        println()

        println("Count how many results")

        // Filter is transformation on DataFrame.
        // Count is action on DataFrame.
        // After filter operation, we will count how many rows we get.
        println(df.filter($"Close" > 480).count())
        println()

        // Note the use of triple ===, this may change in the future.
        println("Get only one row satisfying certain condition")
        df.filter($"High" === 484.40).show(4)
        //df.filter("High = 484.40").show() // SQL notation.
        println()

        // Can also use SQL notation.
        //df.filter("High = 484.40").count()

        // Multiple Filters.
        println("Multiple filters with multiple conditions")
        df.filter($"Close" < 480 && $"High" < 480).show(5)
        println()

        // Can also use SQL notation.
        println("SQL Notation")
        df.filter("Close < 480 AND High , 484.40").show(5)

        println("Collect results into a Scala object (Array)")
        // Row object can be indexed to access each column.
        val High484 = df.filter($"High" === 484.40).collect()
        High484.foreach(println)
        println()

        import org.apache.spark.sql.functions.corr

        // Operations and Useful Functions.
        // http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.functions$

        // High and low are highly correlated (0.999)
        // Within certain days, high price and low price are roughly going up and down together.
        println("Pearson Correlation between two column")
        df.select(corr("High", "Low")).show()

    }
}

