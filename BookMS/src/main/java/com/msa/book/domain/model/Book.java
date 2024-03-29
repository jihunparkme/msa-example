package com.msa.book.domain.model;

import com.msa.book.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;
    private String title;
    @Embedded
    private BookDesc desc;
    private Classification classfication;
    private BookStatus bookStatus;
    private Location location;

    public static Book enterBook(String title,
                                 String author,
                                 String isbn,
                                 String description,
                                 LocalDate publicationDate,
                                 Source source,
                                 Classification classfication,
                                 Location location) {

        BookDesc bookDesc = BookDesc.createBookDesc(
                author, isbn, description, publicationDate, source);
        Book book = new Book();
        book.setTitle(title);
        book.setDesc(bookDesc);
        book.setClassfication(classfication);
        book.setLocation(location);
        book.setBookStatus(BookStatus.ENTERED);
        return book;
    }

    public Book makeAvailable() {
        this.setBookStatus(BookStatus.AVAILABLE);
        return this;
    }

    public Book makeUnAvailable() {
        this.setBookStatus(BookStatus.UNAVAILABLE);
        return this;
    }
}
