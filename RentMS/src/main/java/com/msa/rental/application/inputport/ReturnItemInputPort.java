package com.msa.rental.application.inputport;

import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.application.outputport.RentalCardOutputPort;
import com.msa.rental.application.usecase.ReturnItemUserCase;
import com.msa.rental.domain.model.RentalCard;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.vo.Item;
import com.msa.rental.framework.web.dto.RentalCardOutputDTO;
import com.msa.rental.framework.web.dto.UserItemInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnItemInputPort implements ReturnItemUserCase {

    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public RentalCardOutputDTO returnItem(UserItemInputDTO returnDto) throws Exception {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(returnDto.userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        Item returnItem = new Item(returnDto.getItemId(), returnDto.getItemTitle());
        rentalCard.returnItem(returnItem, LocalDate.now());

        ItemReturned itemReturnEvent = RentalCard.createItemReturnEvent(rentalCard.getMember(), returnItem, 10L);
        eventOutputPort.occurReturnEvent(itemReturnEvent);

        return RentalCardOutputDTO.mapToDTO(rentalCard);
    }
}
