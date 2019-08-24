package david.hosseini.android_reliable_messaging_challenge

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import david.hosseini.androidreliablemessaginglibrary.post
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun send(view: View) {

        val text = edit_text.text.toString()
        post(URL, mapOf(TEXT to text))
                .observe(this, Observer<WorkInfo> {

                    if (it.state == WorkInfo.State.SUCCEEDED) {

                        Toast.makeText(
                                this, "Request successfully finished",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                })

    }
}

const val URL = "https://challenge.ronash.co/reliable-messaging/"
const val TEXT = "text"