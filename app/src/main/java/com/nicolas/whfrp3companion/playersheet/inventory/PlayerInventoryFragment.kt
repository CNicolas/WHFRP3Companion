package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ITEM_EDIT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item
import com.nicolas.whfrp3database.extensions.removeItem
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread

class PlayerInventoryFragment : Fragment() {
    @BindView(R.id.inventory)
    lateinit var inventoryView: ExpandableListView

    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade
    private lateinit var playerName: String
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_inventory, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerFacade = PlayerFacade(context!!)

        getPlayerItems()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        getPlayerItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.add_item)
    fun openAddItemActivity() {
        startItemEditionActivity()
    }

    private fun startItemEditionActivity(item: Item? = null) {
        if (activity != null) {
            startActivity(activity!!.intentFor<ItemEditionActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name,
                    ITEM_EDIT_INTENT_ARGUMENT to item
            ))
        }
    }

    private fun getPlayerItems() {
        doAsync {
            player = playerFacade.find(playerName)!!

            val inventoryAdapter = PlayerInventoryExpandableAdapter(context!!, player)
            inventoryAdapter.addItemListener(object : ItemListener {
                override fun onItemDeleted(item: Item) {
                    player.removeItem(item)
                    playerFacade.update(player)

                    getPlayerItems()
                }

                override fun onItemEditionDemand(item: Item) {
                    startItemEditionActivity(item)
                }
            })

            uiThread {
                inventoryView.setAdapter(inventoryAdapter)
            }
        }
    }

    companion object {
        fun newInstance(playerName: String): PlayerInventoryFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerInventoryFragment()
            fragment.arguments = args

            return fragment
        }
    }
}