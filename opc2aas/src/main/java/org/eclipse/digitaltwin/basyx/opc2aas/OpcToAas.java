package org.eclipse.digitaltwin.basyx.opc2aas;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXSerializer;
import submodel.SubmodelFactory;

public class OpcToAas {
    private static final Logger logger = LoggerFactory.getLogger(OpcToAas.class);
    private static String aasIdShort;
    private static String opcNodeId;
    private static String opcServerUrl;
    private static String opcUsername;
    private static String opcPassword;

    /**
     * The main method of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            System.out.println("hello");
            logger.info("OPC2AAS started");
            //SubmodelFactory creationsubmodel = new SubmodelFactory();
            //String[] inputs = creationsubmodel.
            SubmodelFactory.creationSubmodel(); // Calling the creationSubmodel Method
            //SubmodelFactory.createAASFromOPCNodeStructure();
            System.out.println("hello2");
            System.out.println("hello3");
            initializeDataBridgeConfig();
            System.out.println("hello4");
            OpcUaClient client = createOpcUaClientConnection();
            System.out.println("hello5");
            NodeInfo subTree = readOpcUaSubtree(client);
            Environment generatedAAS = createAasEnvironment(client, subTree);
            exportAasAsFile(generatedAAS);
            logger.info("AAS saved to aas_environment.aasx");
            SubmodelFactory.outputSubmodel();
        } catch (Exception e) {
            logger.error("An error occurred during the runtime of OPC2AAS: ", e);
        }
    }
    public static void processOperation(String aasIdShort, String opcNodeId, String opcServerUrl, String opcUsername, String opcPassword) {
        // The input parameters
        OpcToAas.aasIdShort = aasIdShort;
        OpcToAas.opcNodeId = opcNodeId;
        OpcToAas.opcServerUrl = opcServerUrl;
        OpcToAas.opcUsername = opcUsername;
        OpcToAas.opcPassword = opcPassword;
        System.out.println(opcServerUrl);
    }
    //public static String getaasId() {
    //   return aasIdShort;
    //}

    //public static String getNodeId() {
    //    return opcNodeId;
    //}

    //public static String getServerUrl() {
    //    return opcServerUrl;
    //}

    //public static String getUsername() {
    //    return opcUsername;
    //}

    //public static String getPassword() {
    //    return opcPassword;
    //}


    /**
     * Initializes the DataBridge configuration files.
     *
     * @throws IOException If the configuration files cannot be created.
     */
    private static void initializeDataBridgeConfig() throws IOException {
        DataBridgeConfig.createConfigFiles();
        logger.info("DataBridge configuration files initialized");
    }

    /**
     * Creates an OPC UA client connection.
     *
     * @return The OPC UA client connection.
     * @throws Exception If the connection cannot be created.
     */
    private static OpcUaClient createOpcUaClientConnection() throws Exception {
        System.out.println("hello6");
        opcServerUrl = System.getenv("opcServerUrl"); // URL of the OPC UA server
        opcUsername = System.getenv("opcUsername"); // username for the OPC UA server
        opcPassword = System.getenv("opcPassword"); // password for the OPC UA server
        System.out.println(opcServerUrl);
        OpcUaClient client = OpcUtils.createClientConnection(opcServerUrl, opcUsername, opcPassword);
        System.out.println("hello8");
        logger.info("OPC UA client connection created");
        return client;
    }

    /**
     * Reads the OPC UA subtree.
     *
     * @param client The OPC UA client connection.
     * @return The OPC UA subtree.
     * @throws Exception If the subtree cannot be read.
     */
    private static NodeInfo readOpcUaSubtree(OpcUaClient client) throws Exception {
        // specify the NodeId of the starting node
        NodeId opcNodeId = OpcUtils.stringToNodeId(System.getenv("START_NODE_ID"));
        // read the subtree starting from the specified starting node
        NodeInfo subTree = OpcUtils.readEntireSubtree(client, opcNodeId);
        logger.info("OPC UA subtree read");

        return subTree;
    }

    /**
     * Creates an AAS environment.
     *
     * @param client  The OPC UA client connection.
     * @param subTree The OPC UA subtree.
     * @return The AAS environment.
     * @throws Exception If the AAS environment cannot be created.
     */
    private static Environment createAasEnvironment(OpcUaClient client, NodeInfo subTree) throws Exception {
        String serverApplicationUri = OpcUtils.getServerApplicationUri(client);
        String submodelRepositoryUrl = System.getenv("SUBMODEL_REPOSITORY_URL");
        System.out.println(submodelRepositoryUrl);
        System.out.println("hello");
        String aasIdShort = System.getenv("AAS_ID_SHORT");
        String opcServerUrl = System.getenv("ENDPOINT_URL"); // URL of the OPC UA server
        String opcUsername = System.getenv("USERNAME"); // username for the OPC UA server
        String opcPassword = System.getenv("PASSWORD"); // password for the OPC UA server
        Environment environment = AasBuilder.createEnvironment(aasIdShort, serverApplicationUri, subTree, opcServerUrl, opcUsername, opcPassword, submodelRepositoryUrl);
        logger.info("AAS created");
        return environment;
    }

    /**
     * Exports the AAS as a file.
     *
     * @param environment The AAS environment.
     * @throws IOException            If the file cannot be written.
     * @throws SerializationException If the AAS cannot be serialized.
     */
    private static void exportAasAsFile(Environment environment) throws IOException, SerializationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AASXSerializer().write(environment, new ArrayList<>(), out);
        writeByteArrayToFile(out.toByteArray());
    }

    /**
     * Writes a byte array to a file.
     *
     * @param content The byte array to write.
     * @throws IOException If the file cannot be written.
     */
    private static void writeByteArrayToFile(byte[] content) throws IOException {
        File envFolder = new File("AasEnvConfig");
        if (!envFolder.exists() && !envFolder.mkdir()) {
            logger.error("Failed to create directory: {}", envFolder.getAbsolutePath());
            return;
        }

        File file = new File(envFolder, "aas_environment.aasx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
            logger.debug("Written content to file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error writing to file: {}", file.getAbsolutePath(), e);
            throw e;
        }
    }
}