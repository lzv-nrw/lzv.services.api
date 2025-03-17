[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d24b672713a646e48b2b3a66e2e08de5)](https://app.codacy.com/gh/lzv-nrw/lzv.services.pdf/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

# About #

## About the lzv.services.pdf ##

The LZV Services for PDF Files are WEB-API based services to validate PDF/A.

They currently make use of veraPDF to check if a PDF file is compliant to any PDF/A flavour. 
Integration of Callas PDF/A-Pilot is on the roadmap as the Callas Tool also provide PDF/A generation.
    
API for veraPDF encloses:

- about: lists services available (GET, html)
- version: returns version of veraPDF libraries in use (GET, html, json)
- upload: form for uploading PDF to validate
- validate: service for veraPDF validation (POST, html)

## License ##

lzv.services.pdf are licensed under [MIT Licence](LICENSE)

veraPDF is licensed under [GNU General public license GPLv3+](https://docs.verapdf.org/develop/LICENSE.GPL)

## Prerequisites ##

For compilation and packaging:

- OpenJDK 1.11
- Maven 3.x (e.g.MVN 3.9.9) for integration and deployment
- git 2.x
- (Eclipse 2024/12 for convenience)

For server deployment:
 
- Apache Tomcat 10.x for server deployment - please be aware that doesn't run with Tomcat 9 or earlier due to the jersey and jakarta libraries used
- (Apache Webserver or HaProxy for proxying Tomcat for production) 


## Installation ##

Server installation (requires running Tomcat 10) 
- Clone Repository with `git clone https://github.com/lzv-nrw/lzv.services.pdf.git`
- change into local directory `cd lzv.services.pdf`
- run `mvn clean test war:war`

If all tests successfully passed you will find file `pdfs.war` in newly created directory `target`

- copy file into webapps directory in your Tomcat container application server

## Running on localhost ##

For testing or development purposes lzv.services.pdf brings a jetty server with it. 

- After cloning software with git and changed into newly created directory `lzv.services.pdf` you can either run `mvn jetty:run`from console or
- import lzv.services.pdf as new maven project into Eclipse and run jetty by configure "Run Configuration" with goal jetty:run
- jetty starts a Web server running under `http://localhost:8080`
- you can reach the available services by within our browser via `http://localhost:8080/pdfs/about` which gives you a list of available services. 
  
## Use API calls ##

With cUrl or any other tool for web requests you can request the available endpoints.

- use `curl -XGET -H "Accept: application/json" http://localhost/pdfs/version > test.json` will write `[{"plugin" : "PDFA-Validation with veraPDF","serviceInfo" : {"veraPDF Version" : "1.26.5"}}]` into new file test.json
- use 
`curl -XPOST --form file='@src/test/resources/pdfa_1b.pdf'  http://localhost:8080/pdfs/validate` will give you the validation result for the pdfa_1b.pdf test file as html response
 


