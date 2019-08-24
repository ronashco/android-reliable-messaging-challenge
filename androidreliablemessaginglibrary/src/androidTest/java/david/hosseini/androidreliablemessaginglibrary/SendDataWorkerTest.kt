package david.hosseini.androidreliablemessaginglibrary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class SendDataWorkerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var wmRule = WorkManagerTestRule()


    @Test
    fun testFailsIfNoUrl() {

        val inputData = Data.Builder()
            .putAll(mapOf("anyKey" to "anyValue"))// InputData must not be empty
            .build()

        val request = OneTimeWorkRequestBuilder<SendDataWorker>()
            .setInputData(inputData)
            .build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request)

        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        assertThat(workInfo.state, `is`(WorkInfo.State.FAILED))
    }

    @Test
    fun testFailsIfNoData() {


        val inputData = Data.Builder()
            .putString(URL, SERVER_URL)
            .build()

        val request = OneTimeWorkRequestBuilder<SendDataWorker>()
            .setInputData(inputData)
            .build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request)

        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        assertThat(workInfo.state, `is`(WorkInfo.State.FAILED))
    }

    @Test
    @Throws(Exception::class)
    fun testRetryWhenServerRespondsError() {

        val server = enqueueMockResponse(500)

        val inputData = Data.Builder()
            .putString(URL, server.url("/").toString())
            .putAll(mapOf("anyKey" to "anyValue"))
            .build()

        val request = OneTimeWorkRequestBuilder<SendDataWorker>()
            .setInputData(inputData)
            .build()


        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request)
        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        server.shutdown()

        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }

    @Test
    @Throws(Exception::class)
    fun testSendsDataWhenServerRespondsSuccessful() {

        val server = enqueueMockResponse(200)

        val inputData = Data.Builder()
            .putString(URL, server.url("/").toString())
            .putAll(mapOf("anyKey" to "anyValue"))
            .build()

        val request = OneTimeWorkRequestBuilder<SendDataWorker>()
            .setInputData(inputData)
            .build()


        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request)
        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        server.shutdown()

        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
    }


    private fun enqueueMockResponse(responseCode: Int): MockWebServer {
        // Create a MockWebServer.
        val server = MockWebServer()
        server.start()

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(responseCode)

        server.enqueue(mockedResponse)
        return server
    }
}
