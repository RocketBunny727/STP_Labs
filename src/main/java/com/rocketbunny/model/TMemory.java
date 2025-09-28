package com.rocketbunny.model;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TMemory<T> {
    private T fNumber;
    private State fState;
    private final Supplier<T> zeroSupplier;
    private final BiFunction<T, T, T> addFunction;

    public enum State { _ON, _OFF}

    public TMemory(Supplier<T> zeroSupplier, BiFunction<T, T, T> addFunction) {
        this.zeroSupplier = zeroSupplier;
        this.addFunction = addFunction;
        fNumber = zeroSupplier.get();
        fState = State._OFF;
    }

    public void store(T e) {
        fNumber = e;
        fState = State._ON;
    }

    public T get() {
        return fNumber;
    }

    public void add(T e) {
        fNumber = addFunction.apply(fNumber, e);
        fState = State._ON;
    }

    public void clear() {
        fNumber = zeroSupplier.get();
        fState = State._OFF;
    }

    public String getStateAsString() {
        return fState.name();
    }

    public T getNumber() {
        return get();
    }
}
