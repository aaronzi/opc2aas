package submodel;
import org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import org.springframework.context.ApplicationContext;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepository;

import java.io.IOException;

@SpringBootApplication(
        scanBasePackages = {"org.eclipse.digitaltwin.basyx", "org.eclipse.digitaltwin.basyx.opc2aas"},
        exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SubmodelRepositoryComponentForOpc {
    public static void main(String[] args) throws IOException {

            System.out.println("Hello0");
            ApplicationContext context= SpringApplication.run(SubmodelRepositoryComponentForOpc.class, args);
            System.out.println("Hello1");
            SubmodelRepository repo = context.getBean(SubmodelRepository.class);
            //OpcToAas opc = new OpcToAas();


            System.out.println("Hello2");
            repo.createSubmodel(SubmodelFactory.creationSubmodel());
            //opc.main();
            System.out.println("Hello3");
            repo.createSubmodel(SubmodelFactory.outputSubmodel());


    }


}
