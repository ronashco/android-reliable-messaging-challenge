package david.hosseini.androidreliablemessaginglibrary

import androidx.lifecycle.LiveData
import androidx.work.*

/**
 * This method works with WorkManager API because WorkManager guarantees to do the work.
 */
@Suppress("DEPRECATION")
fun post(url: String, data: Map<String, String>): LiveData<WorkInfo> {

    // We need internet connection for this
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val inputData = Data.Builder()
        .putString(URL, url)
        .putAll(data)
        .build()


    val sendDataWorkRequest = OneTimeWorkRequestBuilder<SendDataWorker>()
        .setInputData(inputData)
        .setConstraints(constraints)
        //I commented out setting Back off criteria because by default it retries in EXPONENTIAL manner with 30 seconds
        // backoff delay, respectively.
        /*.setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )*/
        .build()

    WorkManager.getInstance().enqueue(sendDataWorkRequest)

    return WorkManager.getInstance().getWorkInfoByIdLiveData(sendDataWorkRequest.id)
}


const val URL = "url"
