package com.nicolas.whfrp3companion.shared.adapters

import com.nicolas.models.action.Action

interface ActionListener {
    fun primaryHandler(action: Action) {}
    fun longPrimaryHandler(action: Action): Boolean {
        return false
    }
    fun secondaryHandler(action: Action) {}
}
