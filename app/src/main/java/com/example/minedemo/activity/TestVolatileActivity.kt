package com.example.minedemo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minedemo.R

class TestVolatileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_volatile)
        startTest()
    }


    @Volatile
    private var a = 0
    @Volatile
    private var b = 1
    @Volatile
    private var c = 2
    @Volatile
    private var d = 3
    @Volatile
    private var e = 4

    private fun startTest() {
        println("start a:$a b:$b c:$c d:$d e:$e")

        val testA = Thread {
            a++
            b++
            c++
            d++
            e++
            println("testA a:$a b:$b c:$c d:$d e:$e")
        }
        testA.name = "testA"


        val testB = Thread {
            a++
            b++
            c++
            d++
            e++
            println("testB a:$a b:$b c:$c d:$d e:$e")
        }
        testB.name = "testB"

        val testC = Thread {
            a++
            b++
            c++
            d++
            e++
            println("testC a:$a b:$b c:$c d:$d e:$e")
        }
        testC.name = "testC"


        val testD = Thread {
            a++
            b++
            c++
            d++
            e++
            println("testD a:$a b:$b c:$c d:$d e:$e")
        }
        testD.name = "testD"


        val testE = Thread {
            a++
            b++
            c++
            d++
            e++
            println("testE a:$a b:$b c:$c d:$d e:$e")
        }
        testE.name = "testE"

        testA.start()
        testB.start()
        testC.start()
        testD.start()
        testE.start()

        Thread.sleep(2000)
        println("end a:$a b:$b c:$c d:$d e:$e")
    }
}