package base.fintechapplication

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnFavorite: AppCompatButton
    private lateinit var btnPopular: AppCompatButton
    private val pf = PopularFragment()
    private val ff = FavoriteFragment()
    private val failFragment = FailureFragment()
    var service: NetworkService? = null
    var downloaded = false
    private var isBound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnFavorite = findViewById(R.id.FavoriteBtn)
        btnPopular = findViewById(R.id.PopularBtn)

        setNewFragment(pf, Constants.tag_Popular)

        btnPopular.setOnClickListener {
            setNewFragment(pf, Constants.tag_Popular)
        }
        btnFavorite.setOnClickListener {
            setNewFragment(ff, Constants.tag_Favorite)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            failureFragment, IntentFilter(Constants.intentFailure)
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(
            retryAttempt, IntentFilter(Constants.intentRetry)
        )
        val intent = Intent(this, NetworkService::class.java)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    private fun setNewFragment(fr: Fragment, tag: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.Frame, fr, tag)
        ft.commit()
    }

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val binderBridge = binder as NetworkService.MyBinder
            service = binderBridge.getMyService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            service = null
        }

    }

    private val failureFragment: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            setNewFragment(failFragment, Constants.tag_Fail)
        }
    }

    private val retryAttempt: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (!downloaded) {
                service?.receiveMovies()
            }
            setNewFragment(pf, Constants.tag_Popular)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(boundServiceConnection)
        }
        val intent = Intent(this, NetworkService::class.java)
        stopService(intent)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(failureFragment)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(retryAttempt)
    }
}
