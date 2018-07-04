package ru.javawebinar.topjava.storage;

import java.util.Collection;
import java.util.List;

public interface Storage<T> {

    void create(T t);
    void delete(int id);
    void update(int id, T t);
    T read(int id);
    List<T> getList();
}
