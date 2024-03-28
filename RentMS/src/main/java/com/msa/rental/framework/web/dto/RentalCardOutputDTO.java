package com.msa.rental.framework.web.dto;

import com.msa.rental.domain.model.RentalCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalCardOutputDTO {

    private String rentalCardId;
    private String memberId;
    private String memberName;
    private String rentStatus; // 대여가능여부
    private Long totalLateFee; // 전체연체료
    private Long totalRentalCnt; // 전체대여도서건수
    private Long totalReturnCnt; // 반납도서건수
    private Long totalOverdueCnt; // 연체중인도서건수

    public static RentalCardOutputDTO mapToDTO(RentalCard rental){
        RentalCardOutputDTO rentDTO = new RentalCardOutputDTO();
        rentDTO.setRentalCardId(rental.getRentalCardNo().getNo().toString());
        rentDTO.setMemberId(rental.getMember().getId().toString());
        rentDTO.setMemberName(rental.getMember().getName());
        rentDTO.setRentStatus(rental.getRentStatus().toString());
        rentDTO.setTotalRentalCnt(rental.getRentalItemList().stream().count());
        rentDTO.setTotalReturnCnt(rental.getReturnItemLIst().stream().count());
        rentDTO.setTotalOverdueCnt(rental.getRentalItemList().stream().filter(i -> i.isOverdue()).count());
        return rentDTO;
    }
}
