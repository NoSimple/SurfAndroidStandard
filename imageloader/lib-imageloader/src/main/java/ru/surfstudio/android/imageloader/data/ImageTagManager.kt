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
package ru.surfstudio.android.imageloader.data

import android.view.View
import ru.surfstudio.android.imageloader.R

/**
 * Класс, отвечающий за актуализацию тэгов при загрузке изображения
 */
data class ImageTagManager(
        private var imageTargetManager: ImageTargetManager,
        private var imageResourceManager: ImageResourceManager,
        private val signatureManager: SignatureManager
) {

    var force: Boolean = false // Принудительная вставка изображения

    /**
     * Проверка тэга на то, был ли он ранее уже использован
     */
    fun isTagUsed() =
            getTag() != null && imageResourceManager.url == getTag() && !force && !signatureManager.hasSignature

    /**
     * Установка тэга на целевую [View]
     */
    fun setTag(url: String?) {
        imageTargetManager.targetView?.setTag(R.id.image_loader_tag, url)
    }

    /**
     * Извлечение тэга
     */
    private fun getTag() = imageTargetManager.targetView?.getTag(R.id.image_loader_tag)
}