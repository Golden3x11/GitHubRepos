package com.example.githubrepos.exeptions;

public class DataRetrievalException extends RuntimeException {
    public DataRetrievalException(String message) {
        super(message);
    }
}