package org.skypro.hogwarts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/util")
public class UtilController {

    @GetMapping("/sum-million")
    public long calculateSum() {
        long n = 1_000_000;
        return n * (n + 1) / 2;
    }

    @GetMapping("/sum-million-slow")
    public long calculateSumSlow() {
        return LongStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Long::sum);
    }
}