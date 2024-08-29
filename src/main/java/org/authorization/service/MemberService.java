package org.authorization.service;

import org.authorization.Member;
import org.authorization.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Optional<Member> login(String userId, String password) {

        //검색
        Member member = memberRepository.selectMemberByUserIdAndMember(userId, password);
        
        //findMember
        return Optional.of(member);
    }
}
