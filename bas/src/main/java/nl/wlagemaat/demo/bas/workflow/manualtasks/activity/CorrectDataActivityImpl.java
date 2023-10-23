package nl.wlagemaat.demo.bas.workflow.manualtasks.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CorrectDataActivityImpl implements CorrectDataActivity {

	@Override
	public boolean correctData(FineDto transgression) {
		return true;
	}
}