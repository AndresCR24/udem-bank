package com.udem.bank.web.controller;

import com.udem.bank.persistence.exception.UsuarioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        // Aquí puedes retornar un objeto ResponseEntity con un mensaje personalizado y un código de estado HTTP adecuado, por ejemplo, NOT_FOUND (404).
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Otros manejadores de excepciones globales...
}