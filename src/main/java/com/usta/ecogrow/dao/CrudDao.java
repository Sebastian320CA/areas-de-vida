package com.usta.ecogrow.dao;

import java.util.List;

/** Contrato genérico de acceso a datos (reemplaza a GenericDao). */
public interface CrudDao<T> {

    List<T> findAll();

    /** Inserta si id == 0, de lo contrario actualiza. Devuelve el registro guardado. */
    T save(T item);

    void delete(int id);
}
