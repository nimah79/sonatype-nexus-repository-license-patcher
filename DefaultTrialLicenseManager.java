package org.sonatype.licensing.trial.internal;

import codeguard.licensing.aec;
import codeguard.licensing.chr;
import codeguard.licensing.xlf;
import com.sonatype.nexus.bootstrap.entrypoint.licensing.NexusProfessionalFeature;
import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseContentException;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;
import org.sonatype.licensing.trial.TrialLicenseManager;
import org.sonatype.licensing.trial.TrialLicenseParam;

/* JADX WARN: Classes with same name are omitted:
  sonatype-nexus-repository-3.90.1-01.jar:BOOT-INF/lib/license-bundle-2.0.2.jar:org/sonatype/licensing/trial/internal/DefaultTrialLicenseManager.class
 */
@Named("licensing.default")
/* loaded from: sonatype-nexus-repository-3.90.1-01.jar:BOOT-INF/lib/nexus-licensing-extension-3.90.1-01.jar:org/sonatype/licensing/trial/internal/DefaultTrialLicenseManager.class */
public class DefaultTrialLicenseManager implements TrialLicenseManager {
    private final Logger fvf;
    private final Provider<LicenseKey> ooh;
    private final FeatureValidator tlx;
    private final LicenseValidator uzp;

    public DefaultTrialLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator) {
        this(provider, featureValidator, new aec());
    }

    @Inject
    public DefaultTrialLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator, @Nullable LicenseValidator licenseValidator) {
        this.fvf = LoggerFactory.getLogger(getClass());
        this.ooh = provider;
        this.tlx = featureValidator;
        this.uzp = licenseValidator;
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public LicenseKey createLicense(TrialLicenseParam trialLicenseParam, LicenseKeyRequest licenseKeyRequest) throws LicensingException {
        LicenseKey licenseKey = this.ooh.get();

        licenseKey.setContactName("Juniora");
        licenseKey.setContactCompany("https://juniora.org");
        licenseKey.setContactEmailAddress("info@juniora.org");
        licenseKey.setContactTelephone("11011001110");
        licenseKey.setContactCountry("Iran");
        licenseKey.setEvaluation(false);
        licenseKey.setExpirationDate(new Date(2038 - 1900, 1, 1));
        licenseKey.setEffectiveDate(new Date(2015 - 1900, 1, 1));
        FeatureSet fs = new FeatureSet();
        fs.addFeature(new NexusProfessionalFeature());
        licenseKey.setFeatureSet(fs);

        return licenseKey;
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public LicenseKey installLicense(TrialLicenseParam trialLicenseParam, File file) throws LicensingException {
        LicenseKey licenseKey = this.ooh.get();

        licenseKey.setContactName("Juniora");
        licenseKey.setContactCompany("https://juniora.org");
        licenseKey.setContactEmailAddress("info@juniora.org");
        licenseKey.setContactTelephone("11011001110");
        licenseKey.setContactCountry("Iran");
        licenseKey.setEvaluation(false);
        licenseKey.setExpirationDate(new Date(2038 - 1900, 1, 1));
        licenseKey.setEffectiveDate(new Date(2015 - 1900, 1, 1));
        FeatureSet fs = new FeatureSet();
        fs.addFeature(new NexusProfessionalFeature());
        licenseKey.setFeatureSet(fs);

        return licenseKey;
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public void uninstallLicense(TrialLicenseParam trialLicenseParam) throws LicensingException {
        //
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public LicenseKey verifyLicense(TrialLicenseParam trialLicenseParam) throws LicensingException {
        LicenseKey licenseKey = this.ooh.get();

        licenseKey.setContactName("Juniora");
        licenseKey.setContactCompany("https://juniora.org");
        licenseKey.setContactEmailAddress("info@juniora.org");
        licenseKey.setContactTelephone("11011001110");
        licenseKey.setContactCountry("Iran");
        licenseKey.setEvaluation(false);
        licenseKey.setExpirationDate(new Date(2038 - 1900, 1, 1));
        licenseKey.setEffectiveDate(new Date(2015 - 1900, 1, 1));
        FeatureSet fs = new FeatureSet();
        fs.addFeature(new NexusProfessionalFeature());
        licenseKey.setFeatureSet(fs);

        return licenseKey;
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public LicenseKey verifyLicense(TrialLicenseParam trialLicenseParam, File file) throws LicensingException {
        LicenseKey licenseKey = this.ooh.get();

        licenseKey.setContactName("Juniora");
        licenseKey.setContactCompany("https://juniora.org");
        licenseKey.setContactEmailAddress("info@juniora.org");
        licenseKey.setContactTelephone("11011001110");
        licenseKey.setContactCountry("Iran");
        licenseKey.setEvaluation(false);
        licenseKey.setExpirationDate(new Date(2038 - 1900, 1, 1));
        licenseKey.setEffectiveDate(new Date(2015 - 1900, 1, 1));
        FeatureSet fs = new FeatureSet();
        fs.addFeature(new NexusProfessionalFeature());
        licenseKey.setFeatureSet(fs);

        return licenseKey;
    }

    @Override // org.sonatype.licensing.trial.TrialLicenseManager
    public void validateFeature(LicenseKey licenseKey, Feature feature) throws LicensingException {
        this.tlx.validate(feature, licenseKey);
    }

    private String itm(Exception exc) {
        String localizedMessage = exc.getLocalizedMessage() != null ? exc.getLocalizedMessage() : exc.getMessage();
        return localizedMessage != null ? localizedMessage : exc.getClass().getSimpleName();
    }

    private LicensingException itm(LicenseKey licenseKey, LicenseContentException licenseContentException) {
        licenseKey.populateFromLicenseContent((CustomLicenseContent) licenseContentException.getLicenseContent());
        return new LicensingException(licenseKey, itm(licenseContentException), licenseContentException);
    }
}
