package com.nicolas.whfrp3companion.shared.adapters

import com.nicolas.models.action.Action

interface ActionListener {
    fun launchAction(action: Action)
}
