###########Payment Initiation service#########

>>>>Step 1:
#Create a certificate using "CN Name = Sandbox-TPP" using below command,
>  keytool -genkeypair -alias senderKeyPair -keyalg RSA -keysize 2048 -dname "CN=Sandbox-TPP" -validity 365 -storetype PKCS12 -keystore sender_keystore.p12 -storepass changeit
>  keytool -exportcert -alias senderKeyPair -storetype PKCS12 -keystore sender_keystore.p12 -file sender_certificate.cer -rfc -storepass changeit
>  keytool -importcert -trustcacerts -alias root -file sender_certificate.cer -keystore trustStore.jks

#It will generate the below list of files,
>  sender_keystore.p12
>  sender_certificate.cer
>  trustStore.jks

>>>>Step 2: 
#Run the Spring boot application with inbuilt tomcat server

>>>>Step 3: 
#Configure Maven using pom.xml file and do the run the below command

>  mvn clean
>  mvn package

#It will generate Runnable jar file into the following location

>   /target/payment-initiation-0.0.1-SNAPSHOT.jar

>>>>Step 4:
#Run the jar file using command

>  java -jar /target/payment-initiation-0.0.1-SNAPSHOT.jar

>>>>Step 5: 
#Test the application using Postman tool

#Configure the inputs required for the application
#1. Add the X-Request-Id, Signature and Signature-Certificate in the header tab(provided in example-signature.md file)
#2. Add the Request body as a JSON (provided in example-signature.md file)
#3. Add the certificate(.p12) into Postman --> Settings --> Certificate tab --> Add Certificate and provide password as "changeit"
#4. Trigger the below url as a "POST" method and click on "Send"

>   https://localhost:8080/v1.0.0/initiate-payment

