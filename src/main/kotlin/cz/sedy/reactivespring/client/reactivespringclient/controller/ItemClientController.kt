package cz.sedy.reactivespring.client.reactivespringclient.controller

import cz.sedy.reactivespring.client.reactivespringclient.domain.Item
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ItemClientController(val webClient: WebClient= WebClient.create("http://localhost:8080")) {

    @GetMapping("/client/retrieve")
    fun getAllItemsUsingRetrieve(): Flux<Item> {
        return webClient.get().uri("/v1/items/")
                .retrieve()
                .bodyToFlux(Item::class.java)
                .log("Items in Client project")
    }

    @GetMapping("/client/exchange")
    fun getAllItemsUsingExchange(): Flux<Item> {
        return webClient.get().uri("/v1/items/")
                .exchange()
                .flatMapMany { clientResponse: ClientResponse? ->
                    clientResponse?.bodyToFlux(Item::class.java)
                }
                .log("Items in Client project exchange")
    }

    @GetMapping("/client/fun/retrieve")
    fun getAllItemsFunctionalUsingRetrieve(): Flux<Item> {
        return webClient.get().uri("/v1/fun/items/")
                .retrieve()
                .bodyToFlux(Item::class.java)
                .log("Items in Client project")
    }

    @GetMapping("/client/fun/exchange")
    fun getAllItemsFunctionalUsingExchange(): Flux<Item> {
        return webClient.get().uri("/v1/fun/items/")
                .exchange()
                .flatMapMany { clientResponse: ClientResponse? ->
                    clientResponse?.bodyToFlux(Item::class.java)
                }
                .log("Items in Client project exchange")
    }


    @GetMapping("/client/fun/retrieve/singleItem")
    fun getSingleItemFunctionalUsingRetrieve(): Mono<Item> {
        val id = "ABC"
        return webClient.get().uri("/v1/fun/items/{id}", id)
                .retrieve()
                .bodyToMono(Item::class.java)
                .log("Items in Client project")
    }

    @GetMapping("/client/fun/exchange/singleItem")
    fun getSingleItemFunctionalUsingExchange(): Mono<Item> {
        val id = "ABC"
        return webClient.get().uri("/v1/fun/items/{id}", id)
                .exchange()
                .flatMap { clientResponse: ClientResponse? ->
                    clientResponse?.bodyToMono(Item::class.java)
                }
                .log("Items in Client project exchange")
    }

    @PostMapping("/client/createItem")
    fun createItem(@RequestBody item: Item): Mono<Item>{
        val newItemMono: Mono<Item> = Mono.just(item)
        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newItemMono, Item::class.java)
                .retrieve()
                .bodyToMono(Item::class.java)
                .log("Created item is : ")
    }

    @PutMapping("/client/updateItem/{id}")
    fun updateItem(@PathVariable id: String,
                   @RequestBody item: Item): Mono<Item>{
        val newItemMono: Mono<Item> = Mono.just(item)
        return webClient.put().uri("/v1/fun/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(newItemMono, Item::class.java)
                .retrieve()
                .bodyToMono(Item::class.java)
                .log("Created item is : ")
    }


    @DeleteMapping("/client/deleteItem/{id}")
    fun deleteItem(@PathVariable id: String){
        webClient.delete().uri("/v1/fun/items/{id}", id)
                .retrieve()
    }
}