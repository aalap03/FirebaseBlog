package com.example.aalap.blogs.TabFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aalap.blogs.Models.Hit
import com.example.aalap.blogs.Models.MainHitsObject
import com.example.aalap.blogs.Models.ResponsePostItem
import com.example.aalap.blogs.R
import com.example.aalap.blogs.RetrofitUtils.RetrofitCreator
import com.example.aalap.blogs.RetrofitUtils.RetrofitElasticSearch
import com.example.aalap.blogs.Utilities.GridAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class FragmentSearch : Fragment(), AnkoLogger {

    var postItems = mutableListOf<ResponsePostItem>()
    lateinit var adapter: GridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(context, 4)
        recycler_view.layoutManager = gridLayoutManager

        adapter = GridAdapter(requireContext(), postItems)
        recycler_view.adapter = adapter
        val retrofit = RetrofitCreator().retrofit().create(RetrofitElasticSearch::class.java)

        info { "getting list" }
        retrofit.getMainHits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> handleResponse(t) }, { error -> handleError(error) })

    }

    private fun handleError(error: Throwable?) {
        info { "fuck its error" }
        info { error?.localizedMessage }
    }

    private fun handleResponse(response: Response<MainHitsObject>) {
        info { "got response" }
        if(response.isSuccessful) {
            val body = response.body()
            val hits = body?.hits
            val post = hits?.post
            info { post }
            postItems.clear()
            for (item in post!!) {
                postItems.add(item.postItem)
            }

            if(adapter != null)
                adapter.notifyDataSetChanged()

        }else {
           info { response.errorBody()?.string()}
        }
    }
}