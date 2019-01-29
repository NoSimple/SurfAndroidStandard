/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.pagination

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.stub.generateStubs
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData

class PaginationPresentationModel : ScreenModel() {

    val reloadAction = Action<Unit>()
    val getMoreAction = Action<Unit>()
    val selectElementAction = Action<Element>()

    val elementsState = State<DataList<SelectableData<Element>>>(DataList.empty())
    val stubsState = State(generateStubs(20))
    val loadState = State(LS.NONE)
    val paginationState = State(PaginationState.READY)

    var hasData = false
}

enum class LS {
    LOADING,
    ERROR,
    EMPTY,
    NONE
}
