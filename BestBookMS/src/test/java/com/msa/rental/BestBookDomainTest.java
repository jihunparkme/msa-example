package com.msa.rental;

import com.msa.bestbook.domain.model.BestBook;
import com.msa.bestbook.domain.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BestBookDomainTest {
    @Test
    void best_book_domain_test() {
        BestBook bestBook1 = BestBook.registerBestBook(new Item(10, "도메인주도로 시작하는 마이크로서비스 개발"));
        bestBook1.increaseBestBookCount();
        bestBook1.increaseBestBookCount();
        Assertions.assertEquals(3, bestBook1.getRentCount());

        BestBook bestBook2 = BestBook.registerBestBook(new Item(20, "경험과 사례로 풀어낸 성공하는 애자일"));
        bestBook2.increaseBestBookCount();
        Assertions.assertEquals(2, bestBook2.getRentCount());;
    }
}
