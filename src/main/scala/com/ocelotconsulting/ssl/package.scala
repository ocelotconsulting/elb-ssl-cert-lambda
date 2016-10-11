package com.ocelotconsulting

import java.net.URLDecoder

import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by Larry Anderson on 10/11/16.
  */
package object ssl {
  object ELBSSLCertificateLambdaConfig {
    val config = ConfigFactory.load()
    private def certMap(s3Path: String): Config = config.getConfig("cert-path-to-arn").getConfig(s3Path)
    def arn(s3Path: String) = certMap(s3Path).getString("arn")
    def elb(s3Path: String) = certMap(s3Path).getString("elb")
  }

  def toSSLCertName(entity: S3Entity): String =
    ELBSSLCertificateLambdaConfig.arn(s"${decodeS3Key(entity.getBucket.getName)}/${decodeS3Key(entity.getObject.getKey)}")

  def toELBName(entity: S3Entity): String =
    ELBSSLCertificateLambdaConfig.elb(s"${decodeS3Key(entity.getBucket.getName)}/${decodeS3Key(entity.getObject.getKey)}")

  def decodeS3Key(key: String): String = URLDecoder.decode(key.replace("+", " "), "utf-8")
}
