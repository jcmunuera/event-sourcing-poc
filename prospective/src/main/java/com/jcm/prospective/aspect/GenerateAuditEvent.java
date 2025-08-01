package com.jcm.prospective.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateAuditEvent {
    boolean includeArguments() default true;  // Incluir argumentos (input)
    boolean includeResponse() default true;  // Incluir respuesta (output)
}
