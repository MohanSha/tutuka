# Technical Task for testing and deployment engineer
![Build Status](https://codebuild.ap-south-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiQTVqNmdtQWVVb05UbVJnM1VuV29md2hTWmprWDVpRGJ1d1A0WmlPM0t2ZlFQZFFHL1pNc0tPYnIvbVV4OThPamUvc2VjZE5WVWRiU0ZtSm9hVitxejB3PSIsIml2UGFyYW1ldGVyU3BlYyI6Ikk4U0doektRL20zb2svbW8iLCJtYXRlcmlhbFNldFNlcmlhbCI6Mn0%3D&branch=master)

## AWS Credentials to review infrastructure:
I have created a temporary IAM user with read access for the resources used for my project in my AWS account. You can use the credentials to review the infra setup.

Console Link: https://973744485172.signin.aws.amazon.com/console



Note: This user will be deactivated on 01-Aug-2021 12:00:00 UTC.

Region: `Asia Pacific (Mumbai) ap-south-1`

### Accessible Resources:
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

### Cloud formation templates:
- [EC2](./Ec2-tutuka.yml)
- [Redis](./Tukaka-Redis.yml)
- [ECS](./Tutuka-ECS-Cluster.yml)
- [Security groups](./tutuka-SecurityGroups.yml) 

## Unit Test Report:
[Sample test report here](./build/reports/tests/test/index.html)

![sample test report screenshot](https://github.com/MohanSha/tutuka/blob/master/Test%20Report%20Screenshot.png)















