# opc2aas
Asset Administration Shell Service that translates OPC UA Node Structures to Asset Administration Shells

## OPC UA Testserver

To test the functionality of the opc2aas service, you can use the OPC UA Testserver. It is a docker container that provides a simple OPC UA server with a few nodes. The docker image is available at [aaronzi/demo-opc-server](https://hub.docker.com/r/aaronzi/demo-opc-server).
To start the container, run the following command:

```bash
docker run -d --name opcserver -p 4840:4840 -p 8080:8080 --health-cmd='curl -f http://localhost:8080/health || exit 1' --health-interval=10s --health-timeout=5s --health-retries=3 --restart always aaronzi/demo-opc-server:v1.0.0
```

## opc2aas Service

In its current state, the opc2aas service is a java application that should be used with the following specified environment variables:

| Variable                  | Description                                                                            |
|---------------------------|----------------------------------------------------------------------------------------|
| `SUBMODEL_REPOSITORY_URL` | The URL of the BaSyx Submodel Repository where the AASs Submodels will be uploaded to. |
| `ENDPOINT_URL`            | The URL of the OPC UA server that should be used.                                      |
| `START_NODE_ID`           | The NodeId of the node from which the opc2aas service should start.                    |
| `AAS_ID_SHORT`            | The idShort of the AAS that will be created.                                           |
| `USERNAME` (optional)     | The username that will be used to authenticate with the OPC UA server.                 |
| `PASSWORD` (optional)     | The password that will be used to authenticate with the OPC UA server.                 |

## ToDos:

- [ ] Use BaSyx SubmodelRepository Component to represent the opc2aas service as an AAS itself (two Submodels that will be linked to an OPC2AAS AAS)
- [ ] Implement the following two Submodels as interfaces to the opc2aas service:
  - [ ] A Submodel called "CreationSubmodel" that includes an Operation called "CreateAASFromOPCNodeStructure". The Operation should have the following parameters:
    - [ ] A String called "aasIdShort" that represents the idShort of the AAS that will be created.
    - [ ] A String called "opcNodeId" that represents the NodeId of the node from which the opc2aas service should start.
    - [ ] A String called "opcServerUrl" that represents the URL of the OPC UA server that should be used.
    - [ ] A String called "opcUsername" that represents the username that will be used to authenticate with the OPC UA server.
    - [ ] A String called "opcPassword" that represents the password that will be used to authenticate with the OPC UA server.
  - [ ] A Submodel called "OutputSubmodel" that includes File-SubmodelElements for the generated AAS and DataBridge configuration. The following files should be included:
    - [ ] "GeneratedAAS" including the generated AASX file.
    - [ ] "OPCUAConsumerFile" including the DataBridge configuration file for the OPC UA Consumer.
    - [ ] "ExtractValueFile" including the transformer configuration file to extract the OPC UA value from the Java Node Object.
    - [ ] "JsonataTransformerFile" including the transformer configuration file.
    - [ ] "JsonJacksonTransformerFile" including the transformer configuration file.
    - [ ] "AASServerFile" including the DataBridge configuration file for the AAS Environment datasink.
    - [ ] "RoutesFile" including the DataBridge configuration file for the routes between the OPC UA Consumer and the AAS Environment datasink.
- [ ] Add a Dockerfile to build a docker image of the opc2aas service
- [ ] Add a docker-compose file to start a demo environment with the opc2aas service, the OPC UA Testserver, the BaSyx AAS Environment, the BaSyx Registries and the DataBridge
