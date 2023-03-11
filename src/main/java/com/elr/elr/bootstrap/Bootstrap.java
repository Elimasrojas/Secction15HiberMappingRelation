package com.elr.elr.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    /*
    * SE EJECUTA E INICIA CON SPING CONTEXT
    * */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("I was called!");
    }
}