package com.nimah79.agent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Registers every known licensed feature on the fake license FeatureSet.
 *
 * The injected method bodies cannot reference these feature classes directly
 * (javassist compiles them eagerly and a feature class that is missing in an
 * older Nexus version would abort the whole patch), so features are looked up
 * reflectively in the Nexus class loader and every feature is registered on a
 * best-effort basis: a feature that does not exist in the running version is
 * skipped, everything else is added.
 */
public final class LicenseFeaturePatcher {

    private static final String BASE = "com.sonatype.nexus.bootstrap.entrypoint.";

    /**
     * Features whose constructor takes the professional feature
     * (they also register themselves as its sub-features).
     */
    private static final String[] PRO_FEATURES = {
        BASE + "analytics.AnalyticsFeature",
        BASE + "blobstore.azure.AzureBlobStoreFeature",
        BASE + "blobstore.gcloud.GoogleCloudBlobStoreFeature",
        BASE + "blobstore.group.BlobStoreGroupFeature",
        BASE + "blobstore.pro.s3.ProS3BlobStoreFeature",
        BASE + "cleanup.ProCleanupFeature",
        BASE + "crowd.CrowdFeature",
        BASE + "datastore.DataStoreConfigurationFeature",
        BASE + "exportimport.ExportImportFeature",
        BASE + "metrics.RepositoryMetricsFeature",
        BASE + "move.RepositoryMoveFeature",
        BASE + "oauth2.OAuth2Feature",
        BASE + "replication.ReplicationFeature",
        BASE + "repository.group.deploy.GroupWriteFeature",
        BASE + "saml.SamlFeature",
        BASE + "serviceaccount.ServiceAccountFeature",
        BASE + "staging.StagingFeature",
        BASE + "tags.TagsFeature",
        BASE + "usertoken.UserTokenFeature",
    };

    /**
     * Features with a no-argument constructor.
     */
    private static final String[] STANDALONE_FEATURES = {
        BASE + "licensing.NexusClmFeature",
        BASE + "licensing.NexusFirewallFeature",
        BASE + "analytics.telemetry.TelemetryOptOutFeature",
    };

    private LicenseFeaturePatcher() {
    }

    public static void populate(Object licenseKey, Object featureSet, Object proFeature) {
        ClassLoader loader = licenseKey.getClass().getClassLoader();
        try {
            Class<?> featureClass = Class.forName("org.sonatype.licensing.feature.Feature", true, loader);
            Method addFeature = featureSet.getClass().getMethod("addFeature", featureClass);

            Object firewallCapability = newInstance(loader,
                BASE + "licensing.NexusFirewallCapabilityFeature");
            Object professionalPlus = firewallCapability == null ? null
                : newInstance(loader, BASE + "licensing.NexusProfessionalPlusFeature",
                    proFeature, firewallCapability);
            Object enterprise = professionalPlus == null ? null
                : newInstance(loader, BASE + "licensing.NexusEnterpriseFeature",
                    professionalPlus, firewallCapability);

            if (firewallCapability != null) {
                addFeature.invoke(featureSet, firewallCapability);
            }
            if (professionalPlus != null) {
                addFeature.invoke(featureSet, professionalPlus);
            }
            if (enterprise != null) {
                addFeature.invoke(featureSet, enterprise);
                add(featureSet, addFeature, loader,
                    BASE + "blobstore.pro.s3.ProS3GlobalDistributionFeature", enterprise);
            }

            for (String name : PRO_FEATURES) {
                add(featureSet, addFeature, loader, name, proFeature);
            }
            for (String name : STANDALONE_FEATURES) {
                add(featureSet, addFeature, loader, name);
            }

            System.out.println("[NexusLicenseAgent] Fake license feature set populated");
        } catch (Throwable t) {
            System.err.println("[NexusLicenseAgent] Could not populate full feature set: " + t);
            t.printStackTrace();
        }
    }

    private static void add(Object featureSet, Method addFeature, ClassLoader loader,
                            String featureClassName, Object... constructorArgs) {
        try {
            Object feature = newInstance(loader, featureClassName, constructorArgs);
            if (feature != null) {
                addFeature.invoke(featureSet, feature);
            }
        } catch (Throwable t) {
            System.err.println("[NexusLicenseAgent] Skipping feature " + featureClassName + ": " + t);
        }
    }

    private static Object newInstance(ClassLoader loader, String featureClassName,
                                      Object... constructorArgs) throws Exception {
        Class<?> featureClass = Class.forName(featureClassName, true, loader);
        for (Constructor<?> constructor : featureClass.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != constructorArgs.length) {
                continue;
            }
            boolean matches = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (constructorArgs[i] != null
                    && !parameterTypes[i].isAssignableFrom(constructorArgs[i].getClass())) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return constructor.newInstance(constructorArgs);
            }
        }
        System.err.println("[NexusLicenseAgent] No matching constructor for " + featureClassName);
        return null;
    }
}
