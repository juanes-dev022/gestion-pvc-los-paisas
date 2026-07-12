package com.gestionpvc.applications.interfaces.iusecase;

public interface IbaseUsecase<D,R> {
    R execute(D data);
}