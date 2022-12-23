package com.example.gameapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_oyun_ekrani.*
import java.lang.Math.floor
import java.util.*
import kotlin.concurrent.schedule

class OyunEkraniActivity : AppCompatActivity() {

    private var anakarakterX = 0.0f
    private var anakarakterY = 0.0f
    private var dokunmaKontrol = true
    private var baslangicKontrol = false
    private val timer = Timer()

    private var skor = 0

    private var kirmiziX = 0.0f
    private var kirmiziY = 0.0f

    private var maviX = 0.0f
    private var maviY = 0.0f

    private var turuncuX = 0.0f
    private var turuncuY = 0.0f

    private var anakarakterYuksekligi = 0
    private var anakarakterGenisligi = 0
    private var ekranGenisligi = 0
    private var ekranYuksekligi = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oyun_ekrani)

        kirmizi.x = -800.0f
        kirmizi.y= -800.0f
        mavi.x= -800.0f
        mavi.y= -800.0f
        turuncu.x = -800.0f
        turuncu.y = -800.0f

       cl.setOnTouchListener(object : View.OnTouchListener{
           override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

               if (baslangicKontrol){ //baslangickontrol true ise bu if çalışır.

                   if(p1?.action == MotionEvent.ACTION_DOWN){
                       Log.e("MotionEvent", "Action_Down : Ekrana dokundu")
                   }
                   if(p1?.action == MotionEvent.ACTION_UP){
                       Log.e("MotionEvent", "Action_Up : Ekran bırakıldı")
                   }

               }else{
                   /*baslangickontrol değişkeni false olarak tanımlanmıştır. program çalıştığında değişken false olacağı için önce bu else bloğu çalışacak ve
                   çalıştığı anda değişkenin değeri true olacağı için program bu bloğa bir daha girmeyecek. Çünkü if bloğu içerisinde değişkenin değeri false olarak
                   değişmiyor. True olduğu için ve true olduğu müddetçe çalıştığı için timer birden fazla kez oluşmayacak. */

                   baslangicKontrol = true
                   textViewOyunaBasla.visibility = View.INVISIBLE
                   //Karakterlerin anlık x-y değerlerini aldık
                   anakarakterX =anakarakter.x
                   anakarakterY = anakarakter.y

                   anakarakterGenisligi = anakarakter.width
                   anakarakterYuksekligi = anakarakter.height
                   ekranGenisligi = cl.width
                   ekranYuksekligi = cl.height

                   timer.schedule(0,20){
                       Handler(Looper.getMainLooper()).post{

                           anakarakterHareket()
                           cisimlerHareket()
                           carpismaKontrol()

                       }
                   }
               }


               return true
           }

       })
    }
    fun anakarakterHareket(){

        val anakarakterHiz = ekranYuksekligi/60.0f

        if (dokunmaKontrol){

            anakarakterY -=anakarakterHiz

        }else{
            anakarakterY+=anakarakterHiz
        }
        if (anakarakterY<=0){
            anakarakterY = 0.0f
        }
        if (anakarakterY>=ekranYuksekligi - anakarakterYuksekligi){
            anakarakterY = (ekranYuksekligi - anakarakterYuksekligi).toFloat()
        }
        anakarakter.y=anakarakterY

    }

    fun cisimlerHareket(){
        kirmiziX-=ekranGenisligi/40.0f
        turuncuX-=ekranGenisligi/54.0f
        maviX-=ekranGenisligi/36.0f

        if(kirmiziX<0.0f){

            kirmiziX = ekranGenisligi+20.0f
            kirmiziY = floor(Math.random()*ekranYuksekligi).toFloat()

        }
        kirmizi.x=kirmiziX
        kirmizi.y=ekranYuksekligi/2.0f

        if(turuncuX<0.0f){

            turuncuX = ekranGenisligi+20.0f
            turuncuY = floor(Math.random()*ekranYuksekligi).toFloat()

        }
        turuncu.x=turuncuX
        turuncu.y=ekranYuksekligi/2.0f

        if(maviX<0.0f){

            maviX = ekranGenisligi+20.0f
            maviY = floor(Math.random()*ekranYuksekligi).toFloat()

        }
        mavi.x=maviX
        mavi.y=ekranYuksekligi/2.0f

    }
    fun carpismaKontrol(){
        val maviMerkezX = maviX+mavi.width/2.0f
        val maviMerkezY = maviY+mavi.height/2.0f

        if(0.0f<=maviMerkezX && maviMerkezX<=anakarakterGenisligi && anakarakterY<=maviMerkezY
            && maviMerkezY<= anakarakterY+anakarakterYuksekligi){
            skor+=20
            maviX= -10.0f

        }

        val turuncuMerkezX = turuncuX+turuncu.width/2.0f
        val turuncuMerkezY = turuncuY+turuncu.height/2.0f

        if(0.0f<=turuncuMerkezX && turuncuMerkezX<=anakarakterGenisligi && anakarakterY<=turuncuMerkezY
            && turuncuMerkezY<= anakarakterY+anakarakterYuksekligi){
            skor+=50
            turuncuX= -10.0f

        }

        val kirmiziMerkezX = kirmiziX+kirmizi.width/2.0f
        val kirmiziMerkezY = kirmiziY+kirmizi.height/2.0f

        if(0.0f<=kirmiziMerkezX && kirmiziMerkezX<=anakarakterGenisligi && anakarakterY<=kirmiziMerkezY
            && kirmiziMerkezY<= anakarakterY+anakarakterYuksekligi){
            kirmiziX= -10.0f
            timer.cancel()
            val intent = Intent(this@OyunEkraniActivity, SonucEkrani::class.java)
            intent.putExtra("skor",skor)
            startActivity(intent)
            finish()

        }
        textViewSkor.text = skor.toString()

    }
}