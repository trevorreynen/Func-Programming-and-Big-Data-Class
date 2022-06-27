// UrlStructureStreaming.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/28/2022
// Trevor Reynen

// In this lab, we will keep track of top urls in our apache access log using 30 second windows.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._


object UrlStructureStreaming {
    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        val spark = SparkSession
            .builder()
            .appName("StructuredStreaming")
            .master("local[*]")
            .getOrCreate()

        import spark.implicits._

        // Read in streaming data from ./assets/logs directory as a DataFrame.
        val accessLogLines = spark.readStream.text("./assets/logs/")

        // Regular expressions to extract pieces of Apache access log lines.
        val contentSizeExp = "\\s(\\d+)$"
        val statusExp = "\\s(\\d{3})\\s"
        val generalExp = "\"(\\S+)\\s(\\S+)\\s*(\\S*)\""
        val timeExp = "\\[(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]"
        val hostExp = "(^\\S+\\.[\\S+\\.]+\\S+)\\s"

        // Apply these regular expressions to create structure from the unstructured text.
        val logsDF = accessLogLines.select(regexp_extract(col("value"), hostExp, 1).alias("host"),
                                           regexp_extract(col("value"), timeExp, 1).alias("timestamp"),
                                           regexp_extract(col("value"), generalExp, 1).alias("method"),
                                           regexp_extract(col("value"), generalExp, 2).alias("endpoint"),
                                           regexp_extract(col("value"), generalExp, 3).alias("protocol"),
                                           regexp_extract(col("value"), statusExp, 1).cast("Integer").alias("status"),
                                           regexp_extract(col("value"), contentSizeExp, 1).cast("Integer").alias("content_size"))

        // Add the new column with current timestamp. The timestamp is used to show when the data
        // is ingested.
        val logsDF2 = logsDF.withColumn("eventTime", current_timestamp())

        println("logsDF2 Schema")
        logsDF2.printSchema()

        // Keep a running count of endpoints with 30 a 30 second window sliding 10 seconds.
        // Count up the occurrences of each url every 10 seconds in the 30 second window.
        // Endpoint is a url.
        val endpointCounts = logsDF2.groupBy(window($"eventTime", "30 seconds", "10 seconds"),
                                             col("endpoint")).count()

        println("endpointCounts Schema")
        endpointCounts.printSchema()

        // Sort by count.
        val sortedEndpointCounts = endpointCounts.orderBy(col("count").desc)

        // Display the endpointCounts stream to the console.
        val query = endpointCounts.writeStream
            .outputMode("complete")
            .format("console")
            .queryName("counts2")
            .start()

        // Basically, we print out two types of streams.
        // Wait until we terminate the scripts.
        query.awaitTermination()

        // Stop the session.
        spark.stop()


        /* Output (Example?):

        (With access_logs.txt and access_logs2.txt in ./assets/logs/ directory)..

            Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
            logsDF2 Schema
            root
             |-- host: string (nullable = true)
             |-- timestamp: string (nullable = true)
             |-- method: string (nullable = true)
             |-- endpoint: string (nullable = true)
             |-- protocol: string (nullable = true)
             |-- status: integer (nullable = true)
             |-- content_size: integer (nullable = true)
             |-- eventTime: timestamp (nullable = false)

            endpointCounts Schema
            root
             |-- window: struct (nullable = true)
             |    |-- start: timestamp (nullable = true)
             |    |-- end: timestamp (nullable = true)
             |-- endpoint: string (nullable = true)
             |-- count: long (nullable = false)

            -------------------------------------------
            Batch: 0
            -------------------------------------------
            +--------------------+--------------------+-----+
            |              window|            endpoint|count|
            +--------------------+--------------------+-----+
            |[2022-06-27 11:03...|/wp-content/cache...|   42|
            |[2022-06-27 11:03...|        /about/feed/|    2|
            |[2022-06-27 11:03...|/introducing-comm...|  120|
            |[2022-06-27 11:03...|/national-headlin...|    2|
            |[2022-06-27 11:03...|/wp-content/uploa...|   16|
            |[2022-06-27 11:03...|/wp/wp-content/fo...|    4|
            |[2022-06-27 11:03...|/san-jose-headlines/|  170|
            |[2022-06-27 11:03...|    /australia/?pg=1|    6|
            |[2022-06-27 11:03...|/entertainment/?pg=0|    2|
            |[2022-06-27 11:03...|/washington-dc-sp...|    4|
            |[2022-06-27 11:03...|/san-francisco-sp...|    2|
            |[2022-06-27 11:03...|     /dallas-sports/|  118|
            |[2022-06-27 11:03...|     /?page_id=13481|    6|
            |[2022-06-27 11:03...|           /?p=13636|    2|
            |[2022-06-27 11:03...|/feeds/tampa-bay-...|    2|
            |[2022-06-27 11:03...|          /jeecms.do|    2|
            |[2022-06-27 11:03...|/web-console/Serv...|    2|
            |[2022-06-27 11:03...|             /about/|  138|
            |[2022-06-27 11:03...|        /technology/|  136|
            |[2022-06-27 11:03...|/apple-touch-icon...|    4|
            +--------------------+--------------------+-----+
            only showing top 20 rows

        */

    }
}

