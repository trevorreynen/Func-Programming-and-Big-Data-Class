// DatesTimeStamps.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/23/2022
// Trevor Reynen

// In this lab, we discuss how to handle Dates and Time Stamps.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._ // For Date/Time functions.


object DatesTimeStamps {
    def main(args: Array[String]): Unit = {

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

        println("Just selecting month from timestamp object.")
        // Select month from Date column and show 5 rows.
        df.select(month(df("Date"))).show(5)

        // Select year from Date column and show 5 rows.
        df.select(year(df("Date"))).show(5)

        // You want to use that timestamp information to perform some operation.
        // For example, we want to find out average Closing price per Year.
        println("Adding a new column.")
        val df2 = df.withColumn("Year", year(df("Date")))

        // This import is needed to use the $-notation.
        //abstract class SQLImplicits
        import spark.implicits.StringToColumn

        // Group by rows based on Year and calculate average value per Year.
        val dfavgs = df2.groupBy("Year").mean()

        dfavgs.show()

        println("Mean per year, notice large 2008 drop")
        // Finally, select average Close price per Year.
        dfavgs.select($"Year", $"avg(Close)").show()

        // This reflects the financial crisis for 2008.
        // Group by Year and calculate min Close price.
        val dfmins = df2.groupBy("Year").min()
        dfmins.select($"Year", $"min(Close)").show()

    }
}

