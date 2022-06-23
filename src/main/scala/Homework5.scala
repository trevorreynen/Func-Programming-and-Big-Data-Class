// Homework5.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/27/2022
// Trevor Reynen

// ==========<  Homework 5  >==========
// Use the Netflix_2011_2016.csv file given to complete Homework 5.

//// 1. Start a simple Spark Session.
//// 2. Load the Netflix_2011_2016.csv file, having Spark infer the data types.
////     Q1: What are the column names?
//     A1:
////     Q2: What does the Schema look like?
//     A2:
//// 3. Print out the first 5 columns.
//// 4. Use describe() to learn about the DataFrame.
//// 5. Create a new DataFrame with a column called HV Ratio that is the ratio of the High Price
//// versus volume of stock traded for a day.
//     Q3: What day had the Peak High in Price?
//     A3:
////     Q4: What is the mean of the Close column?
//     A4:
////     Q5: What is the max and min of the Volume column?
//     A5:
// For Scala/Spark $ Syntax
// How many days was the Close lower than $ 600?
// What percentage of the time was the High greater than $500 ?
// What is the Pearson correlation between High and Volume?
// What is the max High per year?
// What is the average Close for each Calender Month?


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import Utilities._


object Homework5 {
    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        // 1. Start a simple Spark Session.
        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()


        // 2. Load the Netflix_2011_2016.csv file, having Spark infer the data types.
        val df = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/Netflix_2011_2016.csv")


        //  Q1: What are the column names?
        println("Q1: What are the column names?\n" + df.columns.mkString(", ") + "\n")
        // Date, Open, High, Low, Close, Volume, Adj Close


        //  Q2: What does the Schema look like?
        println("Q2: What does the Schema look like?")
        df.printSchema()
        /*
            root
             |-- Date: string (nullable = true)
             |-- Open: double (nullable = true)
             |-- High: double (nullable = true)
             |-- Low: double (nullable = true)
             |-- Close: double (nullable = true)
             |-- Volume: integer (nullable = true)
             |-- Adj Close: double (nullable = true)
        */


        println("3. Print out the first 5 columns.")
        for (row <- df.head(5)) println(row)
        /* NOTE: For saving space, I rounded all decimals just for this comment.
            [2011-10-24, 119.10, 120.28, 115.10, 118.84, 120460200, 16.98]
            [2011-10-25, 74.90, 79.39, 74.25, 77.37, 315541800, 11.05]
            [2011-10-26, 78.73, 81.42, 75.40, 79.40, 148733900, 11.34]
            [2011-10-27, 82.18, 82.72, 79.25, 80.86, 71190000, 11.55]
            [2011-10-28, 80.28, 84.66, 79.60, 84.14, 57769600, 12.02]
        */


        println("4. Use describe() to learn about the DataFrame.")
        df.describe().show()



        println("\n5. Create a new DataFrame with a column called HV\n" +
                "Ratio that is the ratio of the High Price versus\n" +
                "volume of stock traded for a day.")
        val df2 = df.withColumn("HV Ratio", df("High") + df("Volume"))


        //df2.printSchema()

        //df2.head(5).foreach(println)


        //  Q3: What day had the Peak High in Price?




        //  Q4: What is the mean of the Close column?
        println("\nQ4: What is the mean of the Close column?")
        //val dfmean = df.groupBy("Close").mean()
        df.agg(round(avg("Close"), 2)).show()


        //  Q5: What is the max and min of the Volume column?
        println("Q5: What is the max of the Volume column?")
        df.select(max("Volume")).show()

        println("Q5: What is the min of the Volume column?")
        df.select(min("Volume")).show()


        // For Scala/Spark $ Syntax
        import spark.implicits._

        // How many days was the Close lower than $ 600?
        println("How many days was the Close lower than $ 600?  " + df.filter($"Close" < 600).count() + "\n")

        // What percentage of the time was the High greater than $500 ?


        // What is the Pearson correlation between High and Volume?


        // What is the max High per year?


        // What is the average Close for each Calender Month?


        spark.stop()

    }
}

