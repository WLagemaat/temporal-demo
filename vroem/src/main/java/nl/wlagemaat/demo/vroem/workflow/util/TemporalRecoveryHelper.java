package nl.wlagemaat.demo.vroem.workflow.util;

import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import lombok.experimental.UtilityClass;

import javax.xml.datatype.DatatypeConfigurationException;

@UtilityClass
public class TemporalRecoveryHelper {
	
	public static <I,O> O tryOrRecover(InvokeActivity<I, O> ia, RecoverableActivity<I, O> r, I i) {
		try {
			return ia.invoke(i);
		} catch (ActivityFailure | DatatypeConfigurationException e) { // ActivityFailure occurs after retries
			// TODO discuss if DatatypeConfigurationException should be here
			ApplicationFailure cause = (ApplicationFailure) e.getCause();
			return r.recover(i, cause.getType(), cause.getOriginalMessage());
		}
	}
	
	public interface RecoverableActivity<I,O> {
		O recover(I input, String exceptionType, String msg);
	}
	
	public interface InvokeActivity<I,O> {
		O invoke(I input) throws DatatypeConfigurationException;
	}
}