package nl.wlagemaat.demo.bas.workflow.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.temporal.common.converter.*;
import lombok.val;

/**
 * Since Temporal uses Jackson ObjectMapper by default, Jackson annotations present on the POJO's can
 * interfer with Temporal's ability to serialize/deserialize the entire object. That's why we customize
 * a DataConverter for Temporal with everything the same, except the ability of the ObjectMapper to also
 * process fields that are marked to be ignored.
 */
public class TemporalDataConverterHelper {
	// copied from the DefaultDataConverter, but with our custom Jackson mapper
	public static DataConverter createOmniscientJsonDataConverter() {
		return new DefaultDataConverter(
				new NullPayloadConverter(),
				new ByteArrayPayloadConverter(),
				new ProtobufJsonPayloadConverter(),
				new JacksonJsonPayloadConverter(defaultTemporalObjectMapperCustomized())
		);
	}
	
	// copied from JacksonJsonPayloadConverter default constructor
	private static ObjectMapper defaultTemporalObjectMapperCustomized() {
		val mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		// magic sauce:
		mapper.setAnnotationIntrospector(new MarshalAllTheThingsIntrospector());
		return mapper;
	}
}