package org.sonatype.licensing.internal;

import codeguard.licensing.aec;
import codeguard.licensing.whz;
import com.sonatype.nexus.bootstrap.entrypoint.licensing.NexusProfessionalFeature;
import de.schlichtherle.license.LicenseContentException;
import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicenseManager;
import org.sonatype.licensing.LicenseParam;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;

/* JADX WARN: Classes with same name are omitted:
  sonatype-nexus-repository-3.90.1-01.jar:BOOT-INF/lib/license-bundle-2.0.2.jar:org/sonatype/licensing/internal/DefaultLicenseManager.class
 */
@Named("licensing.default")
@Deprecated
/* loaded from: sonatype-nexus-repository-3.90.1-01.jar:BOOT-INF/lib/nexus-licensing-extension-3.90.1-01.jar:org/sonatype/licensing/internal/DefaultLicenseManager.class */
public class DefaultLicenseManager implements LicenseManager {
    private final Provider<LicenseKey> ooh;
    private final FeatureValidator tlx;
    private final LicenseValidator uzp;

    public DefaultLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator) {
        this(provider, featureValidator, new aec());
    }

    @Inject
    public DefaultLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator, @Nullable LicenseValidator licenseValidator) {
        this.ooh = provider;
        this.tlx = featureValidator;
        this.uzp = licenseValidator;
    }

    @Override // org.sonatype.licensing.LicenseManager
    public LicenseKey createLicense(LicenseParam licenseParam, LicenseKeyRequest licenseKeyRequest) throws LicensingException {
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

    @Override // org.sonatype.licensing.LicenseManager
    public LicenseKey installLicense(LicenseParam licenseParam, File file) throws LicensingException {
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

    @Override // org.sonatype.licensing.LicenseManager
    public void uninstallLicense(LicenseParam licenseParam) throws LicensingException {
        //
    }

    @Override // org.sonatype.licensing.LicenseManager
    public LicenseKey verifyLicense(LicenseParam licenseParam) throws LicensingException {
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

    @Override // org.sonatype.licensing.LicenseManager
    public LicenseKey verifyLicense(LicenseParam licenseParam, File file) throws LicensingException {
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

    @Override // org.sonatype.licensing.LicenseManager
    public void validateFeature(LicenseKey licenseKey, Feature feature) throws LicensingException {
        this.tlx.validate(feature, licenseKey);
    }

    private String itm(Exception exc) {
        return exc.getLocalizedMessage() != null ? exc.getLocalizedMessage() : exc.getMessage();
    }
}
