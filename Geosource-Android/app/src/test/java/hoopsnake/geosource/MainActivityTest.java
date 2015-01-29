package hoopsnake.geosource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import org.mockito.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {


    private final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
    MainActivity activity;


    @Before
    public void setup()
    {
        activity = controller.create().start().resume().get();
    }

    @Test
    public void test()
    {
        assertTrue(true);
    }

    @Test
    public void testCoverage() {
        activity.coverage(1);
        activity.coverage(2);
        activity.coverage(3);
        activity.coverage(4);
    }

    @Test
    public void testValue() {
        BasicObject object = Mockito.mock(BasicObject.class);
        Mockito.when(object.value()).thenReturn(2);
        assertEquals(object.value(), 2);
    }

}
