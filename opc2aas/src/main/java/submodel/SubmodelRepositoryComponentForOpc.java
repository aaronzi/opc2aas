package submodel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import org.springframework.context.ApplicationContext;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepository;

@SpringBootApplication(
        scanBasePackages = {"org.eclipse.digitaltwin.basyx", "org.eclipse.digitaltwin.basyx.opc2aas"},
        exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SubmodelRepositoryComponentForOpc {
    public static void main(String[] args) {
        try {
            System.out.println("Hello0");
            ApplicationContext context= SpringApplication.run(SubmodelRepositoryComponentForOpc.class, args);
            System.out.println("Hello1");
            SubmodelRepository repo = context.getBean(SubmodelRepository.class);
            System.out.println("Hello2");
            repo.createSubmodel(SubmodelFactory.creationSubmodel());
            System.out.println("Hello3");
            repo.createSubmodel(SubmodelFactory.outputSubmodel());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


}
