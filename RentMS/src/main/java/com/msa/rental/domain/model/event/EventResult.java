package com.msa.rental.domain.model.event;

import com.msa.rental.domain.model.vo.IDName;
import com.msa.rental.domain.model.vo.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventResult implements Serializable {
    private EventType eventType;
    private boolean isSuccessed;
    private IDName idName;
    private Item item;
    private long point;
}