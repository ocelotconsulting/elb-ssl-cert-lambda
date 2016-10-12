# AWS Configuration

## IAM
In addition to the AWSLambdaBasicExecutionRole (for CloudWatch logging), the
lambda function also needs to be assigned a role which
has permissions to read the S3 buckets it is configured for for domain certificate files.

## ELB Certificate IAM Policy
The following policy was added to the lambda role so that this function could set the ELB SSL termination certificate
for incoming SNS messages. The `Resource` element could be more specific, allowing several instances
of this lambda to exist for different groups sharing an account, etc.
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Stmt1222040922222",
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:SetLoadBalancerListenerSSLCertificate"
            ],
            "Resource": [
                "arn:aws:elasticloadbalancing:us-east-1:my_acct:loadbalancer/*"
            ]
        }
    ]
}
```

## Lambda Execution
The Lambda function should be configured to listen to the SNS topic configured to
send messages from S3 PUT events caused by the [node-letsencrypt-lambda](https://github.com/ocelotconsulting/node-letsencrypt-lambda) Lambda function.