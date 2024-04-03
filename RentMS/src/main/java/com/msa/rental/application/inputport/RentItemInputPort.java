package com.msa.rental.application.inputport;

import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.application.outputport.RentalCardOutputPort;
import com.msa.rental.application.usecase.RentItemUseCase;
import com.msa.rental.domain.model.RentalCard;
import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.vo.IDName;
import com.msa.rental.domain.model.vo.Item;
import com.msa.rental.framework.web.dto.RentalCardOutputDTO;
import com.msa.rental.framework.web.dto.UserItemInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentItemInputPort implements RentItemUseCase {

    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public RentalCardOutputDTO rentItem(UserItemInputDTO rental) throws Exception {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(rental.userId)
                .orElseGet(() -> RentalCard.createRentalCard(new IDName(rental.getUserId(), rental.getUserNm())));

        /**
         * 비즈니스 처리와 이벤트 발행은 트랜잭션이 보장되어야 한다.
         */
        Item returnItem = new Item(rental.getItemId(), rental.getItemTitle());
        rentalCard.rentItem(returnItem);

        // 대여 이벤트 생성 및 발행
        ItemRented itemRentedEvent = RentalCard.createItemRentedEvent(rentalCard.getMember(), returnItem, 10L);
        eventOutputPort.occurRentalEvent(itemRentedEvent);

        return RentalCardOutputDTO.mapToDTO(rentalCard);
    }
}
