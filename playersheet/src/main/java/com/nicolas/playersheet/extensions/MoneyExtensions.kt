package com.nicolas.playersheet.extensions

import com.nicolas.whfrp3database.entities.player.Player

// region Add
fun Player.addMoney(goldAmount: Int, silverAmount: Int, brassAmount: Int) {
    addBrass(brassAmount)
    addSilver(silverAmount)
    addGold(goldAmount)
}

fun Player.addGold(goldAmount: Int) {
    gold += goldAmount
}

fun Player.addSilver(silverAmount: Int) {
    if (silverAmount == 0) {
        return
    }

    val silverRest = silverAmount % SILVER_TO_GOLD
    val goldAmount = (silverAmount - silverRest) / SILVER_TO_GOLD

    if (goldAmount > 0) {
        addGold(goldAmount)
        addSilver(silverRest)
    } else {
        silver += silverRest
    }
}

fun Player.addBrass(brassAmount: Int) {
    if (brassAmount == 0) {
        return
    }

    val brassRest = brassAmount % BRASS_TO_SILVER
    val silverAmount = (brassAmount - brassRest) / BRASS_TO_SILVER

    if (silverAmount > 0) {
        addSilver(silverAmount)
        addBrass(brassRest)
    } else {
        brass += brassRest
    }
}
// endregion

// region Remove
fun Player.removeMoney(goldAmount: Int, silverAmount: Int, brassAmount: Int) {
    removeBrass(brassAmount)
    removeSilver(silverAmount)
    removeGold(goldAmount)
}

fun Player.removeGold(goldAmount: Int) {
    gold = when {
        gold > goldAmount -> gold - goldAmount
        else -> throw  IllegalArgumentException("Not enough money")
    }
}

fun Player.removeSilver(silverAmount: Int) {
    if (silverAmount == 0) {
        return
    }

    if (silverAmount > silver) {
        val goldToWithdraw = (silverAmount - silver) / SILVER_TO_GOLD + 1

        removeGold(goldToWithdraw)
        silver += goldToWithdraw * SILVER_TO_GOLD
    }

    silver -= silverAmount
}

fun Player.removeBrass(brassAmount: Int) {
    if (brassAmount == 0) {
        return
    }

    if (brassAmount > brass) {
        val silverToWithdraw = (brassAmount - brass) / BRASS_TO_SILVER + 1

        removeSilver(silverToWithdraw)
        brass += silverToWithdraw * BRASS_TO_SILVER
    }

    brass -= brassAmount
}
// endregion

const val SILVER_TO_GOLD: Int = 100
const val BRASS_TO_SILVER: Int = 25
