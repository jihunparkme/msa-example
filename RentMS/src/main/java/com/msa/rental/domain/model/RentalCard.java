package com.msa.rental.domain.model;

import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.event.OverdueCleared;
import com.msa.rental.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * 대여라는 비즈니스 활동의 명시적으로 드러나도록 RentalCard 개념 도출
 * 연도 + UUID 값을 갖는 RentalCardNo 을 식별자로 정의
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RentalCard {
    @EmbeddedId
    private RentalCardNo rentalCardNo;

    @Embedded
    private IDName member;

    private RentStatus rentStatus;

    @Embedded
    private LateFee lateFee;

    @ElementCollection
    private List<RentalItem> rentalItemList = new ArrayList<>();

    @ElementCollection
    private List<ReturnItem> returnItemLIst = new ArrayList<>();

    private void addRentalItem(RentalItem rentalItem) {
        this.rentalItemList.add(rentalItem);
    }

    private void removeRentalItem(RentalItem rentalItem) {
        this.rentalItemList.remove(rentalItem);
    }

    private void removeReturnItem(ReturnItem returnItem) {
        this.returnItemLIst.remove(returnItem);
    }

    private void addReturnItem(ReturnItem returnItem) {
        this.returnItemLIst.add(returnItem);
    }

    public static RentalCard createRentalCard(IDName creator) {
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(creator);
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setLateFee(LateFee.createLateFee());
        return rentalCard;
    }

    public static ItemRented createItemRentedEvent(IDName idName, Item item, long point) {
        return new ItemRented(idName, item, point);
    }

    public static ItemReturned createItemReturnEvent(IDName idName, Item item, long point) {
        return new ItemReturned(idName, item, point);
    }

    public static OverdueCleared createOverdueCleardEvent(IDName idName, long point) {
        return new OverdueCleared(idName, point);
    }

    public RentalCard rentItem(Item item) {
        checkRentalAvailable();
        this.addRentalItem(RentalItem.createRentalItem(item));
        return this;
    }

    /**
     * 보상 트랜잭션 대여 취소
     */
    public RentalCard cancelRentItem(Item item) {
        RentalItem rentalItem = this.rentalItemList.stream()
                .filter(i -> i.getItem().equals(item))
                .findFirst().get();
        this.rentalItemList.remove(rentalItem);
        return this;
    }

    /**
     * 보상 트랜잭션 반납 취소
     */
    public RentalCard cancelReturnItem(Item item, long point) {
        ReturnItem returnItem = this.returnItemLIst.stream()
                .filter(i -> i.getRentalItem().getItem().equals(item))
                .findFirst().get();
        this.addRentalItem(returnItem.getRentalItem());
        this.removeReturnItem(returnItem);
        return this;
    }

    private void checkRentalAvailable() {
        if (this.rentStatus == RentStatus.RENT_UNAVAILABLE) {
            throw new IllegalArgumentException("대여불가상태입니다.");
        }

        if (this.rentalItemList.size() > 5) {
            throw new IllegalArgumentException("이미 5권을 대여했습니다.");
        }
    }

    public RentalCard returnItem(Item item, LocalDate returnDate) {
        RentalItem rentalItem = this.rentalItemList.stream()
                .filter(i -> i.getItem().equals(item)).
                findFirst().get();
        calculateLateFee(rentalItem, returnDate);
        this.addReturnItem(ReturnItem.createReturnItem(rentalItem));
        this.removeRentalItem(rentalItem);
        return this;
    }

    private void calculateLateFee(RentalItem rentalItem, LocalDate returnDate) {
        if (returnDate.compareTo(rentalItem.getOverdueDate()) > 0) {
            long point = Period.between(rentalItem.getOverdueDate(), returnDate).getDays() * 10;
            LateFee addPoint = this.lateFee.addPoint(point);
            this.lateFee.setPoint(addPoint.getPoint());
        }
    }

    /**
     *  강제 연체 처리
     *  시스템에 의해서 배치나 스케줄로 실행
     */
    public RentalCard overdueItem(Item item) {
        RentalItem rentalItem = this.rentalItemList.stream()
                .filter(i -> i.getItem().equals(item))
                .findFirst().get();
        rentalItem.setOverdue(true);
        this.rentStatus = RentStatus.RENT_UNAVAILABLE;
        rentalItem.setOverdueDate(LocalDate.now().minusDays(1));
        return this;
    }

    public long makeAvailableRental(long point) throws Exception {
        if (this.rentalItemList.size() != 0) {
            throw new IllegalArgumentException("모든 도서가 반납되어야 정지를 해제할 수 있습니다.");
        }
        if (this.getLateFee().getPoint() != point) {
            throw new IllegalArgumentException("해당 포인트로 연체를 해제할 수 없습니다.");
        }

        this.setLateFee(lateFee.removePoint(point));
        if (this.getLateFee().getPoint() == 0) {
            this.rentStatus = RentStatus.RENT_AVAILABLE;
        }
        return this.getLateFee().getPoint();

    }

    /**
     * 보상 트랜잭션 정지 대여 해제 처리 취소
     */
    public long cancelMakeAvailableRental(long point) {
        this.setLateFee(lateFee.addPoint(point));
        this.rentStatus = RentStatus.RENT_UNAVAILABLE;
        return this.lateFee.getPoint();
    }
}
