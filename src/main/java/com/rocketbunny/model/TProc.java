package com.rocketbunny.model;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class TProc<T> {
    private T lopRes;
    private T rop;
    private Operation operation;
    private Supplier<T> zeroSupplier;
    private BiFunction<T, T, T> addFunction;
    private BiFunction<T, T, T> subFunction;
    private BiFunction<T, T, T> mulFunction;
    private BiFunction<T, T, T> divFunction;
    private Function<T, T> inverseFunction;
    private Function<T, T> squareFunction;

    public enum Operation { NONE, ADD, SUB, MUL, DIV }
    public enum FunctionType { REV, SQR }

    public TProc(Supplier<T> zeroSupplier, BiFunction<T, T, T> addFunction, BiFunction<T, T, T> subFunction,
                 BiFunction<T, T, T> mulFunction, BiFunction<T, T, T> divFunction,
                 Function<T, T> inverseFunction, Function<T, T> squareFunction) {
        this.zeroSupplier = zeroSupplier;
        this.addFunction = addFunction;
        this.subFunction = subFunction;
        this.mulFunction = mulFunction;
        this.divFunction = divFunction;
        this.inverseFunction = inverseFunction;
        this.squareFunction = squareFunction;
        reset();
    }

    public void reset() {
        lopRes = zeroSupplier.get();
        rop = zeroSupplier.get();
        operation = Operation.NONE;
    }

    public void operationClear() {
        operation = Operation.NONE;
    }

    public void operationRun() {
        if (operation == Operation.NONE) {
            return;
        }
        switch (operation) {
            case ADD:
                lopRes = addFunction.apply(lopRes, rop);
                break;
            case SUB:
                lopRes = subFunction.apply(lopRes, rop);
                break;
            case MUL:
                lopRes = mulFunction.apply(lopRes, rop);
                break;
            case DIV:
                lopRes = divFunction.apply(lopRes, rop);
                break;
        }
    }

    public void functionRun(FunctionType func) {
        switch (func) {
            case REV:
                rop = inverseFunction.apply(rop);
                break;
            case SQR:
                rop = squareFunction.apply(rop);
                break;
        }
    }

    public T getLeftOperand() {
        return lopRes;
    }

    public void setLeftOperand(T operand) {
        lopRes = operand;
    }

    public T getRightOperand() {
        return rop;
    }

    public void setRightOperand(T operand) {
        rop = operand;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation oprtn) {
        operation = oprtn;
    }
}
