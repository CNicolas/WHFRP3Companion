package com.nicolas.whfrp3companion.shared.adapters

import android.view.View
import com.nicolas.models.action.Action

interface ActionListener {
    fun primaryHandler(action: Action) {}
    fun longPrimaryHandler(view: View, action: Action): Boolean = false

    fun secondaryHandler(action: Action) {}
}
