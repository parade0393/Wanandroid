package me.parade.wanandroid.ui.profile

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.DslItemDecoration
import com.angcyo.dsladapter.data.Page
import com.angcyo.dsladapter.data.loadDataEnd
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_common.ext.actionBarHeight
import me.parade.lib_common.ext.px
import me.parade.lib_common.ext.statusBarHeight
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.FragmentProfileBinding
import me.parade.wanandroid.ui.home.child.explore.DslHomeArticleItem


class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileVm>() {

    private val dslAdapter: DslAdapter by lazy { DslAdapter() }
    private var loadPage = 1
    private val perPageSize = 10

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.inflateMenu(R.menu.mine_toolbar_menu)

        val statusBarHeight = requireActivity().statusBarHeight()
        binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = statusBarHeight
        }
        binding.cslInfo.updatePadding(top = statusBarHeight+requireContext().actionBarHeight())

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dslAdapter
            addItemDecoration(DslItemDecoration())
        }
        dslAdapter.dslLoadMoreItem.onLoadMore = {
            loadPage++
            viewModel.getArticleList(loadPage,perPageSize)
        }

        binding.main.apply {
            autoRefresh()
            setEnableRefresh(true)
            setEnableLoadMore(false)
            setOnRefreshListener {
                fresh()
            }
        }
    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(true)
    }

    override fun initData() {

    }

    override fun initObserver() {
        viewModel.articleList.observe(viewLifecycleOwner){bean->
            binding.main.finishRefresh()
            dslAdapter.loadDataEnd(DslHomeArticleItem::class,bean.datas,null, Page().apply {
                firstPageIndex = 1
                requestPageSize = perPageSize
                requestPageIndex = loadPage
            },true){
                articleInfo = it
                itemBottomInsert = 10.px
                itemLeftInsert = 10.px
                itemRightInsert = 10.px
            }
        }
    }

    private fun fresh(){
        viewModel.getArticleList(loadPage,perPageSize)
    }

}