package nl.wlagemaat.demo.vroem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
	
	@Bean
	// Copy Spring's ObjectMapper where we are in control of the bean name, so that with the cucumber
	// spring context we inject the right one in our production code
	// NOTE: spring registers a bunch of stuff depending on libraries available on the classpath, so that's
	// why jackson2ObjectMapperBuilder is used here.
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		return jackson2ObjectMapperBuilder.indentOutput(true).build();
	}
}