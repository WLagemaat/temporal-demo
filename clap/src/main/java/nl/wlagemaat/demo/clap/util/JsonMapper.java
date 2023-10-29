package nl.wlagemaat.demo.clap.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clap.exception.TechnicalError;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// wrapper around ObjectMapper to get rid of all the checked exceptions
public class JsonMapper {
	
	private final ObjectMapper objectMapper;
	
	public <T> T parseJson(String json, Class<T>  clazz) {
		return parseJson(objectMapper, json, clazz);
	}
	
	public <T> T parseJson(String json, TypeReference<T> clazz) {
		return parseJson(objectMapper, json, clazz);
	}
	
	public <T> T parseJson(ObjectMapper objectMapper, String json, TypeReference<T>  clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new TechnicalError("unable to parse JSON", e);
		}
	}
	
	public <T> T parseJson(ObjectMapper objectMapper, String json, Class<T>  clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new TechnicalError("unable to parse JSON", e);
		}
	}
	
	public String toJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new TechnicalError("unable to write JSON", e);
		}
	}
}