package ldpd.suso.repository;


import ldpd.suso.entity.Correct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectRepository extends JpaRepository<Correct, Integer> {
}
