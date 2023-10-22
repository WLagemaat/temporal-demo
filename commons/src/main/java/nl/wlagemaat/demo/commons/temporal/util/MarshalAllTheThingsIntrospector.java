package nl.wlagemaat.demo.commons.temporal.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

// this Jackson component makes sure we can marshal fields that are marked as ignore or READ/WRITE_ONLY
public class MarshalAllTheThingsIntrospector extends JacksonAnnotationIntrospector {
		// ignore @JsonIgnore annotations when generating test data
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return false;
		}
		
		// ignore access = READON_ONLY / WRITE_ONLY when generating test data
		@Override
		public JsonProperty.Access findPropertyAccess(final Annotated m) {
			return JsonProperty.Access.READ_WRITE;
		}
	}