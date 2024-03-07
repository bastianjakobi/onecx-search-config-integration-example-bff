# OneCX Search Config Integration
The goal of this repository is to provide you with a reference implementation and step-by-step guide for integrating a connection to the [OneCX Search Config SVC](https://github.com/onecx/onecx-search-config-svc/) into your own BFF. 

In the following sections of this document, you'll be guided through all the necessary steps for achieving a working integration. In each section of the guide, you'll be provided with links to related project files, from which you can copy and paste most of the necessary code.

## Prerequisites
This guide does not cover the actual setup of a BFF application but instead only focuses on the integration of OneCX Search Config SVC into an already existing BFF. We therefore expect you to create a working Quarkus 3 BFF before following the steps mentioned in this guide. Feel free to take a look at this projects' [`pom.xml`](./pom.xml), [`application.properties`](./src/main/resources/application.properties) and overall project structure to get a better understanding of how we set up our example BFF.

If you believe that this guide contains a mistake or doesn't cover all necessary integration steps, feel free to [fork this repo](https://github.com/bastianjakobi/onecx-search-config-integration-example-bff/fork) and submit a PR!

## Integration steps

### Step 1: Create OpenAPI spec for your BFF
If you haven't already done so please create the file `src/main/openapi/openapi-bff.yaml`. This file should contain all the endpoints that you plan to expose to a UI using your BFF. In the next step of this guide we'll use this file to define what search config related endpoints a UI can access when talking to your BFF and what data schemas should be used/expected in requests/responses. Depending on your use case and app you might also want to add other endpoints and schemas here. When setting up the file you should generally follow a structure similar to the one used in [this projects' OpenAPI spec](./src/main/openapi/openapi-bff.yaml). 

### Step 2: Add necessary paths and schemas to the OpenAPI spec
Open the previously created file located at `src/main/openapi/openapi-bff.yaml` and copy all the code from the sections called `paths` and `components` in [this projects' OpenAPI spec](./src/main/openapi/openapi-bff.yaml) to the respective sections in your OpenAPI spec.

### Step 3: Add required plugins to `pom.xml`
After defining the expectations that your BFF should fullfil, you can now add necessary plugins and source code generators to your `pom.xml`. We'll add two plugins which will allow us to integrate the BFF with OneCX Search Config SVC without having to write all boilerplate code ourselfs. The first one will generate interfaces and models based based on our previously defined expectations. The second pluging will download the OpenAPI specification for the actual OneCX Search Config SVC, which will later be used to generate a REST client which our BFF can use to communicate with the services API.

Please open this projects' [`pom.xml`](./pom.xml) and copy all code in the `<plugins>` section (lines 130 - 194) to your pom.xml.

### Step 4: Add all necessary configuration to `application.properties`
