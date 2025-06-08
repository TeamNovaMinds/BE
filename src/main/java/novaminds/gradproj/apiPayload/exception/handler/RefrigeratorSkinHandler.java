package novaminds.gradproj.apiPayload.exception.handler;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;

public class RefrigeratorSkinHandler extends GeneralException {
    public RefrigeratorSkinHandler(ErrorStatus errorStatus) {
        super(errorStatus);
    }

    public RefrigeratorSkinHandler(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }
}
