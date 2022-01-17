from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, LongType, FloatType, BooleanType
from pyspark.sql.functions import column, from_json, to_timestamp, window

# Configure spark
spark = SparkSession \
    .builder \
    .appName("TaskEventConsumer") \
    .getOrCreate()

# Disable logging as it writes too much log
spark.sparkContext.setLogLevel("ERROR")

# Consumes JSON data from Kafka
task_events_raw = spark \
  .readStream \
  .format("kafka") \
  .option("kafka.bootstrap.servers", "localhost:9092") \
  .option("subscribe", "task-events") \
  .option("startingOffsets", "earliest") \
  .option("failOnDataLoss", "false") \
  .load()

# Print Kafka schema 
# task_events_raw.printSchema()

# schema of JSON
schema = StructType([
    StructField("time", LongType(), False), 
    StructField("missingInfo", IntegerType(), True),
    StructField("jobIn", IntegerType(), False), 
    StructField("taskIndex", LongType(), False), 
    StructField("machineId", IntegerType(), True), 
    StructField("eventType", IntegerType(), False), 
    StructField("user", StringType(), True), 
    StructField("schedulingClass", IntegerType(), True), 
    StructField("priority", IntegerType(), False), 
    StructField("cpuRequest", FloatType(), True), 
    StructField("memoryRequest", FloatType(), True), 
    StructField("diskSpaceRequest", FloatType(), True), 
    StructField("differentMachinesRestriction", BooleanType(), True)
  ])

# Convert JSON to DataFrame
task_events_df = task_events_raw.selectExpr("CAST(value AS STRING)") \
  .select(from_json(column("value"), schema).alias("data")) \
  .select("data.*")

task_events_df = task_events_df \
  .withColumn("time", to_timestamp(task_events_df.time))

task_events_df.printSchema()

# task_events_df.writeStream \
#   .format("console") \
#   .outputMode("update") \
#   .queryName("Raw") \
#   .start()

# Start analysis
# 1 Number of events by event type in 1 min window every 30 seconds

events_by_type_1_min_window = task_events_df \
  .withWatermark("time", "3 minutes") \
  .groupBy("eventType", window("time", "1 minutes", "30 seconds")) \
  .count() \
  .withColumn("window-start", column("window.start").cast(StringType()).alias("window-start")) \
  .withColumn("window-end", column("window.end").cast(StringType()).alias("window-end")) \
  .drop("window")

events_by_type_1_min_window \
  .writeStream \
  .format("console") \
  .outputMode("update") \
  .queryName("States' count over 1 min window") \
  .start()


# 2 Number of events by event type and machine in 1 min window every 30 seconds
events_by_machine_type_1_min_window = task_events_df \
  .withWatermark("time", "3 minutes") \
  .filter(task_events_df.machineId.isNotNull()) \
  .groupBy("eventType", "machineId", window("time", "1 minutes", "30 seconds")) \
  .count() \
  .withColumn("window-start", column("window.start").cast(StringType()).alias("window-start")) \
  .withColumn("window-end", column("window.end").cast(StringType()).alias("window-end")) \
  .drop("window")
  

events_by_machine_type_1_min_window.writeStream \
  .format("console") \
  .outputMode("update") \
  .queryName("States' count per machine over 1 min window") \
  .start() \
  .awaitTermination()
