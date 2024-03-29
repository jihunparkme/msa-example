package com.msa.book.application.usecase;

import com.msa.book.framework.web.dto.BookOutPutDTO;

public interface InquiryUseCase {
    BookOutPutDTO getBookInfo(long bookNo);
}
