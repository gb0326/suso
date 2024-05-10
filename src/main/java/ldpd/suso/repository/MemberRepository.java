package ldpd.suso.repository;

import ldpd.suso.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByusername(String username); //로그인 시 넘어온 사용자id와 siteuser엔티티에 있는 id와 비교해야 하므로 작성
    Member findByname(String name);
    Member findByEmail(String email);
}
