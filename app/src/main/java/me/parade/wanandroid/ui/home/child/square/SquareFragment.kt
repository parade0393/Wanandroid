package me.parade.wanandroid.ui.home.child.square

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.data.updateSingleData
import me.parade.lib_base.base.BaseFragment
import me.parade.wanandroid.databinding.FragmentSquareBinding
import me.parade.wanandroid.net.model.ArticleInfo
import me.parade.wanandroid.ui.home.child.explore.DslHomeArticleItem


class SquareFragment : BaseFragment<FragmentSquareBinding,SquareVM>() {

    private val dslAdapter: DslAdapter by lazy { DslAdapter() }
    private var loadPage = 0
    private val pageSize = 10

    companion object{
        fun newInstance(): SquareFragment = with(SquareFragment()){
            arguments = bundleOf()
            this
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        dslAdapter.setLoadMoreEnable(true)
        dslAdapter.dslLoadMoreItem.onLoadMore = {
            loadPage++
            viewModel.getArticleList(loadPage)
        }
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dslAdapter
        }
    }

    override fun initData() {
        viewModel.getArticleList(loadPage,pageSize)
    }

    override fun initObserver() {
        viewModel.articleList.observe(viewLifecycleOwner){
            dslAdapter.updateSingleData<DslHomeArticleItem>(it.datas,loadPage,pageSize){ data->
                data?.let { item->
                    (item as? ArticleInfo)?.let { info ->
                        articleInfo = info
                    }
                }
            }
        }
    }

}