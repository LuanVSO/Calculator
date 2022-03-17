package com.luanvso.calculator

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.mariuszgromada.math.mxparser.*

class MainActivity : AppCompatActivity() {

    private lateinit var display: EditText

    private val btnUpDspIdArr = arrayOf(
        R.id.b0,
        R.id.b1,
        R.id.b2,
        R.id.b3,
        R.id.b4,
        R.id.b5,
        R.id.b6,
        R.id.b7,
        R.id.b8,
        R.id.b9,
        R.id.bDecimal,
        R.id.bDivision,
        R.id.bExponent,
        R.id.bMultiplication,
        R.id.bSubtraction,
        R.id.bSummation,
        R.id.bClear
    )

    private fun setEventListeners() {
        for (bt in btnUpDspIdArr) {
            findViewById<View>(bt)
                .setOnClickListener(btUpdatesDisplayOnClick)
        }
        findViewById<View>(R.id.bErase)
            .setOnClickListener {
                var begin = display.selectionStart
                val end = display.selectionEnd
                if (begin == end && begin > 0) {
                    --begin
                }
                display.text.delete(begin, end)
            }
        findViewById<View>(R.id.bParentheses)
            .setOnClickListener {
                val pos = display.selectionStart
                var nOpenPar = 0
                var nClosePar = 0
                for (i in 0 until pos){
                    if(display.text[i]=='(') ++nOpenPar
                    if(display.text[i]==')') ++nClosePar
                }
                if(nOpenPar==nClosePar ||display.text.endsWith('(',true) ) updateDisplay("(")
                else if(nOpenPar > nClosePar && !display.text.endsWith('(',true) ) updateDisplay(")")

            }
        findViewById<View>(R.id.bEquals)
            .setOnClickListener {
                val str:String = display.text.toString()
                    .replace(Regex(
                    "[${getString(R.string.decimalSeparator)}${getString(R.string.multiplication)}${getString(R.string.division)}]")
                    ) { matchResult: MatchResult ->
                        when (matchResult.value) {
                            getString(R.string.decimalSeparator) -> "."
                            getString(R.string.multiplication) -> "*"
                            getString(R.string.division) -> "/"
                            else -> ""
                        }
                    }
                val exp = Expression(str)
                display.setText(exp.calculate().toString().replace(".",getString(R.string.decimalSeparator)))
                display.setSelection(display.length())
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEventListeners()

        display = findViewById(R.id.input)
        display.showSoftInputOnFocus = false
        display.requestFocus()
    }

    private fun updateDisplay(str: String) {
        val pos = display.selectionStart
        display.text.insert(pos, str)
    }

    private val btUpdatesDisplayOnClick = View.OnClickListener { view ->
        when (view.id) {
            R.id.b0 -> updateDisplay("0")
            R.id.b1 -> updateDisplay("1")
            R.id.b2 -> updateDisplay("2")
            R.id.b3 -> updateDisplay("3")
            R.id.b4 -> updateDisplay("4")
            R.id.b5 -> updateDisplay("5")
            R.id.b6 -> updateDisplay("6")
            R.id.b7 -> updateDisplay("7")
            R.id.b8 -> updateDisplay("8")
            R.id.b9 -> updateDisplay("9")
            R.id.bDecimal -> updateDisplay(getString(R.string.decimalSeparator))
            R.id.bDivision -> updateDisplay(getString(R.string.division))
            R.id.bExponent -> updateDisplay("^")
            R.id.bMultiplication -> updateDisplay(getString(R.string.multiplication))
            R.id.bSubtraction -> updateDisplay("-")
            R.id.bSummation -> updateDisplay(getString(R.string.summation))
            R.id.bClear -> display.text.clear()
        }
    }
}
