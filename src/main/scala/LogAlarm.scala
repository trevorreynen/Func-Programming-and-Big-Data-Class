// LogAlarmer.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/21/2022
// Trevor Reynen

// Let us create a system that will actually look at the status codes that are returned on each
// access log line and raise some sort of alarm if we start to see too many errors being returned.

// Observe status code, which is a 3 digit number. Success code is 200 range.
// Failure code is 500 range.
// Raise alarm if too many errors returned.
// Failure codes are due by invalid requests by hackers.
// It is wrong because attack.

// Hackers get into the site and shit it down briefly.

// Slow down the broadcasting.
//   -i secs    Delay interval for lines spent, ports scanned.
// ncat -kl -i 1 9999 < access_log.txt


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.storage.StorageLevel

import java.util.regex.Pattern
import java.util.regex.Matcher

import Utilities._


object LogAlarm {
    def main(args: Array[String]): Unit = {

        // Teacher was using the setupLogging() function from the Utilities.scala, but I realized
        // it doesn't work. So, instead I just add the line below it which does the same thing.
        //setupLogging()
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create the context with a 3 second batch size.
        val sc = new StreamingContext("local[*]", "LogAlarmer", Seconds(3))

        // Construct a regular expression (regex) to extract fields from raw Apache log lines.
        val pattern = apacheLogPattern()

        // Create a socket stream to read log data published via netcat on port 9999, locally.
        val lines = sc.socketTextStream("127.0.0.1", 9999, StorageLevel.MEMORY_AND_DISK_SER)

        // Extract the status field from each log line.
        val statuses = lines.map(x => {
            val matcher: Matcher = pattern.matcher(x)
            if (matcher.matches()) matcher.group(6)
            else "[Error]"
        })

        // Now map these status results to success and failure.
        val successFailure = statuses.map(x => {
            // Since not all strings can be converted to integer, we use the Try function in util
            // to recover from error.
            val statusCode = util.Try(x.toInt) getOrElse 0
            // Code between 200 and 300 is success.
            if (statusCode >= 200 && statusCode < 300) {
                "Success"
            } else if (statusCode >= 500 && statusCode < 600) {
                "Failure"
            } else {
                "Other"
            }
        })

        /*
        successFailure.foreachRDD((rdd, time) => {
            println()
            println("Sample data for successFailure")
            println()
        })
        */

        // Tally up statuses over a 3-minute window sliding every 3 seconds.
        val statusCounts = successFailure.countByValueAndWindow(Seconds(300), Seconds(3))

        // For each batch, get the RDD's representing data from current window.
        statusCounts.foreachRDD((rdd, time) => {
            // Keep track of total success and error codes from each RDD.
            var totalSuccess: Long = 0
            var totalError: Long = 0

            if (rdd.count() > 0) {
                // Size of each RDD is small.
                val elements = rdd.collect()

                for (element <- elements) {
                    val result = element._1
                    val count = element._2

                    if (result == "Success") {
                        totalSuccess += count
                    }

                    if (result == "Failure") {
                        totalError += count
                    }
                }
            }

            // Print totals from current window.
            println("Total success: " + totalSuccess + ", Total error: " + totalError)

            // Don't alarm unless you have some minimum amount of data to work with.
            if (totalError + totalSuccess > 100) {
                // Compute the error rate.
                // Note: use of util.Try to handle potential divide by zero exception.
                val ratio: Double = util.Try(totalError.toDouble / totalSuccess.toDouble) getOrElse 1.0

                // If there are more errors than successes, wake someone up.
                if (ratio > 0.5) {
                    println("SOMETHING IS WRONG")
                } else {
                    println("All Good")
                }
            }
        })

        /*
        statuses.foreachRDD((rdd, time) => {
            println()
            println("Sample data for statuses")
            rdd.take(10).foreach(println)
            println()
        })

        successFailure.foreachRDD((rdd, time) => {
            println()
            println("Sample data for successFailure")
            rdd.take(10).foreach(println)
            println()
        })

        statusCounts.foreachRDD((rdd, time) => {
            println()
            println("Sample data for statusCounts")
            rdd.take(10).foreach(println)
            println()
        })
        */

        // Kick it all off. Set a checkpoint directory.
        sc.checkpoint("assets/checkpoint_Alarm/")
        sc.start()
        sc.awaitTermination()

        /* Output (example):

        Total success: 416, Total error: 0
        All Good
        Total success: 1145, Total error: 0
        All Good
        Total success: 1702, Total error: 0
        All Good
        Total success: 2283, Total error: 0
        All Good
        ...
        ...
        Total success: 4633, Total error: 2006
        All Good
        Total success: 4633, Total error: 2725
        SOMETHING IS WRONG
        Total success: 4633, Total error: 3477
        SOMETHING IS WRONG
        Total success: 4633, Total error: 4228
        SOMETHING IS WRONG
        ...
        ...
        Total success: 15956, Total error: 8556
        SOMETHING IS WRONG
        Total success: 16686, Total error: 8556
        SOMETHING IS WRONG
        Total success: 17433, Total error: 8556
        All Good
        Total success: 18183, Total error: 8556
        All Good

        */

    }
}


