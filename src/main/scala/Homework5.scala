// Homework5.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/27/2022
// Trevor Reynen

// ==========<  Homework 5  >==========
// Use the Netflix_2011_2016.csv file given to complete Homework 5.

// 1. Start a simple Spark Session.   ~~~DONE~~~
// 2. Load the Netflix_2011_2016.csv file, having Spark infer the data types.   ~~~DONE~~~
//     Q1: What are the column names?   ~~~DONE~~~
//     Q2: What does the Schema look like?   ~~~DONE~~~
// 3. Print out the first 5 columns.   ~~~DONE~~~
// 4. Use describe() to learn about the DataFrame.   ~~~DONE~~~
// 5. Create a new DataFrame with a column called HV Ratio that is the ratio of the High Price
// versus volume of stock traded for a day.   ~~~DONE~~~
//     Q3: What day had the Peak High in Price?   ~~~DONE~~~
//     Q4: What is the mean of the Close column?   ~~~DONE~~~
//     Q5: What is the max and min of the Volume column?   ~~~DONE~~~
// For Scala/Spark $ Syntax
// 6. How many days was the Close lower than $ 600?   ~~~DONE~~~
// 7. What percentage of the time was the High greater than $500?   ~~~DONE~~~
// 8. What is the Pearson correlation between High and Volume?   ~~~DONE~~~
// 9. What is the max High per year?   ~~~DONE~~~
// 10. What is the average Close for each Calender Month?   ~~~DONE~~~


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


        //  Q2: What does the Schema look like?
        println("Q2: What does the Schema look like?")
        df.printSchema()


        println("3. Print out the first 5 columns.")
        for (row <- df.head(5)) println(row)


        println("\n4. Use describe() to learn about the DataFrame.")
        df.describe().show()


        println("5. Create a new DataFrame with a column called HV\n" +
                "Ratio that is the ratio of the High Price versus\n" +
                "volume of stock traded for a day.")
        val df2 = df.withColumn("HV Ratio", df("High") / df("Volume"))


        //df2.printSchema()
        df2.show(5)


        //  Q3: What day had the Peak High in Price?
        println("Q3: What day had the Peak High in Price? ")
        df.orderBy(df("High").desc).show(3)


        //  Q4: What is the mean of the Close column?
        println("Q4: What is the mean of the Close column?")
        //val dfmean = df.groupBy("Close").mean()
        df.agg(round(avg("Close"), 2)).show()


        //  Q5: What is the max and min of the Volume column?
        println("Q5a: What is the max of the Volume column?")
        df.select(max("Volume")).show()

        println("Q5b: What is the min of the Volume column?")
        df.select(min("Volume")).show()


        // For Scala/Spark $ Syntax
        import spark.implicits._


        // 6. How many days was the Close lower than $ 600?
        println("6. How many days was the Close lower than $ 600?  " + df.filter($"Close" < 600).count() + "\n")


        // 7. What percentage of the time was the High greater than $500?
        println("7. What percentage of the time was the High greater than $500? " + ((df.filter($"High" > 500).count() * 1.0 / df.count()) * 100))


        // 8. What is the Pearson correlation between High and Volume?
        println("\n8. What is the Pearson correlation between High and Volume?")
        df.select(corr("High", "Volume")).show()


        // 9. What is the max High per year?
        println("9. What is the max High per year?")
        val yearDf = df.withColumn("Year", year(df("Date")))
        val yearMax = yearDf.select($"Year", $"High").groupBy("Year").max()
        val results = yearMax.select($"Year", $"max(High)")
        results.orderBy("Year").show()


        // 10. What is the average Close for each Calender Month?
        println("10. What is the average Close for each Calender Month?")
        val monthDf = df.withColumn("Month", month(df("Date")))
        val monthAvg = monthDf.select($"Month", $"Close").groupBy("Month").mean()
        monthAvg.select($"Month", $"avg(Close)").orderBy("Month").show()

        spark.stop()

    }
}

