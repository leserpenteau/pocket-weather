package com.katerina.weather.core.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Do you want to enable location?")
        dialog.setMessage("Location is disabled now, do you want enable?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun searchCityWeatherDialog(context: Context, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val editText = EditText(context)

        builder.setView(editText)

        val dialog = builder.create()
        dialog.setTitle("Enter the city name:")

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Search!"){ _, _ ->
            listener.onClick(editText.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"){ _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String?)
    }
}