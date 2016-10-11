package com.ocelotconsulting.ssl

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
  * Created by Larry Anderson on 10/11/16.
  */

class ELBSSLCertificateLambdaSpec extends FlatSpec with Matchers {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def produceS3Event: JsonNode =
    mapper.readValue(Source.fromInputStream(getClass.getResourceAsStream("/sns_s3_event.json")).getLines.mkString, classOf[JsonNode])

  def toEvent: SNSEvent = {
    val event = new SNSEvent()
    val record = new SNSRecord()
    val sns = new SNS()
    val map = produceS3Event
    sns.setMessage(map.path("Records").get(0).path("Sns").path("Message").asText())
    record.setSns(sns)
    event.setRecords(List[SNSRecord](record))
    event
  }

  "An S3 event" should "run lambda successfully" in {
    val mainObj = new ELBSSLCertificateLambda
    val something = mainObj.configureELBCert(toEvent)
    something.asScala.head shouldBe "Status code for setting arn:aws:iam::316254774545:server-certificate/ocelotconsulting-demo.com ELB certificate for myelb is 200."
  }
}
