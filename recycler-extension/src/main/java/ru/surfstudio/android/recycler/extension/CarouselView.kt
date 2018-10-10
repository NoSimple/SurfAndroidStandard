/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.recycler.extension

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.easyadapter.carousel.R

/**
 * Вью-карусель элементов
 */
class CarouselView<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : RecyclerView(context, attributeSet) {

    var centerItemChangedListener: (position: Int) -> Unit = {}

    private var realItemsCount = 0
    private val easyAdapter: EasyAdapter = EasyAdapter()
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    var centerItemPosition: Int = 0
        set(value) {
            if (value != field) { //нас интересуют только отличающиеся от старого значения
                centerItemChangedListener.invoke(value)
            }
            field = value
        }

    var isInfinite = false
        set(value) {
            field = value
            easyAdapter.setInfiniteScroll(value)
            if (value) {
                applyInfiniteScroll()
            }
        }

    init {
        initAttrs(context, attributeSet)
        easyAdapter.setFirstInvisibleItemEnabled(false)

        this.layoutManager = linearLayoutManager
        this.adapter = easyAdapter

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateCenterPosition()
            }
        })
    }

    fun render(elements: ItemList) {
        easyAdapter.setItems(elements)

        // при первоначальной установке элементов и бесконечном скроле необходимо проскроллить до середины
        // фейкового количества элементов, в остальных случаях этого не требуется
        if (isInfinite && (realItemsCount == 0)) {
            realItemsCount = elements.size
            applyInfiniteScroll()
        } else {
            realItemsCount = elements.size
        }
    }

    fun render(elements: List<T>, itemController: BindableItemController<T, out BindableViewHolder<T>>) {
        render(ItemList.create()
                .addAll(elements, itemController))
    }

    fun setSnapHelper(snapHelper: SnapHelper) {
        snapHelper.attachToRecyclerView(this)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CarouselView, 0, 0)
        try {
            isInfinite = ta.getBoolean(R.styleable.CarouselView_isInfinite, false) //должна ли быть карусель зацикленной
        } finally {
            ta.recycle()
        }
    }

    private fun updateCenterPosition() {
        if (realItemsCount != 0) {
            val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
            this.centerItemPosition = (lastVisiblePosition + firstVisiblePosition) / 2 % realItemsCount
        }
    }

    private fun applyInfiniteScroll() {
        val startPosition = EasyAdapter.INFINITE_SCROLL_LOOPS_COUNT / 2 * realItemsCount
        this.linearLayoutManager.scrollToPosition(startPosition)
    }
}