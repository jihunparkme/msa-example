package com.msa.rental.domain.model.vo;

import com.msa.rental.domain.model.RentalItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDate;

/**
 * 렌탈카드는 대여 품목 목록을 보유
 * 대여 품목은 아이템, 대여일, 연체여부, 반납예정일로 구성
 * 연체 여부에 따라 연체 도서로 상태가 변경되기도 하기 때문에 엔티티로 정의
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReturnItem {
    @Embedded
    private RentalItem rentalItem;
    private LocalDate returnDate;

    public static ReturnItem createReturnItem(RentalItem rentalItem) {
        return new ReturnItem(rentalItem, LocalDate.now());
    }
}
