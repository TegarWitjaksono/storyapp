package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.content.res.AppCompatResources
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.material.textfield.TextInputLayout

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        gravity = android.view.Gravity.CENTER_VERTICAL
        background = AppCompatResources.getDrawable(context, R.drawable.rounded_button)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = parent.parent
                if (parent is TextInputLayout) {
                    if (s.toString().length < 8) {
                        parent.error = "Password tidak boleh kurang dari 8 karakter"
                        background = AppCompatResources.getDrawable(context, R.drawable.rounded_button_red) // Set red border
                    } else {
                        parent.error = null
                        background = AppCompatResources.getDrawable(context, R.drawable.rounded_button) // Reset to normal border
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })
    }
}