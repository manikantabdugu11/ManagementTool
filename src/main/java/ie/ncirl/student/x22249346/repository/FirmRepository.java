package ie.ncirl.student.x22249346.repository;

import java.util.List;

import javax.transaction.Transactional;

import ie.ncirl.student.x22249346.entity.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FirmRepository extends JpaRepository<Firm, Integer> {
  List<Firm> findByTitleContainingIgnoreCase(String keyword);

  @Query("UPDATE Firm t SET t.published = :published WHERE t.id = :id")
  @Modifying
  public void updatePublishedStatus(Integer id, boolean published);
}
