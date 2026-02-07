package GUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.JProgressBar;

/**
 * Test class for SplashScreen.
 * Verifies progress bar loading.
 * 
 * @author Disath Damsutha
 */
public class SplashScreenTest {

    private SplashScreen instance;

    public SplashScreenTest() {
    }

    @Before
    public void setUp() {
        instance = new SplashScreen();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * Test Case: Progress Bar Loads to 100%
     */
    @Test
    public void testProgressBarLoading() throws Exception {
        JProgressBar progressBar = (JProgressBar) getPrivateField("ProgressBar");

        // Wait for the thread in load() to finish
        // The thread runs 100 times with sleep(10), so total ~1 second
        Thread.sleep(2000);

        int progress = progressBar.getValue();
        assertEquals("Progress bar should reach 100%", 100, progress);
    }

    /**
     * Helper method to access private fields.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = SplashScreen.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
