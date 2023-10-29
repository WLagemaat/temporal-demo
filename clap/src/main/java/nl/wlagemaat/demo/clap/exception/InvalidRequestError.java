package nl.wlagemaat.demo.clap.exception;

public class InvalidRequestError extends RuntimeException{

    public InvalidRequestError(String message){
        super(message);
    }

    public InvalidRequestError(String message, Throwable e){
        super(message, e);
    }
}
