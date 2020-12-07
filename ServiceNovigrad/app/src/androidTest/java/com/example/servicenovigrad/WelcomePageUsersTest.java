package com.example.servicenovigrad;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.servicenovigrad.data.Class.NovService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WelcomePageUsersTest {

    @Rule
    public ActivityTestRule<WelcomePageUsers> activityTestRule = new ActivityTestRule<WelcomePageUsers>(WelcomePageUsers.class);

    private WelcomePageUsers welcomePageUsers = null;
    @Before
    public void setUp() throws Exception {
        welcomePageUsers = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        welcomePageUsers = null;
    }

    @Test
    public void onCreate() {
        View view = welcomePageUsers.findViewById(R.id.servicesList);
        assertNotNull(view);
        assertNull(view);
        
    }

    @Test
    public void onStart() {
        List<NovService> list = new ArrayList<>();
        assertEquals(welcomePageUsers.serviceList.size(),list.size());
        assertNotEquals(welcomePageUsers.serviceList.size(),list.size());

    }
}