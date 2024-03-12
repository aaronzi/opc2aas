package submodel;
import org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import org.eclipse.digitaltwin.basyx.submodelrepository.component.SubmodelRepositoryComponent;
import org.springframework.context.ApplicationContext;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepository;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication(
        scanBasePackages = {"org.eclipse.digitaltwin.basyx", "org.eclipse.digitaltwin.basyx.opc2aas"},
        exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
//@ComponentScan("org.eclipse.digitaltwin.basyx")
public class SubmodelRepositoryComponentForOpc {
    public static void main(String[] args) throws IOException {


            ApplicationContext context= SpringApplication.run(SubmodelRepositoryComponentForOpc.class, args);

            SubmodelRepository repo = context.getBean(SubmodelRepository.class);

            repo.createSubmodel(SubmodelFactory.creationSubmodel());
            repo.createSubmodel(SubmodelFactory.outputSubmodel());


    }


}
