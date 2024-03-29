package com.msa.member;

import com.msa.book.domain.model.Book;
import com.msa.book.domain.model.vo.BookStatus;
import com.msa.book.domain.model.vo.Classfication;
import com.msa.book.domain.model.vo.Location;
import com.msa.book.domain.model.vo.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BookDomainTest {

    @Test
    void book_available_test() {
        Book book = Book.enterBook( "노인과바다",
                "훼밍웨이",
                "2312321",
                "주인공 노인과 바다",
                LocalDate.now(),
                Source.SUPPLY,
                Classfication.LITERATURE,
                Location.PANGYO);

        book.makeAvailable();
        Assertions.assertEquals(BookStatus.AVAILABLE, book.getBookStatus());
    }

    @Test
    void book_unavailable_test() {
        Book sample = Book.enterBook("엔터프라이즈 아키텍처 패턴", "마틴파울러", "21321321", "엔터프라이즈 패턴에 관한 좋은 서적",
                LocalDate.now(),
                Source.SUPPLY,
                Classfication.COMPUTER,
                Location.JEONGJA);

        sample.makeUnAvailable();
        Assertions.assertEquals(BookStatus.UNAVAILABLE, sample.getBookStatus());

    }
}
