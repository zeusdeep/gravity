package fi.zalando.core.ui.presenter;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;

import fi.zalando.core.BuildConfig;
import fi.zalando.core.domain.helper.SubscriptionHelper;
import fi.zalando.core.module.BaseHelperModule;
import fi.zalando.core.ui.view.BaseView;
import icepick.State;
import rx.Observable;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BasePresenter} class
 *
 * Created by jduran on 29/11/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BasePresenterTest {

    private MockBasePresenter basePresenter;

    @Mock
    private BaseView baseView;

    @Before
    public void setUp() {

        basePresenter = new MockBasePresenter(new BaseHelperModule().provideSubscriptionHelper());
        baseView = mock(BaseView.class);
    }

    @Test
    public void testHasSubscriptionManager() {

        assertNotNull(basePresenter.getSubscriptionHelper());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetViewNotCalledBeforeResumeException() {

        basePresenter.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetViewNotCalledBeforeInitialiseException() {

        basePresenter.initialise(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitialisedNotCalledBeforeResumeException() {

        basePresenter.resume();
    }

    @Test
    public void testUnsubscribeOnDestroy() {

        // Set mocked view
        basePresenter.setView(baseView);
        // Init presenter
        basePresenter.initialise(null);
        basePresenter.resume();

        // Add Subscription
        basePresenter.getSubscriptionHelper().addSubscription(Observable.never().subscribe());

        // Check that the subscription exists
        assertTrue(basePresenter.getSubscriptionHelper().hasSubscriptions());

        // Pause it
        basePresenter.destroy();

        // Check if unsubscribed
        assertFalse(basePresenter.getSubscriptionHelper().hasSubscriptions());
    }

    @SuppressWarnings("all")
    @Test
    public void testSaveState() {

        Integer mockedIntValue = 2;
        Boolean mockedBooleanValue = true;
        Float mockedFloatValue = 1.1f;
        Serializable mockedSerializable = new String("SerializableExample");

        // Set mocked view
        basePresenter.setView(baseView);
        // Init presenter
        basePresenter.initialise(null);
        basePresenter.resume();
        // Change mocked values
        basePresenter.testSavingInt = mockedIntValue;
        basePresenter.testSavingBoolean = mockedBooleanValue;
        basePresenter.testSavingFloat = mockedFloatValue;
        basePresenter.testSavingSerializable = mockedSerializable;
        // Force save instance state
        // Create bundle to save the state
        Bundle bundle = new Bundle();
        // Verify Bundle is empty
        assertTrue(bundle.isEmpty());
        // Force saving the state
        basePresenter.onSaveInstanceState(bundle);
        // Verify bundle is not empty
        assertFalse(bundle.isEmpty());
        // Destroy the saved variables in the MockBasePresenter and init it again with the bundle
        basePresenter = new MockBasePresenter(new BaseHelperModule().provideSubscriptionHelper());
        basePresenter.setView(baseView);
        basePresenter.initialise(bundle);
        basePresenter.resume();
        // Check that it was properly restored
        assertEquals(basePresenter.testSavingInt, mockedIntValue);
        assertEquals(basePresenter.testSavingBoolean, mockedBooleanValue);
        assertEquals(basePresenter.testSavingFloat, mockedFloatValue);
        assertEquals(basePresenter.testSavingSerializable, mockedSerializable);
    }

    protected class MockBasePresenter extends BasePresenter<BaseView> {

        @State Integer testSavingInt;
        @State Boolean testSavingBoolean;
        @State Float testSavingFloat;
        @State Serializable testSavingSerializable;

        /**
         * Constructor
         */
        public MockBasePresenter(SubscriptionHelper subscriptionHelper) {
            super(subscriptionHelper);
        }

    }
}