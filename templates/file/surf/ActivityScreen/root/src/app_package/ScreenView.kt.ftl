<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName}

<#if generateToolbar>
import android.support.v7.widget.Toolbar;
</#if>

class ${className}${screenTypeCapitalized}View : <@superClass.selectTypeView /> {

    @Inject
    lateinit var presenter: ${className}Presenter

    <#if generateToolbar>
    private lateinit var toolbar: Toolbar
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    private lateinit var placeHolderView: PlaceHolderViewImpl
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    </#if>

    <#if generateRecyclerView>
        private lateinit var recyclerView: RecyclerView
        <#if (screenType=='activity' && usePaginationableAdapter) || (screenType=='fragment' && usePaginationableAdapter)>
            private var adapter = PaginationableAdapter<${nameTypeData}>()
        <#else>
            private var adapter = EasyAdapter()
        </#if>
        private var ${nameController?uncap_first}${defPostfixController} = ${nameController}${defPostfixController}(<#if hasListener>presenter::on${nameTypeData}ItemClick</#if>)
    </#if>

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    <#if screenType=='activity'>
        override fun createConfigurator(): ActivityScreenConfigurator = ${className}ScreenConfigurator(intent)
        @LayoutRes
        override fun getContentView(): Int = R.layout.${layoutName}
    <#else>
        override fun createConfigurator(): FragmentScreenConfigurator = ${className}ScreenConfigurator(arguments)
        override fun onCreateView(inflater: LayoutInflater, 
                                  container: ViewGroup?, 
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.${layoutName}, container, false)
        }
    </#if>

    <#if screenType=='activity'>
    override fun onCreate(savedInstanceState: Bundle?,
                         persistentState: PersistableBundle?,
                         viewRecreated: Boolean) {
        findViews(window.decorView)
    <#else>
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
         findViews(view!!)
   </#if>
        <#if generateToolbar>
        initToolbar()
        </#if>
        initListeners()
        <#if generateRecyclerView>
        initRecyclerView()
        </#if>
    }

    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        <#if (screenType=='activity' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='2')>
            override fun getPlaceHolderView(): PlaceHolderView = placeHolderView

            <#if (screenType=='activity' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='3')>
                override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipeRefreshLayout

                <#if (screenType='activity' && typeViewActivity!='4') || (screenType=='fragment' && typeViewFragment!='4')>
                    override fun getPaginationableAdapter(): BasePaginationableAdapter = adapter
                </#if>
            </#if>
        </#if>

        override fun renderInternal(screenModel: ${className}ScreenModel) {
            <#if generateRecyclerView>
                <#if (screenType=='activity' && usePaginationableAdapter) || (screenType=='fragment' && usePaginationableAdapter)>
                    adapter.setItems(ItemList.create()
                        .addAll(screenModel.itemList, ${nameController?uncap_first}${defPostfixController}), screenModel.paginationState)
                    <#else>
                    adapter.setItems(ItemList.create()
                        .addAll(screenModel.itemList, ${nameController?uncap_first}${defPostfixController}))
                </#if>
            </#if>
        }
    </#if>

    private fun findViews(view: View) {
        <#if generateToolbar>
        toolbar = view.findViewById(R.id.toolbar);
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        placeHolderView = view.findViewById(R.id.placeholder)
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        </#if>
        <#if generateRecyclerView>
        recyclerView = view.findViewById(R.id.recycler)
        </#if>
    }

    <#if generateToolbar>
    private fun initToolbar() {
        toolbar.title = null // todo поправить тайтл тулбара
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { _ -> presenter.finish() }
    }
    </#if>

    private fun initListeners() {
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
            placeHolderView.setOnActionClickListener { presenter.reloadData() }
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
            swipeRefreshLayout.setOnRefreshListener { presenter.reloadData() }
        </#if>
    }

    <#if generateRecyclerView>
    private fun initRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(<#if screenType=='activity'>this<#else>context</#if>)
    }
    </#if>

     override fun getName(): String = "${camelCaseToUnderscore(className)}"
}