// LogSSNew.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/28/2022
// Trevor Reynen

// In this lab, we will practice Structure Streaming using the Dataset API instead of RDD API. We
// will count occurrences of each status code in our streaming data (apache access log). Our log
// file is located in the ./assets/logs/ directory. At first, you need to put access_log.txt into this
// directory. After it finishes the first batch, add access_log2.txt into the log directory.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._


object LogSSNew {
    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create Spark Session.
        val spark = SparkSession
            .builder()
            .appName("StructuredStreaming")
            .master("local[*]")
            .getOrCreate()

        // Read in streaming data from ./assets/logs/ directory as a DataFrame. We will keep
        // monitoring the ./assets/logs/ directory for new text files.

        // When new text files are discovered there, it will append each line from the new text
        // file into accessLogLines DataFrame.
        val accessLogLines = spark.readStream.text("./assets/logs/")

        println("accessLogLines Schema")
        accessLogLines.printSchema()

        // Regular expressions to extract pieces of Apache access log lines. You can download
        // regular expression from BlackBoard.
        val contentSizeExp = "\\s(\\d+)$"
        val statusExp = "\\s(\\d{3})\\s"
        val generalExp = "\"(\\S+)\\s(\\S+)\\s*(\\S*)\""
        val timeExp = "\\[(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} -\\d{4})]"
        val hostExp = "(^\\S+\\.[\\S+\\.]+\\S+)\\s"

        // Apply these regular expressions to create structure from the unstructured text. Convert
        // accessLogLines with only one value column into new DataFrame with new column such as
        // host, timestamp, and method.
        val logsDF = accessLogLines.select(regexp_extract(col("value"), hostExp, 1).alias("host"),
                                           regexp_extract(col("value"), timeExp, 1).alias("timestamp"),
                                           regexp_extract(col("value"), generalExp, 1).alias("method"),
                                           regexp_extract(col("value"), generalExp, 2).alias("endpoint"),
                                           regexp_extract(col("value"), generalExp, 3).alias("protocol"),
                                           regexp_extract(col("value"), statusExp, 1).cast("Integer").alias("status"),
                                           regexp_extract(col("value"), contentSizeExp, 1).cast("Integer").alias("content_size"))

        println("logsDF Schema")
        logsDF.printSchema()

        // Keep a running count of status codes from input stream. In other words, we want to know
        // how many times each status code appears in the log file over time.
        val statusCountDF = logsDF.groupBy("status").count()

        println("statusCountDF Schema")
        statusCountDF.printSchema()

        // Display the stream to the console. We will write stream out to the console.
        val query = statusCountDF.writeStream
            .outputMode("complete")
            .format("console")
            .queryName("counts").start()

        // Wait until we terminate the script.
        query.awaitTermination()

        // Stop the session.
        spark.stop()


        /* Output (example?):

        (with just access_log.txt in ./assets/logs/ and program running for like 4 min)..
            Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
            accessLogLines Schema
            root
             |-- value: string (nullable = true)

            logsDF Schema
            root
             |-- host: string (nullable = true)
             |-- timestamp: string (nullable = true)
             |-- method: string (nullable = true)
             |-- endpoint: string (nullable = true)
             |-- protocol: string (nullable = true)
             |-- status: integer (nullable = true)
             |-- content_size: integer (nullable = true)

            statusCountDF Schema
            root
             |-- status: integer (nullable = true)
             |-- count: long (nullable = false)

            -------------------------------------------
            Batch: 0
            -------------------------------------------
            +------+-----+
            |status|count|
            +------+-----+
            |   500|10714|
            |   301|  271|
            |   400|    2|
            |   404|   26|
            |   200|64971|
            |   304|   92|
            |   302|    2|
            |   405|    1|
            +------+-----+


        (Added access_logs2.txt into ./assets/logs/ then running the program again)..
            Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
            accessLogLines Schema
            root
             |-- value: string (nullable = true)

            logsDF Schema
            root
             |-- host: string (nullable = true)
             |-- timestamp: string (nullable = true)
             |-- method: string (nullable = true)
             |-- endpoint: string (nullable = true)
             |-- protocol: string (nullable = true)
             |-- status: integer (nullable = true)
             |-- content_size: integer (nullable = true)

            statusCountDF Schema
            root
             |-- status: integer (nullable = true)
             |-- count: long (nullable = false)

            -------------------------------------------
            Batch: 0
            -------------------------------------------
            +------+------+
            |status| count|
            +------+------+
            |   500| 21428|
            |   301|   542|
            |   400|     4|
            |   404|    52|
            |   200|129942|
            |   304|   184|
            |   302|     4|
            |   405|     2|
            +------+------+

        */

    }
}

