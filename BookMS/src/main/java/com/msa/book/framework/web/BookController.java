package com.msa.book.framework.web;

import com.msa.book.application.usecase.AddBookUseCase;
import com.msa.book.application.usecase.InquiryUseCase;
import com.msa.book.framework.web.dto.BookInfoDTO;
import com.msa.book.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final AddBookUseCase addBookUsecase;
    private final InquiryUseCase inquiryUsecase;

    @PostMapping("/book")
    public ResponseEntity<BookOutPutDTO> createBook(@RequestBody BookInfoDTO bookInfoDTO) {
        BookOutPutDTO bookOutPutDTO = addBookUsecase.addBook(bookInfoDTO);
        return new ResponseEntity<>(bookOutPutDTO, HttpStatus.CREATED);
    }

    @GetMapping("/book/{no}")
    public ResponseEntity<BookOutPutDTO> getBookInfo(@PathVariable("no") String no) {
        BookOutPutDTO bookInfo = inquiryUsecase.getBookInfo(Long.parseLong(no));

        return bookInfo != null
                ? new ResponseEntity<>(bookInfo, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
