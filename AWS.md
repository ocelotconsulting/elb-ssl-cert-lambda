# AWS Configuration

## IAM
In addition to the AWSLambdaBasicExecutionRole (for CloudWatch logging), the
lambda function also needs to be assigned a role which
has permissions to read the S3 buckets it is configured for for domain certificate files.


## Lambda Execution
The Lambda function should be configured to listen to the SNS topic configured to
send messages from S3 PUT events caused by the [node-letsencrypt-lambda](https://github.com/ocelotconsulting/node-letsencrypt-lambda) Lambda function.