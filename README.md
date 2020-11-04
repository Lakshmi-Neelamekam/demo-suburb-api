# Demo Suburb Api
> Api to retrieve suburb information by post code, suburb information (including post code) by suburb name, and to add new suburb and post code combinations.

## Project Set up

### RDS Database
Create an RDS Database on the AWS Console.
Ensure inbound rules on security group allow traffic from the IP you are running the spring boot app (local IP or EC2 IP)

### Local development

To point spring boot app to a different RDS instance, change the following in `application.properties`

`spring.datasource.url=jdbc:mysql://{your RDS instance name}:3306/demo
 spring.datasource.username={your RDS db username}
 spring.datasource.password={your RDS db password}`
 
To build the project, execute:

`./gradlew clean build`

To run application, execute:

`./gradlew bootRun`

#### Documentation 
Open Api spec available at [OpenApiSpec](documentation/openapi.yaml)

#### Authorization & Accessing the Endpoints
Use postman collection available at [ApiPostmanCollection](postman-collection/Demo-Suburb-Api.postman_collection)
Obtain an access token using `http://localhost:8080/oauth/token` 
Use the `access_token` from response on `GetSuburbByPostCode` and `GetSuburbByName` requests in the collection.
The collection will be improved later to inject the token through environment variables into the APIs

### EC2 Deployment
Login to AWS Console and launch an EC2 instance. It is sufficient to keep defaults and the free tier t2.micro instance type. As part of Security Group Configuration, add an entry to allow SSH access on port 22 from your IP

Generate a new key pair, save it locally. 

Locally, execute `./gradlew clean build`, this places the jar to be deployed at `build/libs`. Now copy the jar from your local into the EC2 instance. 
`scp -i {localPathToEc2KeyPair} {localRepoPath}\build\libs\demo-1.0.0.jar ec2-user@{dns of ec2 instance}:~`

SSH to instance using the key pair

Install java on the instance 
`sudo amazon-linux-extras install java-openjdk11`

Verify java version on the instance
`java -version`

Ensure RDS Security Group permits inbound traffic from this EC2 instance (EC2 Private IP address to be added to RDS Security Group)

Ensure EC2 Security Group permits inbound TCP traffic on port 8080

Run the application on EC2 instance
`java -jar demo-1.0.0.jar`

Update the EC2 requests in the postman collection with the DNS of the created EC2 instance and hit the APIs.

### Improvements to be done
The following improvements can be done, but haven't been so far due to time constraints and an attempt to first build an overall working solution.
- Configure Checkstyle
- Jacoco thresholds to be defined for instruction/line/branch/complexity coverage to fail build when they are not met.
- Exclusions for test coverage - eg. Mapstruct Impl classes that are auto-generated.
- Fail build based on OWASP dependencyCheckAnalyze
- OWASP report generation as part of build.
- Postman collection to be automated as much, to inject the accesstoken/urls based on env variable.
- Automation of creating AWS resources using CloudFormation.
- Reactive Spring has not been tried here although it is supposed to be more efficient because of its non-blocking nature. Reactive Spring and Relational database have not yet become compatible with each other because JDBC is blocking in general and can be a bottleneck to Reactive Spring and not allow us to reap its benefits. Looks like R2DBC can be used which provides JPA functionality with Reactive Streams. 
- Checking if the postCode suburb combination is valid - is there any geolocation service to validate this ? not sure.
- Dockerise the application, push image to a registry like ECR, scan the container for vulnerabilities using a tool like Clair.
- Distributed tracing, use Spring Sleuth to add trace/span information for every integration. 
- In reality, users will be coming from JDBC/LDAP so userDetailsService will need to be implemented by our own with a UserDetails model and JPA repository to fetch the users.
- ClientId and secret used for basic authentication is used from memory, can come from JDBC.
- Validity of access token not defined, hence default behaviour applies.
- Scope not clarified between GET and POST methods (i.e GET having only read scope and POST requiring write scope)