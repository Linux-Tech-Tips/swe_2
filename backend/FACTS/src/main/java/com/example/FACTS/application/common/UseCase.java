package com.example.FACTS.application.common;

public interface UseCase<Output, Input> {
    Output execute(Input input);
}
