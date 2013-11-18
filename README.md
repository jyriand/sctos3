==README==

Installation guide:

1. Open src/main/resource/app.properties file and fill in  empty properties with your soundcloud client id,
  AWS accessKey, AWS secretKey and S3 bucketName.

2. Run mvn package
3. To start standalone server run java -jar target/sctos3-1.0-war-exec.jar
4. Navigate to http://localhost:8080
