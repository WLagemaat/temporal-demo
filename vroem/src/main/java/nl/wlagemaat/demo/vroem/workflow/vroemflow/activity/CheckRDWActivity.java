package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.model.ValidatedFineDto;

@ActivityInterface
public interface CheckRDWActivity extends VroemActivityMarker {
	
	@ActivityMethod(name = "Retrieve licenseplate data from RDW system")
    FineProcessingResult determineLicenseplate(FineDto transgression);
}