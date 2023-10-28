package nl.wlagemaat.demo.clap.exception;

public class TechnicalError extends RuntimeException{

    public TechnicalError(String message){
        super(message);
    }

    public TechnicalError(String message, Throwable e){
        super(message, e);
    }
}
