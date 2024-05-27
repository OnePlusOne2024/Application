package com.example.oneplusone.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.paging.map
import com.example.oneplusone.R
import com.example.oneplusone.activity.SearchActivity
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.databinding.ProductDetailViewerBinding
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.`interface`.ProductFavoriteClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.LoadingBarAdapter
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MainFilterViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mainFilterViewModel: MainFilterViewModel by viewModels()
    //메인엑티비티와 데이터를 공유해야함activityViewModels로 메인엑티비티의 db뷰모델과 공유
    private val dbViewModel:DataBaseViewModel by activityViewModels()
    private var productNameList= arrayListOf<String>()


    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter



    private val productSpacingController = ItemSpacingController(25, 25, 40)

    private val filterSpacingController = ItemSpacingController(15, 15, 0)

    private var selectMainFilter:View?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAdapter()

        setupDataBinding()

        observeSetting()


        moveSearchActivity()

    }


    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
        initLoadingAdapter()
    }

    private fun moveSearchActivity() {

        val searchActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        }

        binding.searchIcon.setOnClickListener {
            val searchActivityIntent = Intent(activity, SearchActivity::class.java)
            searchActivityIntent.putStringArrayListExtra("productNameList",productNameList)
            searchActivityResult.launch(searchActivityIntent)
        }
    }


    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@HomeFragment.mainFilterViewModel
            filterDataViewModel = this@HomeFragment.filterDataViewModel
            productDataViewModel = this@HomeFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
        observeDataBaseViewModel()
    }



    private fun initMainFilterAdapter() {
        mainFilterAdapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
            override fun onMainFilterClick(mainFilter: MainFilterData,itemView: View) {

                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)

                selectMainFilter = itemView
                filterDataViewModel.showFilter(mainFilter.filterType)

            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter

    }

    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {

                mainFilterViewModel.updateMainFilter(filterData)

                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }

        })
        binding.filterViewer.adapter = productFilterAdapter
    }

    private fun initProductItemRecyclerAdapter() {
        productItemRecyclerAdapter = ProductItemRecyclerAdapter(
            object : ProductClickListener {
            override fun onItemClick(productData: ProductData) {
                productDataViewModel.loadClickProductData(productData)
            }
        }, object : ProductFavoriteClickListener {
            override fun onFavoriteClick(productData: ProductData) {
                productDataViewModel.toggleFavorite(productData)
            }
        })
//        binding.productGridView.adapter = productItemRecyclerAdapter.withLoadStateFooter(footer=LoadingBarAdapter())
        binding.productGridView.adapter = productItemRecyclerAdapter

        binding.productGridView.addItemDecoration(productSpacingController)


//        productItemRecyclerAdapter.addLoadStateListener { loadState ->
//            val isListEmpty =
//                loadState.refresh is LoadState.NotLoading && productItemRecyclerAdapter.itemCount == 0
//            val isLoading = loadState.source.refresh is LoadState.Loading ||
//                    loadState.source.append is LoadState.Loading
//            if (isLoading) {
//                dbViewModel.toggleLoadingBar(true)
//                binding.productGridView.isEnabled=false
//
//            } else {
//                // 로딩이 완료되었거나 에러가 발생했을 때
//                dbViewModel.toggleLoadingBar(false)
//                binding.productGridView.isEnabled=true
//
//                // 에러 상태 처리
//                val errorState = loadState.source.append as? LoadState.Error
//                    ?: loadState.source.prepend as? LoadState.Error
//                    ?: loadState.append as? LoadState.Error
//                    ?: loadState.prepend as? LoadState.Error
//                errorState?.let {
//                    // 에러 처리 로직
//                }
//            }
//        }
    }

    private fun initLoadingAdapter() {

    }

    private fun observeMainFilterViewModel() {
        mainFilterViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { mainFilterData ->

            mainFilterAdapter.submitList(mainFilterData)
            productDataViewModel.setCurrentMainFilterData(mainFilterData)
            //메인필터의 값이 바뀔때마다 아이템들을 새로 불러옴
            dbViewModel.loadFavoriteProducts()
            dbViewModel.loadProductDataList()

            //메인필터의 값을 설정한 후에 db에서 값을 가져옴
//            dbViewModel.loadProductAndFavoriteProduct()
//            dbViewModel.loadProductDataList()
//            dbViewModel.loadFavoriteProducts()
        })

    }

    private fun observeFilterDataViewModel() {
        //여기서 변화를 감지하고 변경함
        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { data ->
            productFilterAdapter.submitList(data)

        })

        filterDataViewModel.filterBar.observe(viewLifecycleOwner, Observer { isVisible  ->
            //사라질 때 시각적으로 버벅 거림이 느껴져서 애니메이션으로 부드럽게 바꿨음
            FilterAnimated().viewAnimated(isVisible,binding.filterBarDetail)

        })

        filterDataViewModel.selectFilterColorSwitch.observe(viewLifecycleOwner, Observer { switchState ->

            if(switchState){
                FilterStyle().updateFilterStyle(requireContext(),selectMainFilter)
            }
            else{
                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)
            }
        })
    }

    //todo 즐겨찾기 페이지 만들기
    private fun observeProductDataViewModel() {
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { data ->

//            lifecycleScope.launch {

//                data.collectLatest { pagingData ->
//
//                    val transformedData = pagingData
//                        .map { productData ->
//                            productDataViewModel.loadDBProductData(productData)
//                            productData
//                        }
//                    productItemRecyclerAdapter.submitData(lifecycle, transformedData)
//                }
//            }
        })

        productDataViewModel.clickProductData.observe(viewLifecycleOwner, Observer { clickProductData ->
            showProductDetailDialog(clickProductData)
        })
        productDataViewModel.filterProductData.observe(viewLifecycleOwner, Observer { filterProductData ->
//            productItemRecyclerAdapter.submitList(filterProductData)

        })

        productDataViewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            dbViewModel.favoriteProductJudgment(isFavorite)
        })
        productDataViewModel.productNameList.observe(viewLifecycleOwner, Observer { productNameList ->
            this.productNameList=productNameList
        })

