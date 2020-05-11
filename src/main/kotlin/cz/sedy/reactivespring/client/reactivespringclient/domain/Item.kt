package cz.sedy.reactivespring.client.reactivespringclient.domain

import java.math.BigDecimal

data class Item(

        val id: String,
        var description: String,
        var price: BigDecimal
)