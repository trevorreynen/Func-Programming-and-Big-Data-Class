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
import org.apache.spark.sql.SparkSession
import Utilities._


object DataFrame_Overview {
    def main(args: Array[String]): Unit = {

        // Start a simple Spark Session.
        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()

        setupLogging()

        // Create a DataFrame from Spark Session.
        // header = true will treat first row as header (get column name).
        // inferSchema = true will infer type for each column.
        // Spark can infer the Schema based on objects or elements inside the data source.
        val df = spark.read.option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/CitiGroup2006_2008")

        // Find out DataTypes.
        // Print Schema (column name and its data type)
        println("Schema")
        df.printSchema()
        println()

        println("Describe Show Statistics of Numerical Columns")
        df.describe().show()
        println()

        println("Column Name")
        println(df.columns.mkString(", "))
        println()

        // Get first 10 rows.
        println("First 10 rows")
        for (row <- df.head(10)) println(row)
        println()

        println("Select columns called Volume")
        df.select("Volume").show(5)
        println()

        import spark.implicits.StringToColumn // Converts $"col name" into a Column. See line 72.

        println("Select multiple columns Date and Close")
        df.select($"Date", $"Close").show(2)
        println()

        println("Creating New Columns with New Dataframe")
        val df2 = df.withColumn("HighPlusLow", $"High" + $"Low")
        println(df2.columns.mkString(", "))
        println()
        df2.printSchema()
        println()

        df2.head(5).foreach(println)
        println()

        println("Renaming Columns (and selecting close column)")
        // You can use $ notation $"Close" or df2("Close") to select column.
        df2.select(df2("HighPlusLow").as("HPL"), df2("Close")).show()

    }
}

