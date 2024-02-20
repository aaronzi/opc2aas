package submodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class SubmodelFactory {
    public Submodel create() {
        List<LangStringTextType> description = new ArrayList<LangStringTextType>();
        description.add(new DefaultLangStringTextType.Builder().language("de-DE")
                .text("Test")
                .build());
        List<LangStringNameType> displayName = new ArrayList<LangStringNameType>();
        displayName.add(new DefaultLangStringNameType.Builder().language("de-DE")
                .text("Test")
                .build());
        List<Key> refKeys = new ArrayList<Key>();
        refKeys.add(new DefaultKey.Builder().value("123")
                .build());

        SubmodelElement sme1 = new DefaultProperty.Builder()
                .value("123")
                .idShort("test")
                .build();
        Operation square = createInvokableOperation();
        List<SubmodelElement> smeList = Arrays.asList(sme1, square);

        Submodel submodel = new DefaultSubmodel.Builder().category("TestCategory")
                .description(description)
                .displayName(displayName)
                .id("Example")
                .idShort("example")
                .kind(ModellingKind.INSTANCE)
                .semanticId(new DefaultReference.Builder().keys(refKeys)
                        .build())
                .submodelElements(smeList)
                .build();

        return submodel;
    }

    private static Operation createInvokableOperation() {
        return new InvokableOperation.Builder()
                .idShort("AASfromOPC")
                .inputVariables(Arrays.asList(createStringOperationVariable("aasIdShort"),
                        createStringOperationVariable("opcNodeId"),
                        createStringOperationVariable("opcServerUrl"),
                        createStringOperationVariable("opcUsername"),
                        createStringOperationVariable("opcPassword")))
                .invokable(SubmodelFactory::square)
                .build();
    }

    private static OperationVariable createOperationVariable(Property val) {
        return new DefaultOperationVariable.Builder().value(val).build();
    }

    private static DefaultOperationVariable createStringOperationVariable(String idShort) {
        return new DefaultOperationVariable.Builder().value(new DefaultProperty.Builder().idShort(idShort).valueType(DataTypeDefXsd.STRING).build()).build();
    }

    private static OperationVariable[] square(OperationVariable[] inputs) {
        Property in = (Property) inputs[0].getValue();
        Integer val = Integer.valueOf(in.getValue());
        Integer squared = val * val;
        in.setValue(squared.toString());
        in.setIdShort("result");
        return new OperationVariable[] { createOperationVariable(in) };
    }
}
