package com.msa.book.application.inputport;

import com.msa.book.application.outputport.BookOutPutPort;
import com.msa.book.application.usecase.MakeUnAvailableUseCase;
import com.msa.book.domain.model.Book;
import com.msa.book.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MakeUnAvailableInputPort implements MakeUnAvailableUseCase {
    private final BookOutPutPort bookOutPutPort;

    @Override
    public BookOutPutDTO unavailable(Long bookNo) {
        Book loadBook = bookOutPutPort.loadBook(bookNo);
        loadBook.makeUnAvailable();
        return BookOutPutDTO.mapToDTO(loadBook);
    }
}
