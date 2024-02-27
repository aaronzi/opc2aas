package submodel;

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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.basyx.InvokableOperation;
import org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas;

public class SubmodelFactory {

    private static final String DATA_BRIDGE_CONFIG_FOLDER = "DataBridgeConfig";
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

    public static Submodel outputSubmodel() {
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

        SubmodelElement generatedAAS = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement OPCUAConsumerFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement ExtractValueFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement JsonataTransformerFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement JsonJacksonTransformerFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement AASServerFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        SubmodelElement RoutesFile = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        Operation creation = createAASFromOPCNodeStructure();
        List<SubmodelElement> smeList = Arrays.asList(generatedAAS, OPCUAConsumerFile,
                ExtractValueFile, JsonataTransformerFile, JsonJacksonTransformerFile,
                AASServerFile, RoutesFile, creation);

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

    private static Operation createAASFromOPCNodeStructure() {
        return new InvokableOperation.Builder()
                .idShort("AASfromOPC")
                .inputVariables(Arrays.asList(createStringOperationVariable("aasIdShort"),
                        createStringOperationVariable("opcNodeId"),
                        createStringOperationVariable("opcServerUrl"),
                        createStringOperationVariable("opcUsername"),
                        createStringOperationVariable("opcPassword")))
                .invokable(SubmodelFactory::creation)
                .build();
    }

    private static OperationVariable createOperationVariable(Property val) {
        return new DefaultOperationVariable.Builder().value(val).build();
    }

    private static DefaultOperationVariable createStringOperationVariable(String idShort) {
        return new DefaultOperationVariable.Builder().value(new DefaultProperty.Builder().idShort(idShort).valueType(DataTypeDefXsd.STRING).build()).build();
    }

    private static OperationVariable[] creation(OperationVariable[] inputs) {

        String aasIdShort = ((Property) inputs[0].getValue()).getValue();
        String opcNodeId = ((Property) inputs[1].getValue()).getValue();
        String opcServerUrl = ((Property) inputs[2].getValue()).getValue();
        String opcUsername = ((Property) inputs[3].getValue()).getValue();
        String opcPassword = ((Property) inputs[4].getValue()).getValue();
   //         in.setIdShort("result");
   //         results[i] = createOperationVariable(in);

        OpcToAas.processOperation(aasIdShort, opcNodeId, opcServerUrl, opcUsername, opcPassword);


        //Integer squared = val * val;
        //in.setValue(squared.toString());
        //in.setIdShort("result");
        return new OperationVariable[0];
    }
}
