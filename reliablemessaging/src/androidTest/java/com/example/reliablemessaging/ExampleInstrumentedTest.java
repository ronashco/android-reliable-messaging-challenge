package com.example.reliablemessaging;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.reliablemessaging.service.MessageJobService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private JobScheduler jobScheduler;
    private Context context;

    @Before
    public void setUP()
    {
       context = InstrumentationRegistry.getTargetContext();
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }
    @Test
    public void schedule_success() {
        // Context of the app under test.

        PersistableBundle bundle = new PersistableBundle();

        bundle.putString(BundleKey.URL,"https://challenge.ronash.co/reliable-messaging");
        bundle.putString(BundleKey.DATA, "hi");
        JobInfo jobInfo =
                new JobInfo.Builder(99, new ComponentName(context,  MessageJobService.class))
                        .setPeriodic(1000)
                        .setExtras(bundle)
                        .build();

        int result = jobScheduler.schedule(jobInfo);

        assertEquals(result,JobScheduler.RESULT_SUCCESS);

    }
}
