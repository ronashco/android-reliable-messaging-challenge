package david.hosseini.androidreliablemessaginglibrary

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SendDataWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {
            val url = inputData.getString(URL) ?: return Result.failure()

            @Suppress("UNCHECKED_CAST")
            val dataMap = (inputData.keyValueMap as Map<String, String>).toMutableMap()
            dataMap.remove(URL)

            if (dataMap.isEmpty()) return Result.failure()//Nothing to send to server.

            val response = HttpManager.service
                .postMessage(url, dataMap)
                .execute()

            if (response.code() != 200) {
                return Result.retry()
            }

            return Result.success()

        } catch (e: Exception) {
            // Retry if any exception occurred
            return Result.retry()
        }
    }


}
