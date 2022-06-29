// LinRegDataset.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/29/2022
// Trevor Reynen

// In this lab, we learn how to apply Dataset and DataFrame to build a linear regression model.
// We will introduce how to use linear regression in Spark's machine learning library.

// Linear regression will predict real-valued variables given several input variables.
// Input variables are called independent variables and predicted variables are called dependable
// variables.

// Basically, we will build the model to understand the relationship between independent variables
// and dependable variables.

// For linear regression, all features need to be normalized before training.

// In regression.txt, the first column is how much the customer spent. The second column is page
// speed.

// One simple example is, given page speed, we want to predict how much the customer will spend in
// the online store.


// Imports.
import org.apache.spark.sql._
import org.apache.log4j._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.types._


object LinRegDataset {

    // Case class to define Schema of our dataset.
    // Label is how much the customer spent.
    // feature_raw is page speed.
    case class RegressionSchema(label: Double, features_raw: Double)


    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create Spark Session.
        val spark = SparkSession
            .builder()
            .appName("LinearRegressionDF")
            .master("local[*]")
            .getOrCreate()

        // In the machine learning world, "label" is just the value you're trying to predict, and
        // "feature" is the data you are given to make a prediction with.
        // So, in this example, the "labels" are the first column of our data, and the "features"
        // are the second column. You can have more than one "feature" which is why a Vector is
        // required.

        // Create Schema so that we can convert DataFrame to Dataset since we do not have header
        // row in our text file.
        val regressionSchema = new StructType()
            .add("label", DoubleType, nullable = true)
            .add("features_raw", DoubleType, nullable = true)

        // Read regression.txt and turn it into a Dataset.
        import spark.implicits._
        val dsRaw = spark.read
            .option("sep", ",")
            .schema(regressionSchema)
            .csv("./assets/regression.txt")
            .as[RegressionSchema]

        // Print dsRaw Schema.
        dsRaw.printSchema()

        // Spark machine learning library expects label column and features column. We will
        // transform our data into that format. Construct VectorAssembler to produce features
        // column.
        val assembler = new VectorAssembler()
            .setInputCols(Array("features_raw"))
            .setOutputCol("features")

        // Produce Dataset with label column and features column.
        val df = assembler
            .transform(dsRaw)
            .select("label", "features")

        // Print df Schema.
        df.printSchema()

        // Let's split our data into training Dataset and testing Dataset. Training Dataset is used
        // to produce the model. Testing Dataset is used to test the performance of our model.
        val trainTest = df.randomSplit(Array(0.5, 0.5))
        val trainingDF = trainTest(0)
        val testDF = trainTest(1)

        // Create new LinearRegression Object with several parameters.
        val lir = new LinearRegression()
            .setRegParam(0.3) // Regularization.
            .setElasticNetParam(0.8) // Elastic net mixing.
            .setMaxIter(100) // Max iterations.
            .setTol(1E-6) // Convergence tolerance.

        // Train the model using our training data.
        val model = lir.fit(trainingDF)

        // Now see if we can predict values in our test data. Generate predictions using our linear
        // regression model for all features in our test DataFrame.

        // This basically adds a "prediction" column to our testDF DataFrame.
        val fullPredictions = model.transform(testDF).cache()

        fullPredictions.show()

        // Extract the predictions and the "known" correct labels.
        val predictionAndLabel = fullPredictions.select("prediction", "label").collect()

        // Print out the predicted and actual values for each sample.
        for (prediction <- predictionAndLabel) {
            println(prediction)
        }

        // Stop the Session.
        spark.stop()

    }
}

