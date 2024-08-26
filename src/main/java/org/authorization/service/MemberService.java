package org.authorization.service;

import org.authorization.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {


    public Optional<Member> login(String userId, String password) {

        //TODO
        Member member = new Member();
        member.setUserId(userId);
        //findMember
        return Optional.of(member);
    }
}
