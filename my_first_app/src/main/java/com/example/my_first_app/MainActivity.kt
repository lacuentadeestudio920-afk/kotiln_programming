package com.example.my_first_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 코드로 직접 구성 (XML 필요 없음)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 80)
        }

        val title = TextView(this).apply {
            text = "BMI 계산기"
            textSize = 24f
        }

        val heightInput = EditText(this).apply {
            hint = "키(cm)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val weightInput = EditText(this).apply {
            hint = "몸무게(kg)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val button = Button(this).apply {
            text = "계산하기"
        }

        val result = TextView(this).apply {
            textSize = 18f
            setPadding(0, 30, 0, 0)
        }

        layout.addView(title)
        layout.addView(heightInput)
        layout.addView(weightInput)
        layout.addView(button)
        layout.addView(result)

        setContentView(layout)

        // 버튼 클릭 이벤트
        button.setOnClickListener {
            val hText = heightInput.text.toString()
            val wText = weightInput.text.toString()

            if (hText.isBlank() || wText.isBlank()) {
                result.text = "키와 몸무게를 모두 입력하세요."
                return@setOnClickListener
            }

            val height = hText.toDoubleOrNull()
            val weight = wText.toDoubleOrNull()

            if (height == null || weight == null || height <= 0 || weight <= 0) {
                result.text = "올바른 숫자를 입력하세요."
                return@setOnClickListener
            }

            val bmi = weight / (height / 100).pow(2)
            val rounded = round(bmi * 10) / 10

            val category = when {
                bmi < 18.5 -> "저체중"
                bmi < 25 -> "정상"
                bmi < 30 -> "과체중"
                else -> "비만"
            }

            result.text = "당신의 BMI는 $rounded 입니다.\n($category)"
        }
    }
}
