// MissingData.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/22/2022
// Trevor Reynen

// Handle missing data.
// Schema of data: Id, Name, Sales.
// The sample file has only last row complete.

// In this lecture, we discuss how to handle missing values using the drop or fill options.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import Utilities._


object MissingData {
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
            .csv("./assets/ContainsNull.csv")

        println("DataFrame Schema")
        df.printSchema()

        // Notice the missing values!
        df.show()

        // You basically have 3 options with Null values.
        // 1. Just keep them, maybe only let a certain percentage through.
        // 2. Drop them.
        // 3. Fill them in with some other value.
        // No "correct" answer. You'll have to make a decision based on the data.

        // Dropping values.
        // Technically, still experimental, but it has been around since Scala 1.3.
        println("Drop any row(s) with any amount of null values.")
        df.na.drop().show()

        println("Drop any row(s) that have less than a minimum number of Non-null values ( < Int)")
        // Drop any row(s) that have less than 2 Non-null values.
        df.na.drop(2).show()

        // Interesting behavior.
        // What happens when using double/int versus strings?

        // Based on the data type you put in for fill, it will look for all columns that match
        // that data type and fill it in.

        println("Fill in the na values with 100.")
        df.na.fill(100).show()

        println("Fill in String will only go to all string columns.")
        // Fill in the missing value with "Missing Name".
        df.na.fill("Missing Name").show()

        println("Be more specific, pass an Array of string column Name.")
        // Be more specific, fill in missing value on column Name with "New Name".
        df.na.fill("New Name", Array("Name")).show()


        // Exercise: Fill in Sales with average sales.
        // How to get averages? For now, a simple was is to use describe.
        df.describe().show()

        println("Fill in missing value in Sales column with average sales.")
        df.na.fill(400.5, Array("Sales")).show()

        println("Fill in missing value for Sales and Name column")
        val df2 = df.na.fill(400.5, Array("Sales"))
        df2.na.fill("Missing Name", Array("Name")).show()

    }
}

