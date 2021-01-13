package com.nononsensecode.simple.spring.data.commons

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class OffsetBasedPageRequest(
    _offset: Int,
    _limit: Int
): Pageable {
    val offset: Int
    val limit: Int

    init {
        if (_offset < 0) {
            throw IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (_limit < 1) {
            throw IllegalArgumentException("Limit must not be less than one!");
        }

        this.limit = _limit;
        this.offset = _offset;
    }

    override fun getPageNumber(): Int {
        return offset/limit
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getOffset(): Long {
        return offset.toLong()
    }

    override fun getSort(): Sort {
        return Sort.by(Sort.DEFAULT_DIRECTION)
    }

    override fun next(): Pageable {
        return OffsetBasedPageRequest(offset + pageSize, pageSize)
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }

    private fun previous(): Pageable {
        return if (hasPrevious()) {
            OffsetBasedPageRequest(offset - pageSize, pageSize)
        } else {
            this
        }
    }

    override fun first(): Pageable {
        return OffsetBasedPageRequest(0, pageSize)
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) {
            previous()
        } else {
            first()
        }
    }
}