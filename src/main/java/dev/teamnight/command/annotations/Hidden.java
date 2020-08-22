package dev.teamnight.command.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a command method or module class, that
 * should be hidden from a help command.
 * 
 * @author Jonas
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Hidden {}
