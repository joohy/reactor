package com.example.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

@SpringBootTest
public class FluxCreateOperationTests {

    //TODO : Create Operator
    @Test
    public void createAflux_first(){
        //생성만 되고 구독X only read
        Flux<String> flux = Flux.just("Hello World");
    }


    @Test
    public void createAflux_subscribe(){
        Flux<String> flux = Flux.just("Apple", "Grape", "Orange");
        flux.subscribe(System.out::println);
    }

    @Test
    public void stepVerifier(){
        Flux<String> flux = Flux.just("Apple", "Grape", "Orange");
        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Grape")
                .expectNext("Orange")
                .verifyComplete();
    }

    @Test
    public void createFlux_fromArray(){
        String[] fruits = new String[]{"apple", "grape", "orange"};
        Flux<String> flux = Flux.fromArray(fruits);
        StepVerifier.create(flux)
                .expectNext("apple")
                .expectNext("grape")
                .expectNext("orange")
                .verifyComplete();
    }

    //TODO : subscribe하지 않으면
    @Test
    public void iterable(){
        Flux.fromIterable(Arrays.asList("foo","bar"))
                .doOnNext(System.out::println)
                .map(String::toUpperCase);
//                .subscribe(System.out::println);

        System.out.println("이거먼저 ??");

    }

    @Test
    public void range(){
        Flux<Integer> flux = Flux.range(1,3);
        StepVerifier.create(flux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete();
    }

    //TODO : async 확인할것
    @Test
    public void interval() {
        Flux.interval(Duration.ofMillis(100)).take(10).subscribe(System.out::println);
        System.out.println("이거먼저?");
    }

    //TODO : Mono Type 설명
    @Test
    public void mono() throws InterruptedException {

        Mono.firstWithValue(
                        Mono.delay(Duration.ofMillis(100)).thenReturn("bar"),
                        Mono.just(1).map(integer -> "foo" + integer)
                )
                .subscribe(System.out::println);
    }

    @Test
    public void mono_error() throws InterruptedException {
        Mono.error(new IllegalStateException()).subscribe(System.out::println);
    }
}
