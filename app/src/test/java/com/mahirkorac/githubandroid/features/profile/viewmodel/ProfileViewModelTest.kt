package com.mahirkorac.githubandroid.features.profile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mahirkorac.githubandroid.RxImmediateSchedulerRule
import com.mahirkorac.githubandroid.features.profile.repository.ProfileRepository
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var dataRepository: ProfileRepository

    lateinit var model: ProfileViewModel

    @Before
    fun setUp() {
        model = ProfileViewModel(dataRepository)

    }

    @Test
    fun `test date formatting from API to readable string`() {
        //before
        val date = "2023-02-10T08:01:36Z"

        //when
        val formattedDate = model.getFormattedDate(date)

        //then
        Assert.assertEquals("2023-Feb-10", formattedDate)
    }

}