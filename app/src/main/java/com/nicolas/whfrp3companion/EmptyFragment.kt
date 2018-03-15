package com.nicolas.whfrp3companion

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EmptyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.empty_fragment, container, false)
    }

    companion object {
        fun newInstance(): EmptyFragment {
            val args = Bundle()
            val fragment = EmptyFragment()
            fragment.arguments = args

            return fragment
        }
    }
}