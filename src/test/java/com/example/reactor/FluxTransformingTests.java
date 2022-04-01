package com.example.reactor;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@SpringBootTest
public class FluxTransformingTests {

    @Test
    public void skipAFew() {
        Flux<String> countFlux = Flux.just(
                        "one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        StepVerifier.create(countFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    //TODO : 1초 지연 -> 4초 웨이팅
    @Test
    public void skipAFewSeconds() {
        Flux<String> countFlux = Flux.just(
                        "one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        StepVerifier.create(countFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    @Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
                .take(3);

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    //TODO : 3.5초 경과
    @Test
    public void takeForAwhile() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("flux");
        System.out.println("");
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500));
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        nationalParkFlux.subscribe(System.out::println);
        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete();
    }

    @Test
    public void distinct() {
        Flux<String> animalFlux = Flux.just(
                        "dog", "cat", "bird", "dog", "bird", "anteater")
                .distinct();

        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "anteater")
                .verifyComplete();
    }


    //TODO :
    @Test
    public void map() {
//        Flux<Player> playerFlux = Flux
//                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
//                .map(n -> {
//                    String[] split = n.split("\\s");
//                    return new Player(split[0], split[1]);
//                });
//
//        StepVerifier.create(playerFlux)
//                .expectNext(new Player("Michael", "Jordan"))
//                .expectNext(new Player("Scottie", "Pippen"))
//                .expectNext(new Player("Steve", "Kerr"))
//                .verifyComplete();
        Flux<String> flux = Flux.just("a", "b", "c")
                .map(m->m.toUpperCase());

        flux.subscribe(System.out::println);
        StepVerifier.create(flux)
                .expectNext("A")
                .expectNext("B")
                .expectNext("C")
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        Flux<String> flux = Flux.just("a", "b", "c")
                .flatMap(n-> Mono.just(n)
                        .map(s-> s.toUpperCase())
                );


//        flux.subscribe(System.out::println);
        flux.subscribeOn(Schedulers.parallel())
                .log();

    }

    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;

        public Player(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }


    }
}
