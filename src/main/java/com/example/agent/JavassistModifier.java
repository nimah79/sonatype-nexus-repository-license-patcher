package com.nimah79.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Example: Using Javassist to modify bytecode.
 * This shows how to inject code into a target class.
 */
public class JavassistModifier {

    /**
     * Modify bytecode using Javassist library.
     * Adds logging before target method execution.
     */
    public static byte[] modifyWithJavassist(byte[] originalBytecode) {
        try {
            ClassPool pool = ClassPool.getDefault();
            
            // Load the class from bytecode
            CtClass ctClass = pool.makeClass(new ByteArrayInputStream(originalBytecode));
            
            // Get all methods and add instrumentation
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                System.out.println("[JavassistModifier] Found method: " + method.getName());
                
                // Example: Add logging before method execution
                // Uncomment to add logging:
                // method.insertBefore("{ System.out.println(\"[LOG] Entering: " + method.getName() + "\"); }");
            }
            
            return ctClass.toBytecode();
            
        } catch (CannotCompileException | IOException e) {
            System.err.println("[JavassistModifier] Error modifying bytecode: " + e.getMessage());
            e.printStackTrace();
            return originalBytecode; // Return original on failure
        }
    }

    /**
     * Example: Replace method implementation entirely.
     */
    public static byte[] replaceMethod(byte[] originalBytecode, String methodName, String newImplementation) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(new ByteArrayInputStream(originalBytecode));
            
            CtMethod method = ctClass.getDeclaredMethod(methodName);
            method.setBody(newImplementation);
            
            return ctClass.toBytecode();
            
        } catch (NotFoundException | CannotCompileException | IOException e) {
            System.err.println("[JavassistModifier] Error replacing method: " + e.getMessage());
            return originalBytecode;
        }
    }
}
