package nova.core.factory;

import nova.core.util.NovaException;

public class WrapperBrokenException extends NovaException {
    public WrapperBrokenException() {
        super("Show this crashlog/log to the wrapper producer. This should have not happened if wrapper was codded properly");
    }
    public WrapperBrokenException(String cause) {
        super(cause + "\nShow this crashlog/log to the wrapper producer. This should have not happened if wrapper was codded properly");
    }

}
