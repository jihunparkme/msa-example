package com.msa.member.application.outputport;

import com.msa.member.domain.model.Member;
import com.msa.member.domain.model.vo.IDName;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberOutPutPort {
    Member loadMember(long memberNo);

    Member loadMemberByIdName(IDName idName);

    Member saveMember(Member member);
}
