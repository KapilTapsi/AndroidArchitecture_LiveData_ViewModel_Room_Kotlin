package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.Utils

import android.content.Context

fun Context.logd(message:String){
    android.util.Log.d("${javaClass.name} kapil"," $message")
}