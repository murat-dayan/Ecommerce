package com.muratdayan.ecommerce.presentation.dialog

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muratdayan.ecommerce.R

fun Fragment.setUpBottomSheetDialog(
    onSendClick: (String) -> Unit,
){

    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.etResetPassword)
    val buttonSend = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnSendResetPasswordDialog)
    val buttonCancel = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnCancelResetPasswordDialog)

    buttonSend.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }



}