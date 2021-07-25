# Technical Task for Tutuka - Testing and deployment engineer
![Build Status](https://codebuild.ap-south-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiQTVqNmdtQWVVb05UbVJnM1VuV29md2hTWmprWDVpRGJ1d1A0WmlPM0t2ZlFQZFFHL1pNc0tPYnIvbVV4OThPamUvc2VjZE5WVWRiU0ZtSm9hVitxejB3PSIsIml2UGFyYW1ldGVyU3BlYyI6Ikk4U0doektRL20zb2svbW8iLCJtYXRlcmlhbFNldFNlcmlhbCI6Mn0%3D&branch=master)
![Docker Automated build](https://img.shields.io/docker/automated/mohansha/tutuka-app)
![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/mohansha/tutuka-app)
![Unit Test Status](https://img.shields.io/badge/unittests-✔7-green)

## Tasks Done:
1. Wrote units tests for the app, to check if code is breaking the current functionality or not.

2. Created a pipeline to build, test and deploy in AWS.

3. Reviewed the current code structure of the app, and proposed solutions that would benefit the performance, and testability of the app.

4. Provided a report, and the code-as-infrastructure (cloudformation) implementation specs to be reviewed along with AWS access.

Github repo link: https://github.com/MohanSha/tutuka

## AWS Credentials to review infrastructure
I have created a temporary IAM user with read access for the resources used for my project in my AWS account. You can use the credentials to review the infra setup.

Console Link: https://973744485172.signin.aws.amazon.com/console

Username: AWS Credentinals are shared in my status update email to tony@tutuka.com

Password: AWS Credentinals are shared in my status update email to tony@tutuka.com

Note: This user will be deactivated on 01-Aug-2021 12:00:00 UTC.

Region: `Asia Pacific (Mumbai) ap-south-1`

### Accessible Resources
- Elastic Container Registry (ECR)
- Elastic Container Service (ECS)
- CloudFormation Stack
- Elasticache (Redis)
- Cloudwatch Logs
- EC2 Instances
- CodePipeline
- CodeBuild
- VPC
- S3

### Cloud formation templates
- [EC2](./Ec2-tutuka.yml)
- [Redis](./Tukaka-Redis.yml)
- [ECS](./Tutuka-ECS-Cluster.yml)
- [Security groups](./tutuka-SecurityGroups.yml) 

### CodeBuild spec
- [CodeBuild](./buildspec.yml)

## Unit Tests 
- [PubSubClientTest.java](./src/test/java/com/tutuka/pubsubclient/PubSubClientTest.java)
- [PubSubConsumerTest.java](./src/test/java/com/tutuka/redis/PubSubConsumerTest.java)
- [publish-to-test-server-fail.json](./src/test/resources/wiremock/mappings/publish-to-test-server-fail.json)
- [publish-to-test-server-success.json](./src/test/resources/wiremock/mappings/publish-to-test-server-success.json)

### Unit Test Report
[Sample test report here](./build/reports/tests/test/index.html)

![sample test report screenshot](https://github.com/MohanSha/tutuka/blob/master/Test%20Report%20Screenshot.png)
