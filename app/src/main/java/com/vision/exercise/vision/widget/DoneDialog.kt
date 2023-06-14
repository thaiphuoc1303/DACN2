package com.vision.exercise.vision.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.vision.exercise.R
import com.vision.exercise.databinding.DialogAlertBinding
import com.vision.exercise.databinding.DialogConfirmBinding
import com.vision.exercise.databinding.DialogDoneBinding

class DoneDialog(
    private val title: String,
    private val message: String,
    private val yesString: String,
    private val onClickYesListener: () -> Unit
): DialogFragment() {

    companion object {
        const val TAG = "Alert_DIALOG"

        fun newInstance(
            title: String,
            message: String,
            agreeString: String,
            onClickYesListener: () -> Unit
        ): DoneDialog {
            return DoneDialog(
                title,
                message,
                agreeString,
                onClickYesListener
            )
        }
    }

    private val binding by lazy { DialogDoneBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setStyle(
            STYLE_NORMAL,
            R.style.Theme_Project_FullScreen
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
        }
        binding.apply {
            tvMessage.text = message
            btnYes.text = yesString
        }
    }

    private fun initListeners() {
        binding.apply {
            btnYes.setOnClickListener {
                onClickYesListener.invoke()
                dismiss()
            }
        }
    }

    fun closeDialog(){
        this.dismiss()
    }
}

