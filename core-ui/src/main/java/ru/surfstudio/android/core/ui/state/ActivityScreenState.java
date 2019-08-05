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
package ru.surfstudio.android.core.ui.state;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;

/**
 * Предоставляет текущее состояние экрана и живую активити - контейнер
 */
public class ActivityScreenState extends BaseScreenState {
    private FragmentActivity activity;
    private CoreActivityInterface coreActivity;

    public void onCreate(
            FragmentActivity activity,
            CoreActivityInterface coreActivity,
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        this.activity = activity;
        this.coreActivity = coreActivity;
    }

    public void onDestroy() {
        super.onDestroy();
        activity = null;
        coreActivity = null;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public CoreActivityInterface getCoreActivity() {
        return coreActivity;
    }
}
