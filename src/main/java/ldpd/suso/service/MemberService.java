package ldpd.suso.service;


import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder; // PasswordEncoder 필드 추가

    @Autowired
    public MemberService(PasswordEncoder passwordEncoder) { // 생성자를 통해 PasswordEncoder 주입
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(String username, String name, String password) {
        Member user = new Member();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(user);
        return user;
    }

}