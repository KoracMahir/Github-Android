package com.mahirkorac.githubandroid.features.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mahirkorac.githubandroid.RxImmediateSchedulerRule
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.repository.SearchRepository
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var dataRepository: SearchRepository

    lateinit var model: SearchViewModel

    @Before
    fun setUp() {
        model = SearchViewModel(dataRepository)

    }

    @Test
    fun `test value of sort and order in view model after clearing search`() {
        //before
        model.updateFilter(SortType.STARTS, OrderType.DESC)

        //when
        val resultSort = model.sortValue.value
        val resultOrder = model.sortValue.value

        //then
        Assert.assertEquals("stars", resultSort)
        Assert.assertEquals("desc", resultOrder)
    }

    @Test
    fun `test value of sort in view model after clearing search`() {
        //before
        model.updateFilter(SortType.STARTS, OrderType.DESC)
        model.clearSearch()

        //when
        val query = model.queryValue.value
        val repositories = model.repositories.value

        //then
        Assert.assertEquals(null, query)
        Assert.assertEquals(null, repositories?.data)
    }

    @Test
    fun `test value of query on update function`() {
        //before
        val expected = "test query"
        model.updateQuery("test query")

        //when
        val query = model.queryValue.value

        //then
        Assert.assertEquals(expected, query)
    }

}