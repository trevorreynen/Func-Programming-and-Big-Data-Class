// LogParser.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/20/2022
// Trevor Reynen

// In this lab, we will demonstrate Spark streaming.

// We will show top URL's visited over a 5 minute window from a stream of Apache access logs on
// port 9999.
// In other words, we are listening to real click stream data on a real network.


// NOTE: See README. At this point even though my previous SparkContext programs showed many errors
// then gave the correct results, I decided to finally install Apache Spark. Not sure if it's
// required to do run this program. However, Nmap (on windows) is required to use the "ncat"
// command for this to work properly.

// STEPS TO RUN PROGRAM PROPERLY:
// 1. Open terminal and head to assets folder:  C:\...\Func-Programming-and-Big-Data-Class\assets\
// 2. Run the command:  ncat -kl -i 1 9999 < access_log.txt
// 3. Then, run this Scala program.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.storage.StorageLevel

import java.util.regex.Pattern
import java.util.regex.Matcher

import Utilities._


object LogParser {
    def main(args: Array[String]) {

        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create the context with a 3 second batch size.
        val ssc = new StreamingContext("local[*]", "LogParser", Seconds(3))

        setupLogging()

        // Construct a regular expression (regex) to extract fields from raw Apache log lines.
        val pattern = apacheLogPattern()

        // Create a socket stream to read log data published via netcat on port 9999 locally.
        val lines = ssc.socketTextStream("127.0.0.1", 9999, StorageLevel.MEMORY_AND_DISK_SER)

        // Extract the request field from each log line. If the record is invalid, discard.

        // This is sample request field. HTTP command, URL, and Protocol.
        // POST /wp-login.php HTTP/1.1
        val requests = lines.map(x => {
            val matcher: Matcher = pattern.matcher(x)
            if (matcher.matches()) matcher.group(5)
        })

        // Extract the URL from the request.
        val urls = requests.map(x => {
            val arr = x.toString.split(" ")
            if (arr.size == 3) arr(1)
            else "[Error]"
        })

        // Calculate URL request count over a 5-minute window sliding every three seconds.
        val urlCounts = urls
            .map(x => (x, 1))
            .reduceByKeyAndWindow(_ + _, _ - _, Seconds(300), Seconds(3))

        // Sort by URL counts and print the results.
        val sortedResults = urlCounts.transform(rdd => rdd.sortBy(x => x._2, false))
        sortedResults.print()

        // POST /wp-login.php HTTP/1.1
        /*
        requests.foreachRDD((rdd, time) => {
            println()
            println("Sample data for requests")
            rdd.take(10).foreach(println)
            println()
        })
        */

        // Set a checkpoint directory, and kick it off. Please create checkpoint directory.
        ssc.checkpoint("./assets/checkpoint/")
        // I am hiding the /assets/checkpoint/ folder from Git/GitHub since I don't want to go
        // through all those files created by this program and idk if they contain any kind of
        // important information.
        ssc.start()
        ssc.awaitTermination()

        /* Output (Example):

        -------------------------------------------
        Time: 1655665629000 ms
        -------------------------------------------
        (/xmlrpc.php,82)
        (/,32)
        (/robots.txt,21)
        (/wp-login.php,15)
        (http://51.254.206.142/httptest.php,9)
        (/favicon.ico,3)
        (/orlando-headlines/,2)
        ([Error],1)
        (/about/,1)
        (/?page_id=2112,1)
        ...

        -------------------------------------------
        Time: 1655665632000 ms
        -------------------------------------------
        (/xmlrpc.php,802)
        (/,32)
        (/robots.txt,22)
        (/wp-login.php,16)
        (http://51.254.206.142/httptest.php,9)
        (/weather/,3)
        (/favicon.ico,3)
        (/orlando-headlines/,2)
        ([Error],1)
        (/about/,1)
        ...

        -------------------------------------------
        Time: 1655665635000 ms
        -------------------------------------------
        (/xmlrpc.php,1491)
        (/,33)
        (/robots.txt,23)
        (/wp-login.php,16)
        (http://51.254.206.142/httptest.php,9)
        (/weather/,3)
        (/favicon.ico,3)
        (/orlando-headlines/,2)
        ([Error],1)
        (/about/,1)
        ...

        */


    }
}

