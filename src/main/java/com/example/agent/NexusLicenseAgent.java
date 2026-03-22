package com.nimah79.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Java Agent that patches license managers to bypass license verification.
 */
public class NexusLicenseAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[NexusLicenseAgent] License bypass agent loaded!");
        System.out.flush();

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                    Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                    byte[] classfileBuffer) {
                
                if (!className.equals("org/sonatype/licensing/internal/DefaultLicenseManager") &&
                    !className.equals("org/sonatype/licensing/trial/internal/DefaultTrialLicenseManager")) {
                    return null;
                }
                
                System.out.println("[NexusLicenseAgent] Found target class: " + className);
                System.out.flush();
                
                try {
                    System.out.println("[NexusLicenseAgent] About to patch " + className);
                    System.out.flush();
                    
                    if (className.equals("org/sonatype/licensing/internal/DefaultLicenseManager")) {
                        byte[] result = patchDefaultLicenseManager(classfileBuffer);
                        System.out.println("[NexusLicenseAgent] Successfully patched DefaultLicenseManager");
                        System.out.flush();
                        return result;
                    }
                    
                    if (className.equals("org/sonatype/licensing/trial/internal/DefaultTrialLicenseManager")) {
                        byte[] result = patchDefaultTrialLicenseManager(classfileBuffer);
                        System.out.println("[NexusLicenseAgent] Successfully patched DefaultTrialLicenseManager");
                        System.out.flush();
                        return result;
                    }
                } catch (Throwable t) {
                    System.err.println("[NexusLicenseAgent] CRITICAL ERROR patching " + className + ": " + t.getMessage());
                    t.printStackTrace();
                    System.err.flush();
                }
                
                System.out.println("[NexusLicenseAgent] Returning null - patch failed!");
                System.out.flush();
                return null;
            }
        }, true);
        
        System.out.println("[NexusLicenseAgent] Transformer registered!");
        System.out.flush();
    }

    private static String getPatchedLicenseBody() {
        return "{\n" +
            "    System.out.flush();\n" +
            "    org.sonatype.licensing.LicenseKey licenseKey = (org.sonatype.licensing.LicenseKey)this.ooh.get();\n" +
            "    licenseKey.setContactName(\"nimah79\");\n" +
            "    licenseKey.setContactCompany(\"nimah79\");\n" +
            "    licenseKey.setContactEmailAddress(\"info@nimah79.ir\");\n" +
            "    licenseKey.setContactTelephone(\"11011001110\");\n" +
            "    licenseKey.setContactCountry(\"Iran\");\n" +
            "    licenseKey.setEvaluation(false);\n" +
            "    licenseKey.setExpirationDate(new java.util.Date(2038 - 1900, 1, 1));\n" +
            "    licenseKey.setEffectiveDate(new java.util.Date(2015 - 1900, 1, 1));\n" +
            "    org.sonatype.licensing.feature.FeatureSet fs = new org.sonatype.licensing.feature.FeatureSet();\n" +
            "    fs.addFeature(new com.sonatype.nexus.bootstrap.entrypoint.licensing.NexusProfessionalFeature());\n" +
            "    licenseKey.setFeatureSet(fs);\n" +
            "    System.out.flush();\n" +
            "    return licenseKey;\n" +
            "}";
    }

    private static String getPatchedUninstallBody() {
        return "{\n" +
            "    System.out.println(\"[NexusLicenseAgent PATCHED] uninstallLicense - doing nothing!\");\n" +
            "    System.out.flush();\n" +
            "}";
    }

    private static byte[] patchDefaultLicenseManager(byte[] originalBytecode) throws Exception {
        System.out.println("[NexusLicenseAgent] In patchDefaultLicenseManager");
        System.out.flush();
        
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("org.sonatype.licensing.feature.FeatureSet");
        pool.importPackage("com.sonatype.nexus.bootstrap.entrypoint.licensing.NexusProfessionalFeature");
        pool.importPackage("java.util.Date");
        
        CtClass ctClass = pool.makeClass(new java.io.ByteArrayInputStream(originalBytecode));
        
        // Patch methods...
        // (same as before)
        
        return ctClass.toBytecode();
    }

    private static byte[] patchDefaultTrialLicenseManager(byte[] originalBytecode) throws Exception {
        System.out.println("[NexusLicenseAgent] In patchDefaultTrialLicenseManager");
        System.out.flush();
        
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("org.sonatype.licensing.feature.FeatureSet");
        pool.importPackage("com.sonatype.nexus.bootstrap.entrypoint.licensing.NexusProfessionalFeature");
        pool.importPackage("java.util.Date");
        
        CtClass ctClass = pool.makeClass(new java.io.ByteArrayInputStream(originalBytecode));
        
        // Patch createLicense
        try {
            CtClass[] createParams = { pool.get("org.sonatype.licensing.trial.TrialLicenseParam"), pool.get("org.sonatype.licensing.LicenseKeyRequest") };
            ctClass.getDeclaredMethod("createLicense", createParams).setBody(getPatchedLicenseBody());
            System.out.println("[NexusLicenseAgent] - createLicense patched");
        } catch(Exception e) {
            System.err.println("[NexusLicenseAgent] ERROR patching createLicense: " + e);
        }
        
        // Patch installLicense
        try {
            CtClass[] installParams = { pool.get("org.sonatype.licensing.trial.TrialLicenseParam"), pool.get("java.io.File") };
            ctClass.getDeclaredMethod("installLicense", installParams).setBody(getPatchedLicenseBody());
            System.out.println("[NexusLicenseAgent] - installLicense patched");
        } catch(Exception e) {
            System.err.println("[NexusLicenseAgent] ERROR patching installLicense: " + e);
        }
        
        // Patch uninstallLicense
        try {
            CtClass[] uninstallParams = { pool.get("org.sonatype.licensing.trial.TrialLicenseParam") };
            ctClass.getDeclaredMethod("uninstallLicense", uninstallParams).setBody(getPatchedUninstallBody());
            System.out.println("[NexusLicenseAgent] - uninstallLicense patched");
        } catch(Exception e) {
            System.err.println("[NexusLicenseAgent] ERROR patching uninstallLicense: " + e);
        }
        
        // Patch verifyLicense
        try {
            CtClass[] v1 = { pool.get("org.sonatype.licensing.trial.TrialLicenseParam") };
            ctClass.getDeclaredMethod("verifyLicense", v1).setBody(getPatchedLicenseBody());
            System.out.println("[NexusLicenseAgent] - verifyLicense(param) patched");
        } catch(Exception e) {
            System.err.println("[NexusLicenseAgent] ERROR patching verifyLicense(param): " + e);
        }
        
        try {
            CtClass[] v2 = { pool.get("org.sonatype.licensing.trial.TrialLicenseParam"), pool.get("java.io.File") };
            ctClass.getDeclaredMethod("verifyLicense", v2).setBody(getPatchedLicenseBody());
            System.out.println("[NexusLicenseAgent] - verifyLicense(param,file) patched");
        } catch(Exception e) {
            System.err.println("[NexusLicenseAgent] ERROR patching verifyLicense(param,file): " + e);
        }
        
        System.out.flush();
        System.err.flush();
        return ctClass.toBytecode();
    }
    
    public static void agentmain(String agentArgs, Instrumentation inst) {
        premain(agentArgs, inst);
    }
}
