package nl.wlagemaat.demo.vroem.exception;

public class VroemTechnicalError extends RuntimeException{

    public VroemTechnicalError(String msg){
        super(msg);
    }

    public VroemTechnicalError(String msg, Throwable cause) {
        super(msg, cause);
    }
}
