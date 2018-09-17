package ru.surfstudio.android.security.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    override fun renderInternal(screenModel: MainScreenModel) { }

    override fun getScreenName(): String = "main"

    private fun initListeners() {
        check_root_btn.setOnClickListener { presenter.checkRoot() }
        sign_in_btn.setOnClickListener { presenter.createPin(api_key_et.text.toString()) }
    }

    fun showMessage(message: String) {
        toast(message)
    }
}
