package com.epam.workshop

import org.apache.spark.sql.SparkSession
import org.rogach.scallop.{ScallopConf, ScallopOption}

import scala.language.postfixOps

class StreamingLayerArgsExample(args: Seq[String]) extends ScallopConf(args) {

  val inputPath: ScallopOption[String] = opt[String](
    descr = "Questions for streaming input file"
  )

  val outputPath: ScallopOption[String] = opt[String](
    descr = "Questions output path"
  )

  val bootstrapServer: ScallopOption[String] = opt[String](
    descr = "Kafka bootstrap server"
  )

  val topic: ScallopOption[String] = opt[String](
    descr = "Questions kafka topic"
  )

  verify()
}

object MainExample extends App {

  val streamingLayerArgs = new StreamingLayerArgs(args)

  implicit val ss: SparkSession = SparkSession
    .builder()
    .appName("streaming-layer")
    .getOrCreate()

  new QuestionsProducerExample().produceQuestions(
    new HdfsGatewayExample,
    new KafkaGatewayExample,
    streamingLayerArgs.inputPath(),
    streamingLayerArgs.outputPath(),
    streamingLayerArgs.bootstrapServer(),
    streamingLayerArgs.topic()
  )
}
