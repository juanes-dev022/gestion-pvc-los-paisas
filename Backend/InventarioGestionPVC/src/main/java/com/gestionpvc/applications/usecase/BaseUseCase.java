package com.gestionpvc.applications.usecase;
import com.gestionpvc.applications.Exceptions.CustomBadRequestException;
import com.gestionpvc.applications.interfaces.iusecase.IbaseUsecase;


public abstract class BaseUseCase<D,R> implements IbaseUsecase<D,R> {
        @Override
    public final R execute(D data) {
        return doExecute(data);
    }

    protected abstract R doExecute(D data);

    protected <T> T ensureNotNull(T value, String message) {
        if (value == null) {
            throw new CustomBadRequestException(message);
        }
        return value;
    }
    
}
