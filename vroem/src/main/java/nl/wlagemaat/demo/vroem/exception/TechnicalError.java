package nl.wlagemaat.demo.vroem.exception;

public class TechnicalError extends RuntimeException{

    public TechnicalError(String message){
        super(message);
    }
}
