package com.gubee.poc.infrastructure.web.util;

public record Message<T>( String message, T payload) {
}
