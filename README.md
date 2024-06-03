# opc2aas
Asset Administration Shell Service that translates OPC UA Node Structures to Asset Administration Shells

## OPC UA Testserver

To test the functionality of the opc2aas service, you can use the OPC UA Testserver. It is a docker container that provides a simple OPC UA server with a few nodes. The docker image is available at [aaronzi/demo-opc-server](https://hub.docker.com/r/aaronzi/demo-opc-server).

### Prerequisites
Make sure you have Docker and Docker Compose installed on your system.

### Starting the OPC UA Testserver
Run the following command to start the test server:

```bash
docker-compose up -d
```

## opc2aas Service
### Environment variables
In its current state, the opc2aas service takes the input of the following environment variables:

| Variable                  | Description                                                                            | Input values                               |
|---------------------------|----------------------------------------------------------------------------------------|--------------------------------------------|
| `SUBMODEL_REPOSITORY_URL` | The URL of the BaSyx Submodel Repository where the AASs Submodels will be uploaded to. | http://host.docker.internal:9081/submodels |
| `ENDPOINT_URL`            | The URL of the OPC UA server that should be used.                                      | opc.tcp://opcserver:4840                   |
| `START_NODE_ID`           | The NodeId of the node from which the opc2aas service should start.                    | ns=2;i=1                                   |
| `AAS_ID_SHORT`            | The idShort of the AAS that will be created.                                           | DemoOpcServer                              |
| `USERNAME` (optional)     | The username that will be used to authenticate with the OPC UA server.                 | test                                       |
| `PASSWORD` (optional)     | The password that will be used to authenticate with the OPC UA server.                 | test                                       |

### Configuring the Service
To configure the opc2aas service, input appropriate values for environment variables listed above in the _CreationSubmodel_ of the AAS DemoOpcServer using the AAS Web UI. Navigate to the following link to do that:

```
http://localhost:9080
```

After providing the inputs and executing the configuration, the service will generate seven files in the _OutputSubmodel_ if the connection to the OPC UA Node is successful.

### Generated Files
The following files are generated that can also be downloaded:

| File Name                | Description                                                                                                | 
|--------------------------|------------------------------------------------------------------------------------------------------------|
| ***aas***                | Generated AASX file.                                                                                       | 
| ***consumerFile***       | DataBridge configuration file for the OPC UA Consumer.                                                     | 
| ***extractvalue***       | Transformer configuration file to extract the OPC UA value from the Java Node Object.                      | 
| ***jsonatatransformer*** | Transformer configuration file.                                                                            | 
| ***jacksontransformer*** | Transformer configuration file.                                                                            | 
| ***aasserver***          | DataBridge configuration file for the AAS Environment datasink.                                            |
| ***route***              | DataBridge configuration file for the routes between the OPC UA Consumer and the AAS Environment datasink. |
