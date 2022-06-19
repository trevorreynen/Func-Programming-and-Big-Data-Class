// FindMinTemperatures.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/16/2022
// Trevor Reynen


// Imports.
import scala.math.min
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext


object FindMinTemperatures {

    // This function will transform raw line into tuple (stationID, entryType, Temperature).
    def parseLine(line: String): (String, String, Float) = {
        // Split line using comma.
        val fields = line.split(",")

        // Extract stationID and entryTuple.
        val stationID = fields(0)
        val entryType = fields(2)

        // Convert celsius to fahrenheit.
        val temperature = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f

        // Return tuple (stationID, entryType, Temperature)
        (stationID, entryType, temperature)
    }

    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create SparkContext.
        val sc = new SparkContext("local[*]", "MinTemperature")

        // Read each line of input data.
        val lines = sc.textFile("./assets/1800.csv")

        // Convert each line to (stationID, entryType, temperature) tuples using map function.
        val parsedLines = lines.map(parseLine)

        // Keep only TMIN entries using filter function.
        val minTemps = parsedLines.filter(x => x._2 == "TMIN")

        // Convert (stationID, entryType, temperature) tuples to (stationID, temperature) tuple.
        // You want to strip out all the data you can to minimize the amount of data that needs to
        // be pushed around during a shuffle operation.
        val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))

        // Find the minimum temperature for each station using reduceByKey.
        val minTempsByStation = stationTemps.reduceByKey((x, y) => min(x, y))

        // Collect the results.
        val results = minTempsByStation.collect()

        // Format and print out results.
        for (results <- results.sorted) {
            val (station, temp) = (results._1, results._2)
            val formattedTemp = f"$temp%.2f F"

            println(s"$station minimum temperature: $formattedTemp")
        }

        // Only have two weather stations.
        // Output:
        // EZE00100082 minimum temperature: 7.70 F
        // ITE00100554 minimum temperature: 5.36 F


    }
}

