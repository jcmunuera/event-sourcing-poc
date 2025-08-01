package com.jcm.prospective.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca los atributos que deben ser enmascarados o excluidos en los eventos.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SensitiveField {
    boolean mask() default true; // true para enmascarar, false para omitir
}

