/*
    com.duk.lab.rxjava.chapter2.Chapter2.java
    learning_RxJava

    Created by Dukwon Nam on 2018. 7. 2..
    Copyright © 2018년 Dukwon Nam. All rights reserved.
*/

package com.duk.lab.rxjava.chapter2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.*;
import java.util.concurrent.*;

public class Chapter2 {
    public void ex1_just() {
        Observable.just(1, 2, 3, 4, 5, 6)
                // Not using SAM conversion
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer data) throws Exception {
                        System.out.println(data);
                    }
                });
    }

    public void ex2_isDisposed() {
        Observable<String> source = Observable.just("RED", "GREEN", "YELLOW");

        Disposable d = source.subscribe(
                next -> System.out.println("onNext():" + next),
                error -> System.out.println("onError():" + error.getMessage()),
                () -> System.out.println("onComplete()")
        );

        System.out.println("isDisposed() = " + d.isDisposed());
    }

    public void ex3_create() {
        Observable<Integer> source = Observable.create(
            (ObservableEmitter<Integer> emitter) -> {
                emitter.onNext(100);
                emitter.onNext(200);
                emitter.onNext(300);
                emitter.onComplete();
        });

        source.subscribe(System.out::println);
    }

    public void ex4_createWithoutSubscribing() {
        Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                });
    }

    public void ex5_createWithLambda() {
        Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                });
        source.subscribe(data -> System.out.println("Result: " + data));
    }

    public void ex6_createWithAnonymous() {
        Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                });
        source.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer data) throws Exception {
                System.out.println("Result: " + data);
            }
        });
    }

    public void ex7_fromArray() {
        Integer[] arr = {100, 200, 300};
        Observable.fromArray(arr)
                .subscribe(System.out::println);
    }

    public void ex8_fromArrayWithPrimitiveType() {
        int[] arr = {100, 200, 300};
        Observable.fromArray(arr)
                .subscribe(System.out::println);
    }

    public void ex9_fromArrayPrimitiveToReference() {
        int[] arr = {100, 200, 300};
//        Observable.fromArray(Arrays.stream(arr))  // Not work
        Observable.fromArray(Arrays.stream(arr).boxed().toArray(Integer[]::new))
                .subscribe(System.out::println);
    }

    public void ex10_fromIterableWithArrayList() {
        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("William");
        names.add("Bob");

        Observable.fromIterable(names)
                .subscribe(System.out::println);
    }

    public void ex11_fromIterableWithHashSet() {
        Set<String> cities = new HashSet<>();
        cities.add("Seoul");
        cities.add("London");
        cities.add("Paris");

        Observable.fromIterable(cities)
                .subscribe(System.out::println);
    }

    public void ex12_fromIterableWithBlockingQueue() {
        BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(100);
        orderQueue.add(new Order("ORD-1"));
        orderQueue.add(new Order("ORD-2"));
        orderQueue.add(new Order("ORD-3"));

        Observable.fromIterable(orderQueue)
                .subscribe(System.out::println);
    }

    public void ex14_fromCallable() {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Hello Callable!";
        };

        Observable.fromCallable(callable)
                .subscribe(System.out::println);
    }

    public void ex15_fromCallableWithoutLambda() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return "Hello Callable!";
            }
        };

        Observable.fromCallable(callable)
                .subscribe(System.out::println);
    }

    public void ex16_fromFuture() {
        Future<String> future = Executors.newSingleThreadExecutor()
                .submit(() -> {
                   Thread.sleep(1000);
                   return "Hello Future";
                });

        Observable.fromFuture(future)
                .subscribe(System.out::println);
    }

    public void ex17_fromPublisher() {
        Publisher<String> publisher = subscriber -> {
            subscriber.onNext("Hello Observable.fromPublisher()");
            subscriber.onComplete();
        };

        Observable.fromPublisher(publisher)
                .subscribe(System.out::println);
    }

    public void ex18_fromPublisherWithoutLambda() {
        Publisher<String> publisher = new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello Observable.fromPublisher()");
                subscriber.onComplete();
            }
        };

        Observable.fromPublisher(publisher)
                .subscribe(System.out::println);
    }

    public void ex19_Single_just() {
        Single.just("Hello Single!")
                .subscribe(System.out::println);
    }

    public void ex20_Single_fromObservableCases() {
        String defaultItem = "default item";

        // 1. Single.fromObservable()
        Observable<String> observable = Observable.just("1. fromObservable");
        Single.fromObservable(observable)
                .subscribe(System.out::println);

        // 2. observable.single()
        Observable.just("2. observable.single()")
                .single(defaultItem)
                .subscribe(System.out::println);

        // 3. observable.first()
        String[] colors = {"RED", "BLUE", "GOLD"};
        Observable.fromArray(colors)
                .first(defaultItem)
                .subscribe(System.out::println);

        // 4. Observable.empty() 상태에서 single()
        Observable.empty()
                .single(defaultItem)
                .subscribe(System.out::println);

        // 5. observable.take()
        Observable.just(new Order("ORD-1"), new Order("ORD-2"))
//                .take(2)      // Not work
                .take(1)
                .single(new Order(defaultItem))
                .subscribe(System.out::println);
    }

    public void ex22_Single_multipleData() {
        Observable.just("A", "B")
                .single("C")
                .subscribe(System.out::println);
    }
}
