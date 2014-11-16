package it.rainbowbreeze.picama.data.provider;

import android.test.ProviderTestCase2;

import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.provider.PictureProvider;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class PictureProviderTest extends ProviderTestCase2<PictureProvider> {
    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public PictureProviderTest(Class<AmazingPictureDao> providerClass, String providerAuthority) {
        super(PictureProvider.class, PictureProvider.AUTHORITY);
    }
}
