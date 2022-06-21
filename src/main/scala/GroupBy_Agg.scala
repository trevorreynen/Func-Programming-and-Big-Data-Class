// GroupBy_Agg.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/22/2022
// Trevor Reynen

// DataFrame GroupBy and Aggregate Functions.
// Group rows by column and perform aggregate function on it.
// In this lecture, we discuss groupBy operation and perform aggregate function for DataFrame.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import Utilities.setupLogging


object GroupBy_Agg {

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
            .csv("./assets/Sales.csv")

        println("DataFrame Schema")
        df.printSchema()

        // GroupBy categorical columns (group rows by column)
        // Perform aggregate function in chunks based on what you GroupBy.
        // Spark will take any numerical column and take the average.

        // Mean sales across different companies.
        println(".groupBy(\"Company\").mean()")
        df.groupBy("Company").mean().show()

        println(".groupBy(\"Company\").count()")
        df.groupBy("Company").count().show()

        println(".groupBy(\"Company\").max()")
        df.groupBy("Company").max().show()

        println(".groupBy(\"Company\").min()")
        df.groupBy("Company").min().show()

        println(".groupBy(\"Company\").sum()")
        df.groupBy("Company").sum().show()


        // Other Aggregate Functions
        // Perform aggregate function on every single row of a column.
        // https://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/functions$.html


        println("Perform aggregate function on particular column")
        println(".select(countDistinct(\"Sales\"))")
        df.select(countDistinct("Sales")).show()

        println(".select(sumDistinct(\"Sales\"))")
        df.select(sumDistinct("Sales")).show()

        println(".select(variance(\"Sales\"))")
        df.select(variance("Sales")).show()

        println(".select(stddev(\"Sales\"))")
        df.select(stddev("Sales")).show()

        //println("df.select((\"Sales\"))")
        //df.select(("Sales")).show()


        // Arrange sales in ascending order.
        println(".orderBy(\"Sales\") - Order by Sales, Ascending")
        df.orderBy("Sales").show()

        import spark.implicits.StringToColumn

        // Arrange Sales in descending order.
        println(".orderBy($\"Sales\".desc) - Order by Sales, Descending")
        df.orderBy($"Sales".desc).show()

    }
}

