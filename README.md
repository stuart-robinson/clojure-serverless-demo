# clojure-serverless-demo

A full-stack clojure application deployed to API Gateway, AWS Lambda, S3 and Dynamodb

WARNING: This application was built as a proof-of-concept and as such the code should not be considered production ready.

## Installation

In order to deploy this application you will need to install the Serverless Framework using npm

```
# Install serverless framework

npm install -g serverless

# Install serverless S3 plugin

npm install --save serverless-finch

# Setup AWS Credentials

aws configure
```

## Tests

```
lein test
```

## Deployment

### Front-end application:

```
lein clean
lein cljsbuild once min
serverless client deploy -s dev
```

### Back-end application:

```
lein clean
lein uberjar
serverless deploy -s dev
```


## Other Libraries

* https://github.com/mhjort/ring-apigw-lambda-proxy - APIGateway Ring Middleware

* https://github.com/uswitch/lambada - Clojure library for writing Lambda functions
