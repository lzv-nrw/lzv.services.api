[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d24b672713a646e48b2b3a66e2e08de5)](https://app.codacy.com/gh/lzv-nrw/lzv.services.api/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

# LZV Services API #

## About ##

The LZV Services API provides a RESTFul-API for to validate file formats and migrate file formats.

Recently the API is limited to PDF services. It currently make use of veraPDF to check if a PDF file is compliant to any PDF/A flavour. Integration of Callas PDF/A-Pilot is on the roadmap as the Callas Tool also provide PDF/A generation.
    
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

- Maven 3.x (e.g. MVN 3.9.9) for integration and deployment

- git 2.x

- (Eclipse 2024/12 for convenience)

For server deployment:
 
- Apache Tomcat 10.x for server deployment - please be aware that doesn't run with Tomcat 9 or earlier due to the jersey and jakarta libraries used

- (Apache Webserver or HaProxy for proxying Tomcat for production) 


## Installation ##

Server installation (requires running Tomcat 10) 

- Clone Repository with `git clone https://github.com/lzv-nrw/lzv.services.api.git`

- change into local directory `cd lzv.services.api`

- run `mvn clean test war:war`

If all tests successfully passed you will find file `lzv-api.war` in newly created directory `target`

- copy file into webapps directory in your Tomcat container application server

## Running on localhost ##

For testing or development purposes lzv.services.pdf brings a jetty server with it. 

- After cloning software with git and changed into newly created directory `lzv.services.api` you can either run `mvn jetty:run`from console or

- import lzv.services.pdf as new maven project into Eclipse and run jetty by configure "Run Configuration" with goal jetty:run

- jetty starts a Web server running under `http://localhost:8080`

- you can reach the available services by within our browser via `http://localhost:8080/lzv-api/about` which gives you a list of available services. 
  
## Use API calls ##

With cUrl or any other tool for web requests you can request the available endpoints.

Available EndPoints are:

- `https://your.server/lzv-api/version/[pdfbox|verapdf|pdfapilot]` GET

- `https://your.server/lzv-api/validate/[pdfbox|verapdf|pdfapilot]` POST

- `https://your.server/lzv-api/createpdfa/pdfapilot` POST

where `[plugin]` is mandatory


### simple GET example ### 
 
usage of:

`curl -XGET -H "Accept: application/json" http://localhost/lzv-api/version/veradpdf > test.json` 

will return  

`[{"plugin" : "PDFA-Validation with veraPDF","serviceInfo" : {"veraPDF Version" : "1.26.5"}}]` into new file test.json


### use validate endpoints ###

usage of: 

`curl -XPOST -H "Accept: application/json" --form file='@src/test/resources/pdfa_1b.pdf' http://localhost:8080/lzv-api/validate/verapdf` 

will give you version and available metadata from the pdfa_1b.pdf test file as json response


usage of: 

`curl -XPOST --form file='@src/test/resources/pdfa_1b.pdf' http://localhost:8080/lzv-api/validate/verapdf` 

will give you the validation result for the pdfa_1b.pdf test file as html response


Apache pdfBox and Callas pdfaPilot can be used in the same manner by replacing plugin identifier `verapdf` with `pdfbox` or `pdfapilot`
  

### create PDF/A via endpoint ###

PDF/A creation is limited currently to the pdfaPilot plugin, as this is the only plugin providing different scenarios for PDF/A creation.  

`curl -XPOST -H "Accept: text/html" -F file='@src/test/resources/pdf.pdf' -F flavour=1b  http://localhost:8080/lzv-api/convert/pdfapilot`

The `flavour` parameter is mandatory for this endpoint

