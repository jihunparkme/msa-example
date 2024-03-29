package com.msa.member.framework.web.dto;

import com.msa.member.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberOutPutDTO {
    private String id;
    private String Name;
    private String passWord;
    private String email;
    private long point;

    public static MemberOutPutDTO mapToDTO(Member member) {
        MemberOutPutDTO memberOutPutDTO = new MemberOutPutDTO();
        memberOutPutDTO.setId(member.getIdName().getId());
        memberOutPutDTO.setName(member.getIdName().getName());
        memberOutPutDTO.setPassWord(member.getPassword().getPresentPWD());
        memberOutPutDTO.setEmail(member.getEmail().getAddress());
        memberOutPutDTO.setPoint(member.getPoint().getPointValue());
        return memberOutPutDTO;
    }
}
