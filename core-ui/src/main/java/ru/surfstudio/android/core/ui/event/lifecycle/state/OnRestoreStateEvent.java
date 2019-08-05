/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.event.lifecycle.state;


import android.os.Bundle;

import androidx.annotation.Nullable;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;

/**
 * событие восстановления состояния
 */
public class OnRestoreStateEvent implements ScreenEvent {
    @Nullable
    private Bundle savedInstanceState;

    public OnRestoreStateEvent(@Nullable Bundle outState) {
        this.savedInstanceState = outState;
    }

    @Nullable
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

}
