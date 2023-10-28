package nl.wlagemaat.demo.clap.exception;

public class UnknownInsuranceCaseError extends RuntimeException{

    public UnknownInsuranceCaseError(String message){
        super(message);
    }

    public UnknownInsuranceCaseError(String message, Throwable e){
        super(message, e);
    }
}
