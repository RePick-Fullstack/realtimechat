package repick.realtimechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@EnableKafka
@EnableJpaAuditing
@SpringBootApplication
public class RealTimeChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealTimeChatApplication.class, args);
    }

    @Bean
    public RecordMessageConverter converter(){
        return new JsonMessageConverter();
    }
}