//        productDataViewModel._mergeData.observe(viewLifecycleOwner, Observer { (favoriteProducts, dbProducts) ->
//            if (favoriteProducts != null && dbProducts != null) {
//                productDataViewModel.loadProductData()
//            }
//        })
        productDataViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { mainFilterDataList ->
//            productDataViewModel.loadFilteredProductData(mainFilterDataList)
        })

//        productDataViewModel.serverProductDataList.observe(viewLifecycleOwner, Observer { serverProductDataList ->
//            //서버에서 가져온 데이터를 넣는 코드 일단 일시정지
//            if (serverProductDataList != null) {
//                dbViewModel.insertServerProductDataList(serverProductDataList)
//            }
//        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showProductDetailDialog(productData: ProductData) {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.product_detail_viewer, null)
        val dialogBinding = ProductDetailViewerBinding.bind(mDialogView)

        //어쩔 수 없이 notifyItemChanged로 업데이트 하기로 결정
        Log.d("ProductData", productData.id.toString())
        dialogBinding.productData = productData


        dialogBinding.favorite.setOnClickListener{
            productDataViewModel.toggleFavorite(productData)



            if (productData.favorite) {
                dialogBinding.favorite.setImageResource(R.drawable.favorite_on)
            } else {
                dialogBinding.favorite.setImageResource(R.drawable.favorite_off)
            }

        }
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)


        val dialog = mBuilder.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.setOnDismissListener {
            productItemRecyclerAdapter.notifyDataSetChanged()
//            productItemRecyclerAdapter.notifyItemChanged(productData.id!!.toInt())
        }
    }
    private fun observeDataBaseViewModel() {

        dbViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            Log.d("isLoading", isLoading.toString())
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        })

        dbViewModel.favoriteProducts.observe(viewLifecycleOwner, Observer { favoriteProductData ->
            productDataViewModel.loadFavoriteProduct(favoriteProductData)
        })

        dbViewModel.DBProductDataList.observe(viewLifecycleOwner, Observer { dbProductDataList ->
            lifecycleScope.launch {

                dbProductDataList.collectLatest { pagingData ->

                    val transformedData = pagingData
                        .map { productData ->

                            productDataViewModel.isProductFavorite(productData)
                        }.filter{
                            productDataViewModel.loadFilteredProductData(it)
                        }

                    productItemRecyclerAdapter.submitData(lifecycle, transformedData)

                }

            }
        })
        dbViewModel.serverConnectProcessState.observe(viewLifecycleOwner, Observer { serverConnectProcessState ->
            Log.d("serverConnectProcessState", serverConnectProcessState.toString())
            //최초로 db에 데이터 삽입 과정이 끝나면 아이템을 불러옴
            if(serverConnectProcessState){
                dbViewModel.loadFavoriteProducts()
                dbViewModel.loadProductDataList()
            }
        })

    }

}
