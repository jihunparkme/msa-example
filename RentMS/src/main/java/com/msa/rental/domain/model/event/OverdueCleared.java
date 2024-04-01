package com.msa.rental.domain.model.event;

import com.msa.rental.domain.model.vo.IDName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OverdueCleared implements Serializable {
    private IDName idName;
    private long point;
}
