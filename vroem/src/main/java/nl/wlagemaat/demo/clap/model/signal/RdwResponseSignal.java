package nl.wlagemaat.demo.clap.model.signal;

import java.io.Serializable;

public record RdwResponseSignal(String driver, String workflowId) implements Serializable {
}
