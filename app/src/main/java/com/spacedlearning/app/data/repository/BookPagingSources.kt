package com.spacedlearning.app.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spacedlearning.app.data.remote.api.BookApi
import com.spacedlearning.app.data.remote.dto.book.BookSummaryResponseDto
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import javax.inject.Inject

class BookPagingSource @Inject constructor(
    private val bookApi: BookApi
) : PagingSource<Int, BookSummaryResponseDto>() {

    override fun getRefreshKey(state: PagingState<Int, BookSummaryResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookSummaryResponseDto> {
        return try {
            val page = params.key ?: 0
            val response = bookApi.getAllBooks(page = page, size = params.loadSize)

            LoadResult.Page(
                data = response.content,
                prevKey = if (response.first) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class FilteredBookPagingSource @Inject constructor(
    private val bookApi: BookApi,
    private val status: BookStatus?,
    private val difficultyLevel: DifficultyLevel?,
    private val category: String?
) : PagingSource<Int, BookSummaryResponseDto>() {

    override fun getRefreshKey(state: PagingState<Int, BookSummaryResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookSummaryResponseDto> {
        return try {
            val page = params.key ?: 0
            val response = bookApi.filterBooks(
                status = status,
                difficultyLevel = difficultyLevel,
                category = category,
                page = page,
                size = params.loadSize
            )

            LoadResult.Page(
                data = response.content,
                prevKey = if (response.first) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class SearchBookPagingSource @Inject constructor(
    private val bookApi: BookApi,
    private val query: String
) : PagingSource<Int, BookSummaryResponseDto>() {

    override fun getRefreshKey(state: PagingState<Int, BookSummaryResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookSummaryResponseDto> {
        return try {
            val page = params.key ?: 0
            val response = bookApi.searchBooks(
                query = query,
                page = page,
                size = params.loadSize
            )

            LoadResult.Page(
                data = response.content,
                prevKey = if (response.first) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}