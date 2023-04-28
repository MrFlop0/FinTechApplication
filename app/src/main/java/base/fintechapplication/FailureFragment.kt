package base.fintechapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class FailureFragment : Fragment() {

    lateinit var btnRetry: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_failure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnRetry = view.findViewById(R.id.RetryBtn)
        btnRetry.setOnClickListener {
            val intent = Intent(Constants.intentRetry)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }


}