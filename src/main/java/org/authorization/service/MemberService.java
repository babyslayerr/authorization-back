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
        Member member = memberRepository.findMemberByUserIdAndPassword(userId, password);
        
        //findMember
        return Optional.of(member);
    }

    /*
    멤버 사용자 등록
     */
    public Member signUpMember(String userId, String password){
        // 같은 이름으로 userId가 있는지 확인
        Member findMember = memberRepository.findMemberByUserId(userId);
        if(findMember != null){
            throw new RuntimeException("이미 해당 아이디가 존재합니다.");
        }
        // 생성
        memberRepository.createMember(userId,password);
        // 생성 된 member 데이터 찾기
        return memberRepository.findMemberByUserIdAndPassword(userId,password);
    }
}
