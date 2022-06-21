// DataFrame_Overview.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/22/2022
// Trevor Reynen

// In the next few labs, we will discuss DataFrames. DataFrame is the foundation for machine
// learning library offered by Spark.

// In Spark, DataFrames are the distributed collections of data, organized into rows and columns.
// Each column in a DataFrame has a name and an associated type.

// DataFrames are similar to traditional database tables, which are structured and concise.
// We can say that DataFrames are relational databases with better optimization techniques.

// RDD is resilient distributed dataset. RDD is less efficient in terms of storage and computation
// compared to DataFrame.

// Getting started with DataFrames
//   In this lecture, we discuss how to create a DataFrame from a csv file. Also, how to select
//   a column, rename a column, and create a new column for DataFrame.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import Utilities._


object DataFrame_Overview {
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

        // Create a DataFrame from Spark Session.
        // .option("header", "true") = Will treat first row as a header row (gets column names).
        // .option("inferSchema", "true") = Will infer data type for each column.
        // Spark can infer the Schema based on objects or elements inside the data source.
        val df = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/CitiGroup2006_2008")


        // Print Schema (column names and their data type)
        println("DataFrame Schema")
        df.printSchema() // Prints the schema to the console in a nice tree format.


        println("Describe and show stats of Numerical Columns")
        df.describe().show()


        println("Column names: " + df.columns.mkString(", ") + "\n")


        // Get first 10 rows.
        println("First 10 rows of DataFrame:")
        for (row <- df.head(10)) println(row)


        println("\nSelecting column(s) called Volume")
        df.select("Volume").show(5)


        // Converts $"col name" into a Column. See example below.
        import spark.implicits.StringToColumn

        println("Selecting multiple columns. In this case, columns Date and Close")
        df.select($"Date", $"Close").show(2)


        println("Creating new column(s) with a new DataFrame")
        val df2 = df.withColumn("HighPlusLow", $"High" + $"Low")


        println("New DataFrame column names: " + df2.columns.mkString(", ") + "\n")
        println("New DataFrame Schema:")
        df2.printSchema()

        println("First 5 rows of new DataFrame:")
        df2.head(5).foreach(println)

        println("\nRenaming column(s) (and selecting close column)")
        // You can use $ notation $"Close" or df2("Close") to select column.
        df2.select(df2("HighPlusLow").as("HPL"), df2("Close")).show()

    }
}

