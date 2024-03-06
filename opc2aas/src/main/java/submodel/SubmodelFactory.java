package submodel;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
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

public class SubmodelFactory {

    public static final String AASX_PATH = "AasEnvConfig/aas_environment.aasx";
    public static final String DATA_BRIDGE_FOLDER = "DataBridgeConfig";
    public static Submodel creationSubmodel() {
        List<LangStringTextType> description = new ArrayList<LangStringTextType>();
        description.add(new DefaultLangStringTextType.Builder().language("de-DE")
                .text("CreationSubmodel")
                .build());
        List<LangStringNameType> displayName = new ArrayList<LangStringNameType>();
        displayName.add(new DefaultLangStringNameType.Builder().language("de-DE")
                .text("CreationSubmodel")
                .build());
        List<Key> refKeys = new ArrayList<Key>();
        refKeys.add(new DefaultKey.Builder().value("123")
                .build());

        SubmodelElement sme1 = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        Operation creation = createAASFromOPCNodeStructure();
        List<SubmodelElement> smeList = Arrays.asList(sme1, creation);

        Submodel submodel = new DefaultSubmodel.Builder().category("TestCategory")
                .description(description)
                .displayName(displayName)
                .id("CreationSubmodel")
                .idShort("CreationSubmodel")
                .kind(ModellingKind.INSTANCE)
                .semanticId(new DefaultReference.Builder().keys(refKeys)
                        .build())
                .submodelElements(smeList)
                .build();

        return submodel;
    }
    private static String readFileAsString(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }

    public static Submodel outputSubmodel() throws IOException {
        String aas = readFileAsString(AASX_PATH);
        String consumerFile = readFileAsString("DataBridgeConfig/opcuaconsumer.json");
        String extractvalue = readFileAsString("DataBridgeConfig/jsonataExtractValue.json");
        String jsonatatransformer = readFileAsString("DataBridgeConfig/jsonatatransformer.json");
        String jacksontransformer = readFileAsString("DataBridgeConfig/jsonjacksontransformer.json");
        String aasserver = readFileAsString("DataBridgeConfig/aasserver.json");
        String route = readFileAsString("DataBridgeConfig/routes.json");


        List<LangStringTextType> description = new ArrayList<LangStringTextType>();
        description.add(new DefaultLangStringTextType.Builder().language("de-DE")
                .text("OutputSubmodel")
                .build());
        List<LangStringNameType> displayName = new ArrayList<LangStringNameType>();
        displayName.add(new DefaultLangStringNameType.Builder().language("de-DE")
                .text("OutputSubmodel")
                .build());
        List<Key> refKeys = new ArrayList<Key>();
        refKeys.add(new DefaultKey.Builder().value("123")
                .build());

        SubmodelElement test = new DefaultFile.Builder()
                .value("")
                .idShort("id")
                .build();
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
        //Operation creation = createAASFromOPCNodeStructure();
        List<SubmodelElement> smeList = Arrays.asList(generatedAAS, OPCUAConsumerFile,
                ExtractValueFile, JsonataTransformerFile, JsonJacksonTransformerFile,
                AASServerFile, RoutesFile);

        Submodel submodel = new DefaultSubmodel.Builder().category("TestCategory")
                .description(description)
                .displayName(displayName)
                .id("OutputSubmodel")
                .idShort("OutputSubmodel")
                .kind(ModellingKind.INSTANCE)
                .semanticId(new DefaultReference.Builder().keys(refKeys)
                        .build())
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
                .idShort("translateOperation")
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

   //         in.setIdShort("result");
   //         results[i] = createOperationVariable(in);

        OpcToAas.processOperation(aasIdShort, opcNodeId, opcServerUrl, opcUsername, opcPassword, submodelRepoUrl);


        //Integer squared = val * val;
        //in.setValue(squared.toString());
        //in.setIdShort("result");
        return new OperationVariable[0];
    }
}
