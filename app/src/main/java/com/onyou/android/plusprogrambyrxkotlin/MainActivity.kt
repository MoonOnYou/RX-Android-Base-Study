package com.onyou.android.plusprogrambyrxkotlin

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstMethod()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun firstMethod() {
        setupObserver(editTextFirstNumber)
        setupObserver(editTextSecondNumber)                                                         // observer 두개, subscribe 두개 라고 보면되겠지...?
    }

    private fun setupObserver(editText: EditText) {
        val observer = Observable.create<CharSequence>{ emitter: ObservableEmitter<CharSequence> ->
            editText.addTextChangedListener( object  : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let { emitter.onNext(it) }                                                   // onNext() 타면 subscribe 실행
                }
            })
        }

        compositeDisposable.add(observer
            .filter{!TextUtils.isEmpty(it)}
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnError { Toast.makeText(this, "다시 해보세용!", Toast.LENGTH_SHORT).show()}           // 딱히 여기서는 안써도 되는듯... 알아놓으면 좋음
            .subscribe{
                val firstText = editTextFirstNumber.text.toString()
                val secondText = editTextSecondNumber.text.toString()
                val firstInt : Long = if (firstText != "") firstText.toLong() else 0                // if 문 처럼 체크안해주니까 아무거도 없는값 ("") 일때 앱 죽음..
                val secondInt : Long = if (secondText != "") secondText.toLong() else 0             // Long 말고 Int 형 쓰니까 10자리 이상 입력했을때 앱죽음 .. 우리형..
                val temp: Long = firstInt + secondInt
                textViewAnswer.text = "$temp"
            })
    }
}
