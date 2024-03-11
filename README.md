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

Please open this projects' [`pom.xml`](./pom.xml) and copy all code in the `<plugins>` section (lines 130 - 194) to your pom.xml. Afterwards, adjust the package names used in the plugin definitions to match your projects individual naming patterns.

### Step 4: Add all necessary configuration to `application.properties`
To allow your BFF to seamlessly connect to OneCX Search Config SVC and correctly generate boilerplate code, you need to add a few configuration properties to your `application.properties`. If you did already set up a working Quarkus BFF, this file should be located in `src/main/resources`. All of the properties mentioned in this section of the guide can also be found in this projects' [`applications.properties`](./src/main/resources/application.properties).

**Configure the REST client generator**

Please add the following properties to correctly configure the REST client generator.
````properties
# Folder in which download plugin from pom.xml placed API spec of Search Config SVC
quarkus.openapi-generator.codegen.input-base-dir=target/tmp/openapi
````
````properties
# config-key for generated REST client (you'll need this later)
quarkus.openapi-generator.codegen.spec.onecx_search_config_v1_yaml.config-key=onecx_search_config_v1

# Base package for the generated client
quarkus.openapi-generator.codegen.spec.onecx_search_config_v1_yaml.base-package=gen.org.tkit.onecx.searchconfig.v1.client

# Other configuration properties
quarkus.openapi-generator.codegen.spec.onecx_search_config_v1_yaml.return-response=true
quarkus.openapi-generator.codegen.spec.onecx_search_config_v1_yaml.additional-api-type-annotations=@org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
````

**Specify deployment URL of OneCX Search Config SVC**

Please add the following property to ensure that your BFF knows where it can reach OneCX Search Config SVC. The key placed between `rest-client.` and `.url` has to match the config-key that you added to your properties in the previous step.
````properties
%prod.quarkus.rest-client.onecx_search_config_v1.url=http://onecx-search-config-svc:8080
````
*In this case we only specify the property for prod environments. Depending on your use case it might also make sense to define this property for dev and test environments.*

**Add search-config specific configuration**
````properties
# App ID of UI that BFF will be used by
onecx.YOUR_BFF_NAME.ui-app-id=app

# Name of product that this BFF is a part of
## onecx.permissions.product-name property is used as a default if this isn't specified
onecx.YOUR_BFF_NAME.product-name=product

# Enable or disable search config fetching globally for your BFF
onecx.YOUR_BFF_NAME.searchconfig.enabled=true
````

### Step 5: Create config mapper
To be able to easily access the search-config specific config properties in other parts of our BFF it makes sense to create a ConfigMapper. This mapper will provide us with a straightforward interface that we can use to access all necessary config values without having to inject all properties separately.

As a starting point please copy all contents of [OnecxSearchConfigIntegrationExampleBFFConfig.java](./src/main/java/org/tkit/onecx/search/config/integration/bff/rs/mappers/OnecxSearchConfigIntegrationExampleBFFConfig.java) to a file called `<YOUR_BFF_NAME>Config.java`. Afterwards make sure to change the interfaces name to match the name of your file. Additionally, please change `@ConfigMapping(prefix = "onecx.search-config-integration-example-bff")` to `@ConfigMapping(prefix = "onecx.YOUR_BFF_NAME")`. Lastly check that all imports match your package structure.

### Step 6: Generate all sources
Run `mvn clean compile` to execute all plugins and generate the boilerplate source code necessary for the following steps of this guide.

### Step 7: Copy other mappers
Copy all [`mappers`](./src/main/java/org/tkit/onecx/search/config/integration/bff/rs/mappers/) that don't yet exist in your project to your `mappers` folder. If some of the files in this projects' `mappers` folder already exist in your project, please make sure that the contents of the files match to ensure all mappings to work properly. Lastly check that all imports match your package structure.

### Step 8: Create `SearchConfigRestController.java` and verify integration
Add a new controller called `SearchConfigRestController.java` to your BFFs `controllers` directory, copy all contents of [`SearchConfigRestController.java`](./src/main/java/org/tkit/onecx/search/config/integration/bff/rs/controllers/SearchConfigRestController.java) to the newly created file and change the name of the config mapper in line 34 to match the name of your config mapper. To finish the integration of search configs into your BFF check that all imports in `SearchConfigRestController.java` match your package structure and run `mvn clean compile quarkus:dev` to verify that everything is working as expected.

### Step 9: Add unit tests
Tests for the search config integration are currently WIP. Instructions on how to add unit tests covering the integration to your project will be added to this guide soon.


