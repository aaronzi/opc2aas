package org.eclipse.digitaltwin.basyx.opc2aas;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXSerializer;
import submodel.SubmodelFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpcToAas {
    private static final Logger logger = LoggerFactory.getLogger(OpcToAas.class);
    private static String aasIdShort;
    private static String opcNodeId;
    private static String opcServerUrl;
    private static String opcUsername;
    private static String opcPassword;
    private static String submodelRepositoryUrl;

    private static final String BASE_URL = "http://localhost:9085";
    private static final String SUBMODEL_PATH = "/submodels/T3V0cHV0U3VibW9kZWw/submodel-elements/consumerFile/$value";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private static final String requestBody = "{\"contentType\": \"image/jpg\", \"value\": \"https://images.creativefabrica.com/products/previews/2023/12/19/x2ik76fgx/2afYycRGXuaW1qqa4C3TUclGGy1-mobile.jpg\"}";



    /**
     * The main method of the application.
     *
     * @param args The command line arguments.
     */
    public static void main() {
        try {

            logger.info("OPC2AAS started");

            initializeDataBridgeConfig();

            OpcUaClient client = createOpcUaClientConnection();

            NodeInfo subTree = readOpcUaSubtree(client);
            Environment generatedAAS = createAasEnvironment(client, subTree);

            exportAasAsFile(generatedAAS);
            //Environment generatedNewAAS = createNewAasEnvironment(client,subTree);
            //exportAasAsJsonFile(generatedNewAAS);
            logger.info("AAS saved to aas_environment.aasx");
            updateOutputSubmodel();
            //SubmodelFactory.outputSubmodel();
            logger.info("Output Submodel Updated");

        } catch (Exception e) {
            logger.error("An error occurred during the runtime of OPC2AAS: ", e);
        }
    }

    private static void updateOutputSubmodel() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n   \"contentType\": \"image/jpg\",\r\n   \"value\": \"https://images.creativefabrica.com/products/previews/2023/12/19/x2ik76fgx/2afYycRGXuaW1qqa4C3TUclGGy1-mobile.jpg\"\r\n}\r\n");
        Request request = new Request.Builder()
                .url("http://localhost:8081/submodels/T3V0cHV0U3VibW9kZWw/submodel-elements/consumerFile/$value?level=core")
                .method("PATCH", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
    }

    private static String readResourceAsString(String resourcePath) throws IOException {
        try (InputStream inputStream = SubmodelFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static void processOperation(String aasIdShort, String opcNodeId, String opcServerUrl, String opcUsername, String opcPassword, String submodelRepositoryUrl) {
        // The input parameters
        OpcToAas.aasIdShort = aasIdShort;
        OpcToAas.opcNodeId = opcNodeId;
        OpcToAas.opcServerUrl = opcServerUrl;
        OpcToAas.opcUsername = opcUsername;
        OpcToAas.opcPassword = opcPassword;
        OpcToAas.submodelRepositoryUrl = submodelRepositoryUrl;
        main();

    }



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

        System.out.println(opcServerUrl);
        OpcUaClient client = OpcUtils.createClientConnection(opcServerUrl, opcUsername, opcPassword);

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
        NodeId nodeId = OpcUtils.stringToNodeId(opcNodeId); //String NodeId is taken from Submodel and converted to NodeID type
        // read the subtree starting from the specified starting node
        NodeInfo subTree = OpcUtils.readEntireSubtree(client, nodeId);
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
        Environment environment = AasBuilder.createEnvironment(aasIdShort, serverApplicationUri, subTree, opcServerUrl, opcUsername, opcPassword, submodelRepositoryUrl);
        logger.info("AAS created");
        return environment;
    }

    private static Environment createNewAasEnvironment(OpcUaClient client, NodeInfo subTree) throws Exception {
        String serverApplicationUri = OpcUtils.getServerApplicationUri(client);
        Environment environment = AasBuilder.createNewEnvironment(aasIdShort, serverApplicationUri, subTree, opcServerUrl, opcUsername, opcPassword, submodelRepositoryUrl);
        logger.info("New AAS created");
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
//        File envFolder = new File("AasEnvConfig");
//        if (!envFolder.exists() && !envFolder.mkdir()) {
//            logger.error("Failed to create directory: {}", envFolder.getAbsolutePath());
//            return;
//        }

        File file = new File("aas_environment.aasx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
            logger.debug("Written content to file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error writing to file: {}", file.getAbsolutePath(), e);
            throw e;
        }
    }

    private static void exportAasAsJsonFile(Environment environment) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(environment);
        writeStringToFile(jsonContent);
    }
    private static void writeStringToFile(String content) throws IOException {
        File envFolder = new File("AasEnvConfig");
        if (!envFolder.exists() && !envFolder.mkdir()) {
            logger.error("Failed to create directory: {}", envFolder.getAbsolutePath());
            return;
        }

        File file = new File(envFolder, "aas_new_environment.json");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            logger.debug("Written content to file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error writing to file: {}", file.getAbsolutePath(), e);
            throw e;
        }
    }

}