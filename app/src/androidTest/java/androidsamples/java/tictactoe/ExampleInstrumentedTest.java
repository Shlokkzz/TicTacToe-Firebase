package androidsamples.java.tictactoe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;

import androidx.test.espresso.accessibility.AccessibilityChecks;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.io.IOException;

import android.content.pm.PackageManager;
import android.os.Build;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import android.content.pm.PackageManager;
import androidx.test.platform.app.InstrumentationRegistry;



import java.util.Objects;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("androidsamples.java.tictactoe", appContext.getPackageName());
    }

    @Test
    public void testLoginDetails(){
        try{
            // do not login
            Espresso.onView(ViewMatchers.withId(R.id.fab_new_game)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException e){
            onView(withId(R.id.edit_email)).perform(clearText(), typeText("shlok@gmail.com"));
            onView(withId(R.id.edit_email)).check(matches(withText("shlok@gmail.com")));
        }
    }
    @Test
    public void testNavigationToDashboard() throws InterruptedException {

        try{
            // do not login
            Espresso.onView(ViewMatchers.withId(R.id.fab_new_game)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException e){
            onView(withId(R.id.edit_email)).perform(clearText(), typeText("shlok@gmail.com"));
            onView(withId(R.id.edit_password)).perform(clearText(), typeText("shlokpatel"));
            onView(withId(R.id.btn_log_in)).perform(click());

            // Wait for 5 seconds
            Thread.sleep(5000);
        }

        onView(withId(R.id.fab_new_game)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToSinglePlayer() throws InterruptedException {

        try{
            // do not login
            Espresso.onView(ViewMatchers.withId(R.id.fab_new_game)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException e){
            onView(withId(R.id.edit_email)).perform(clearText(), typeText("shlok@gmail.com"));
            onView(withId(R.id.edit_password)).perform(clearText(), typeText("shlokpatel"));
            onView(withId(R.id.btn_log_in)).perform(click());

            // Wait for 5 seconds
            Thread.sleep(5000);
        }
        onView(withId(R.id.fab_new_game)).perform(click());
        onView(withText("One-Player")).perform(click());

        onView(withId(R.id.button0)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToDoublePlayer() throws InterruptedException {

        try{
            // do not login
            Espresso.onView(ViewMatchers.withId(R.id.fab_new_game)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException e){
            onView(withId(R.id.edit_email)).perform(clearText(), typeText("shlok@gmail.com"));
            onView(withId(R.id.edit_password)).perform(clearText(), typeText("shlokpatel"));
            onView(withId(R.id.btn_log_in)).perform(click());

            // Wait for 5 seconds
            Thread.sleep(5000);
        }
        onView(withId(R.id.fab_new_game)).perform(click());
        onView(withText("Two-Player")).perform(click());

        onView(withId(R.id.button0)).check(matches(isDisplayed()));
    }

    @Test
    public void testAreButtonsClickableAfter1Move() throws InterruptedException {

        try{
            // do not login
            Espresso.onView(ViewMatchers.withId(R.id.fab_new_game)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException e){
            onView(withId(R.id.edit_email)).perform(clearText(), typeText("shlok@gmail.com"));
            onView(withId(R.id.edit_password)).perform(clearText(), typeText("shlokpatel"));
            onView(withId(R.id.btn_log_in)).perform(click());

            // Wait for 5 seconds
            Thread.sleep(5000);
        }
        onView(withId(R.id.fab_new_game)).perform(click());
        onView(withText("Two-Player")).perform(click());

        onView(withId(R.id.button0)).check(matches(isDisplayed()));

        onView(withId(R.id.display_tv)).check(matches(withText("Your Turn")));
        onView(withId(R.id.button0)).perform(click());
        onView(withId(R.id.display_tv)).check(matches(withText("Waiting...")));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.display_tv)).check(matches(withText("Waiting...")));
    }

}