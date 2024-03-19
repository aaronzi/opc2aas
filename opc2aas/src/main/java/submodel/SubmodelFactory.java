package submodel;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.*;
import org.eclipse.digitaltwin.basyx.InvokableOperation;
import org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;

public class SubmodelFactory {

    public static Submodel creationSubmodel() {
        List<LangStringTextType> description = new ArrayList<LangStringTextType>();
        description.add(new DefaultLangStringTextType.Builder().language("de-DE")
            .text("CreationSubmodel")
            .build());
        List<LangStringNameType> displayName = new ArrayList<LangStringNameType>();
        displayName.add(new DefaultLangStringNameType.Builder().language("de-DE")
            .text("CreationSubmodel")
            .build());
        Operation creation = createAASFromOPCNodeStructure();
        List<SubmodelElement> smeList = Arrays.asList(creation);

        Submodel submodel = new DefaultSubmodel.Builder().category("TestCategory")
            .description(description)
            .displayName(displayName)
            .id("CreationSubmodel")
            .idShort("CreationSubmodel")
            .kind(ModellingKind.INSTANCE)
            .submodelElements(smeList)
            .build();

        return submodel;
    }
    private static String readFileAsString(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
    private static String readResourceAsString(String resourcePath) throws IOException {
        try (InputStream inputStream = SubmodelFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static Submodel outputSubmodel() throws IOException {
        String aas = "aas";
        String consumerFile = "consumerFile";
        String extractvalue = "extractvalue";
        String jsonatatransformer = "jsonatatransformer";
        String jacksontransformer = "jacksontransformer";
        String aasserver = "aasserver";
        String route = "route";


// Check if the files exist before reading them
        /*if (Objects.nonNull(readResourceAsString("AasEnvConfig/aas_environment.aasx"))) {
            aas = readResourceAsString("AasEnvConfig/aas_environment.aasx");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/opcuaconsumer.json"))) {
            consumerFile = readResourceAsString("DataBridgeConfig/opcuaconsumer.json");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/jsonataExtractValue.json"))) {
            extractvalue = readResourceAsString("DataBridgeConfig/jsonataExtractValue.json");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/jsonatatransformer.json"))) {
            jsonatatransformer = readResourceAsString("DataBridgeConfig/jsonatatransformer.json");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/jsonjacksontransformer.json"))) {
            jacksontransformer = readResourceAsString("DataBridgeConfig/jsonjacksontransformer.json");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/aasserver.json"))) {
            aasserver = readResourceAsString("DataBridgeConfig/aasserver.json");
        }
        if (Objects.nonNull(readResourceAsString("DataBridgeConfig/routes.json"))) {
            route = readResourceAsString("DataBridgeConfig/routes.json");
        }*/
        List<LangStringTextType> description = new ArrayList<LangStringTextType>();
        description.add(new DefaultLangStringTextType.Builder().language("de-DE")
            .text("OutputSubmodel")
            .build());
        List<LangStringNameType> displayName = new ArrayList<LangStringNameType>();
        displayName.add(new DefaultLangStringNameType.Builder().language("de-DE")
            .text("OutputSubmodel")
            .build());
        SubmodelElement generatedAAS = new DefaultFile.Builder()
            .value(aas)
            .idShort("aas")
            .build();
        SubmodelElement OPCUAConsumerFile = new DefaultFile.Builder()
            .value(consumerFile)
            .idShort("consumerFile")
            .build();
        SubmodelElement ExtractValueFile = new DefaultFile.Builder()
            .value(extractvalue)
            .idShort("extractvalue")
            .build();
        SubmodelElement JsonataTransformerFile = new DefaultFile.Builder()
            .value(jsonatatransformer)
            .idShort("jsonatatransformer")
            .build();

        SubmodelElement JsonJacksonTransformerFile = new DefaultFile.Builder()
            .value(jacksontransformer)
            .idShort("jacksontransformer")
            .build();
        SubmodelElement AASServerFile = new DefaultFile.Builder()
            .value(aasserver)
            .idShort("aasserver")
            .build();
        SubmodelElement RoutesFile = new DefaultFile.Builder()
            .value(route)
            .idShort("route")
            .build();

        List<SubmodelElement> smeList = Arrays.asList(generatedAAS, OPCUAConsumerFile, ExtractValueFile, JsonataTransformerFile, JsonJacksonTransformerFile, AASServerFile, RoutesFile);

        Submodel submodel = new DefaultSubmodel.Builder().category("TestCategory")
            .description(description)
            .displayName(displayName)
            .id("OutputSubmodel")
            .idShort("OutputSubmodel")
            .kind(ModellingKind.INSTANCE)
            .submodelElements(smeList)
            .build();

        return submodel;
    }

    public static Operation createAASFromOPCNodeStructure() {
        return new InvokableOperation.Builder()
            .idShort("AASfromOPC")
            .inputVariables(Arrays.asList(createStringOperationVariable("aasIdShort"),
                createStringOperationVariable("opcNodeId"),
                createStringOperationVariable("opcServerUrl"),
                createStringOperationVariable("opcUsername"),
                createStringOperationVariable("opcPassword"),
                createStringOperationVariable("submodelRepoUrl")))
            .invokable(SubmodelFactory::creation)
            .build();
    }
    private static Operation createInvokableOperation() {
        return new InvokableOperation.Builder()
            .idShort("creationOperation")
            .invokable(SubmodelFactory::creation)
            .build();
    }

    private static OperationVariable createOperationVariable(Property val) {
        return new DefaultOperationVariable.Builder().value(val).build();
    }

    private static DefaultOperationVariable createStringOperationVariable(String idShort) {
        return new DefaultOperationVariable.Builder().value(new DefaultProperty.Builder().idShort(idShort).valueType(DataTypeDefXsd.STRING).build()).build();
    }

    public static OperationVariable[] creation(OperationVariable[] inputs) {

        String aasIdShort = ((Property) inputs[0].getValue()).getValue();
        String opcNodeId = ((Property) inputs[1].getValue()).getValue();
        String opcServerUrl = ((Property) inputs[2].getValue()).getValue();
        String opcUsername = ((Property) inputs[3].getValue()).getValue();
        String opcPassword = ((Property) inputs[4].getValue()).getValue();
        String submodelRepoUrl = ((Property) inputs[5].getValue()).getValue();

        OpcToAas.processOperation(aasIdShort, opcNodeId, opcServerUrl, opcUsername, opcPassword, submodelRepoUrl);

        return new OperationVariable[0];
    }
}
