package com.msa.member;

import com.msa.member.domain.model.Member;
import com.msa.member.domain.model.vo.Email;
import com.msa.member.domain.model.vo.IDName;
import com.msa.member.domain.model.vo.PassWord;
import com.msa.member.domain.model.vo.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberDomainTest {
    @Test
    void member_domain_test() throws Exception {
        var member = Member.registerMember(
                new IDName("scant", "jenny"),
                new PassWord("12345","abcde"),
                new Email("scant10@gmail.com"));

        member.savePoint(new Point(10));
        Assertions.assertEquals(new Point(10), member.getPoint());

        member.savePoint(new Point(20));
        Assertions.assertEquals(new Point(30), member.getPoint());

        member.usePoint(new Point(10));
        Assertions.assertEquals(new Point(20), member.getPoint());
    }
}
