package pl.seegoosh.breakoutstrategysim.closing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClosingValueRepository extends JpaRepository<ClosingValue, Long> {
    @Query("SELECT c FROM ClosingValue c WHERE c.date > ?1")
    List<ClosingValue> findClosingValuesSince(LocalDate startDate);
}
