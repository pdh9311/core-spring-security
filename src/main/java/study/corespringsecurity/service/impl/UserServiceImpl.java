package study.corespringsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.corespringsecurity.domain.Member;
import study.corespringsecurity.repository.MemberRepository;
import study.corespringsecurity.service.MemberService;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void createUser(Member member) {
        memberRepository.save(member);
    }
}
