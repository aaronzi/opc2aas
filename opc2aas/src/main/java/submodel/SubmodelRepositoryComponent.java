package submodel;
import org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import org.eclipse.digitaltwin.basyx.submodelservice.SubmodelServiceFactory;
import org.eclipse.digitaltwin.basyx.submodelservice.feature.DecoratedSubmodelServiceFactory;
import org.eclipse.digitaltwin.basyx.submodelservice.feature.SubmodelServiceFeature;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepository;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepositoryFactory;
import org.eclipse.digitaltwin.basyx.submodelrepository.feature.DecoratedSubmodelRepositoryFactory;
import org.eclipse.digitaltwin.basyx.submodelrepository.feature.SubmodelRepositoryFeature;
import org.springframework.context.annotation.Primary;

import java.util.List;

@SpringBootApplication(
        scanBasePackages = {"org.eclipse.digitaltwin.basyx", "org.eclipse.digitaltwin.basyx.opc2aas"},
        exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SubmodelRepositoryComponent {
    public static void main(String[] args) {
        System.out.println("Hello0");
        ApplicationContext context= SpringApplication.run(SubmodelRepositoryComponent.class, args);
        System.out.println("Hello1");
        SubmodelRepository repo = context.getBean(SubmodelRepository.class);
        System.out.println("Hello2");
        repo.createSubmodel(SubmodelFactory.creationSubmodel());
        repo.createSubmodel(SubmodelFactory.outputSubmodel());
    }


}
