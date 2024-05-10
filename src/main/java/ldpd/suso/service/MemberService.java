package ldpd.suso.service;


import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@Slf4j
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder; // PasswordEncoder 필드 추가

    @Autowired
    public MemberService(PasswordEncoder passwordEncoder) { // 생성자를 통해 PasswordEncoder 주입
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(String username, String name, String email, String password) {
        Member user = new Member();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(user);
        return user;
    }

    public void updateUsername(String currentUsername, String newUsername) {
        Member member = memberRepository.findByusername(currentUsername);
        if (member != null) {
            member.setUsername(newUsername);
            memberRepository.save(member);
        }
    }

    public void updateEmail(String username, String newEmail) {
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            member.setEmail(newEmail);
            memberRepository.save(member);
        }
    }

    public void updateName(String username, String newName) {
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            member.setName(newName);
            memberRepository.save(member);
        }
    }

    public int updatePassword(String username, String currentPassword, String newPassword) {
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            // 비밀번호 변경 로직 추가
            // 예를 들어, 암호화된 비밀번호 비교 후 새로운 비밀번호 설정
            if (passwordEncoder.matches(currentPassword, member.getPassword())) {
                member.setPassword(passwordEncoder.encode(newPassword));
                memberRepository.save(member);
                return 1;
            }

        }
        return -1;
    }

}