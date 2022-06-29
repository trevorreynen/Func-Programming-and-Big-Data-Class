// DTRegressorSample.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/29/2022
// Trevor Reynen


// In this lab, we try to predict price per unit area with decision tree regressor.
// The relevant features are "House Age", "Distance to MRT", and "Number of Convenience Stores".
// Decision tree regressor can handle features in different scales better.


// Imports.
import org.apache.log4j._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.DecisionTreeRegressor
import org.apache.spark.sql._


object DTRegressorSample {
    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create Spark Session.
        val spark = SparkSession
            .builder()
            .appName("DTRegressor")
            .master("local[*]")
            .getOrCreate()

        import spark.implicits._

        // Load data source into DataFrame.
        val realEstateDS = spark.read
            .option("sep", ",")
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("./assets/realestate.csv")

        realEstateDS.show()

        // In Spark machine learning, you need a label and features column.
        // Construct VectorAssembler to produce features column.
        val assembler = new VectorAssembler()
            .setInputCols(Array("HouseAge", "DistanceToMRT", "NumberConvenienceStores"))
            .setOutputCol("features")

        // Select both label and features column.
        val df = assembler
            .transform(realEstateDS)
            .select("PriceOfUnitArea", "features")

        df.show(10)

        // Split our Dataset into training set and test set.
        val trainTest = df.randomSplit(Array(0.5, 0.5))
        val trainingDF = trainTest(0)
        val testDF = trainTest(1)

        // Construct TreeRegressor. DecisionTreeRegressor can do without hyper parameters.
        // setLabelCol specify a label column (what you are predicting).
        val TreeRegressor = new DecisionTreeRegressor()
            .setFeaturesCol("features")
            .setLabelCol("PriceOfUnitArea")

        // Build decision tree regression model using fit function.
        val dtModel = TreeRegressor.fit(trainingDF)

        // Generate our prediction using the test data based on decision tree regressor model.
        val completePredictions = dtModel.transform(testDF).cache()
        completePredictions.show()

        // Extract the prediction and correct labels.
        val predictionAndLabel = completePredictions.select("prediction", "PriceOfUnitArea").collect()

        // Print out the predicted and actual values for each sample.
        for (prediction <- predictionAndLabel) {
            println(prediction)
        }

        spark.stop()

    }
}

