package no.avec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Created by avec on 11/12/15.
 */

@SpringBootApplication
@EnableScheduling
public class Application {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private PortfolioService portfolioService;


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(
                Application.class);
        application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
        SpringApplication.run(Application.class, args);
    }
}
