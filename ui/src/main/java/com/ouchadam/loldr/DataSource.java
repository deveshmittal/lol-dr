package com.ouchadam.loldr;

public interface DataSource<T> {

    int size();

    T get(int position);

    void close();

}
