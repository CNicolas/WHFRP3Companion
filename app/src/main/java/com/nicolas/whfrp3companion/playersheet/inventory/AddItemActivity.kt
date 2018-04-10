package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.GenericItem
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.extensions.addItem
import com.nicolas.whfrp3database.extensions.moveToItemType
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddItemActivity : AppCompatActivity() {
    private val armorLayout by bind<View>(R.id.armor_layout)
    private val expandableLayout by bind<View>(R.id.expandable_layout)
    private val weaponLayout by bind<View>(R.id.weapon_layout)

    private val itemNameView by bind<EditText>(R.id.item_name)
    private val itemTypeView by bind<Spinner>(R.id.item_type)
    private val qualityView by bind<Spinner>(R.id.quality)

    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade

    private lateinit var player: Player
    private var item: Item = GenericItem("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        unbinder = ButterKnife.bind(this)

        val playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerFacade = PlayerFacade(this)
        doAsync {
            player = playerFacade.find(playerName)!!
        }

        itemTypeView.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, values().map { getString(it.labelId) })
        itemTypeView.setSelection(GENERIC_ITEM.ordinal)

        qualityView.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Quality.values().map { getString(it.labelId) })
        qualityView.setSelection(GENERIC_ITEM.ordinal)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    @OnTextChanged(R.id.item_name,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun modifyItem(editable: Editable) {
        item.name = editable.trim().toString()
    }

    @OnItemSelected(R.id.item_type)
    fun selectItemType(spinner: Spinner, position: Int) {
        item = item.moveToItemType(ItemType[position])

        when (item.type) {
            ARMOR -> {
                armorLayout.visibility = View.VISIBLE
                expandableLayout.visibility = View.GONE
                weaponLayout.visibility = View.GONE
            }
            EXPANDABLE -> {
                armorLayout.visibility = View.GONE
                expandableLayout.visibility = View.VISIBLE
                weaponLayout.visibility = View.GONE
            }
            GENERIC_ITEM -> {
                armorLayout.visibility = View.GONE
                expandableLayout.visibility = View.GONE
                weaponLayout.visibility = View.GONE
            }
            WEAPON -> {
                armorLayout.visibility = View.GONE
                expandableLayout.visibility = View.GONE
                weaponLayout.visibility = View.VISIBLE
            }
        }
    }

    @OnClick(R.id.save_item)
    fun save() {
        doAsync {
            player.addItem(item)
            player = playerFacade.update(player)

            uiThread {
                finish()
            }
        }
    }
}