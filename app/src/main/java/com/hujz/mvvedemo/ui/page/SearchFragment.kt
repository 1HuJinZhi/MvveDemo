package com.hujz.mvvedemo.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.hujz.mvvedemo.R
import com.hujz.mvvedemo.bridge.state.SearchViewModel
import com.hujz.mvvedemo.databinding.FragmentSearchBinding
import com.hujz.mvvedemo.ui.base.BaseFragment

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/25
 *     desc   :
 * </pre>
 */
class SearchFragment : BaseFragment() {

    private lateinit var mBinding: FragmentSearchBinding
    private lateinit var mSearchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mBinding = FragmentSearchBinding.bind(view).apply {
            click = ClickProxy()
            vm = mSearchViewModel
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    inner class ClickProxy {

        fun back() {
            nav().navigateUp()
        }

        fun testNav() {
            val u = "https://xiaozhuanlan.com/topic/5860149732"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        fun subscribe() {
            val u = "https://xiaozhuanlan.com/kunminx"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}