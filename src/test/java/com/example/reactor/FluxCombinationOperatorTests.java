package com.example.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

@SpringBootTest
public class FluxCombinationOperatorTests {


    //Merge Operator
    @Test
    public void mergeflux(){
        Flux<String> characterFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux.just("GukBab","ChooATang","Samgupsal")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));
        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
        StepVerifier.create(mergedFlux)
                .expectNext("A")
                .expectNext("GukBab")
                .expectNext("B")
                .expectNext("ChooATang")
                .expectNext("C")
                .expectNext("Samgupsal")
                .verifyComplete();

    }

    @Test
    public void zipFluxes(){
        Flux<String> characterFlux = Flux.just("A","B","C");

        Flux<String> foodFlux = Flux.just("GukBab","ChooATang","Samgupsal");


        Flux<Tuple2<String, String>> mergedFlux = Flux.zip(characterFlux, foodFlux);
        StepVerifier.create(mergedFlux)
                .expectNextMatches(p-> p.getT1().equals("A") && p.getT2().equals("GukBab"))
                .expectNextMatches( p-> p.getT1().equals("B") && p.getT2().equals("ChooATang"))
                .expectNextMatches( p-> p.getT1().equals("C") && p.getT2().equals("Samgupsal"))
                .verifyComplete()
        ;

    }

    @Test
    public void firstFlux(){
        Flux<String> first = Flux.just("1","2","3");
        Flux<String> last = Flux.just("A","B","C").delaySubscription(Duration.ofMillis(500));
        Flux<String> mergedFlux = Flux.first(first, last);

        StepVerifier.create(mergedFlux)
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();

    }
}
