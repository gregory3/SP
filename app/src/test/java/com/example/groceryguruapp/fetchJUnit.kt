package com.example.groceryguruapp

import org.junit.Test
import org.json.*
import com.example.groceryguruapp.fetch.KrogerFetch

import org.junit.Assert.*

class fetchJUnit {

    private val fetchdata = KrogerFetch()

    @Test
    fun testProdGet() {
        assertEquals("", fetchdata.getProds(arrayOf("milk"), "21298", "product.compact"))
    }
}