package com.ocelotconsulting.ssl

import com.amazonaws.services.elasticloadbalancing.model.SetLoadBalancerListenerSSLCertificateResult
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.ocelotconsulting.ssl.aws.ec2.SetELBCertificate

import scala.collection.JavaConversions._

/**
  * Created by Larry Anderson on 10/11/16.
  */
class ELBSSLCertificateLambda {

  private def extractS3Event(snsRecord: SNSEvent.SNSRecord): S3EventNotification = S3EventNotification.parseJson(snsRecord.getSNS.getMessage)

  private def updateELBCert(entity: S3Entity): SetLoadBalancerListenerSSLCertificateResult = SetELBCertificate(toELBName(entity), 443, toSSLCertName(entity))

  private def transformResult(entity: S3Entity, result: SetLoadBalancerListenerSSLCertificateResult): String =
    s"Status code for setting ${toSSLCertName(entity)} ELB certificate for ${toELBName(entity)} is ${result.getSdkHttpMetadata.getHttpStatusCode}."

  private def updateCert(s3Event: S3EventNotification): java.util.List[String] =
    s3Event.getRecords.map(_.getS3).map { entity => transformResult(entity, updateELBCert(entity))}

  // Actual lambda function
  def configureELBCert(event: SNSEvent): java.util.List[String] = event.getRecords.map { extractS3Event } flatMap updateCert
}
