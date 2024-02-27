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
import org.springframework.context.annotation.Bean;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepository;
import org.eclipse.digitaltwin.basyx.submodelrepository.SubmodelRepositoryFactory;
import org.eclipse.digitaltwin.basyx.submodelrepository.feature.DecoratedSubmodelRepositoryFactory;
import org.eclipse.digitaltwin.basyx.submodelrepository.feature.SubmodelRepositoryFeature;
import org.springframework.context.annotation.Primary;

import java.util.List;

@SpringBootApplication(
        scanBasePackages = {"org.eclipse.digitaltwin.basyx", "submodel", "org.eclipse.digitaltwin.basyx.opc2aas.OpcToAas"},
        exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SubmodelRepositoryComponent {
    public static void main(String[] args) {
        SpringApplication.run(SubmodelRepositoryComponent.class, args);
        OpcToAas.main(args);
    }
    @Bean
    @ConditionalOnMissingBean
    public SubmodelRepository getSubmodelRepository(SubmodelRepositoryFactory aasRepositoryFactory, List<SubmodelRepositoryFeature> features) {
        return new DecoratedSubmodelRepositoryFactory(aasRepositoryFactory, features).create();
    }

    @Primary
    @Bean
    public SubmodelServiceFactory getSubmodelServiceFactory(SubmodelServiceFactory aasServiceFactory, List<SubmodelServiceFeature> features) {
        return new DecoratedSubmodelServiceFactory(aasServiceFactory, features);
    }



}
