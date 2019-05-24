import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CommandLineJobRunner을 이용하여 돌리기 위해서는 다음과 같이 돌려야 한다.
 *   빌드는 다음과 같이 한다. -> "mvn package spring-boot:repackage" ( brew install maven 했다고 가정 )
 *   실행은 다음과 같이 한다. -> "java -jar target/SpringBatchCommandLineJobRunnerExample.jar SpringBatchCommandLineJobRunnerExampleConfig job1 message=hi"
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchCommandLineJobRunnerExampleConfig {

    Logger logger = LoggerFactory.getLogger(SpringBatchCommandLineJobRunnerExampleConfig.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job1() {
        return jobBuilderFactory.get("job1")
            .incrementer(new RunIdIncrementer())
            .start(step1()).build();
    }

    private TaskletStep step1() {
        Tasklet tasklet = (contribution, context) -> {
            logger.info(String.format("This is from tasklet step with parameter -> %s", context.getStepContext().getJobParameters().get("message")));
            return RepeatStatus.FINISHED;
        };
        return stepBuilderFactory.get("step1").tasklet(tasklet).build();
    }

}
