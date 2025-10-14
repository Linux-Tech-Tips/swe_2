package com.example.tutorial_time.application.common;

public interface UseCase<Output, Input> {
    Output execute(Input input);
}
