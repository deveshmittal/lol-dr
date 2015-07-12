package com.ouchadam.loldr;

public interface SourceProvider<T> extends DataSource<T> {

    void swap(DataSource<T> source);

}
