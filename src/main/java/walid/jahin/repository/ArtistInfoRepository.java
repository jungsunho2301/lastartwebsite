package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import walid.jahin.model.ArtistInfo;

public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            MERGE INTO ARTIST_INFO ai
            USING (SELECT 1 AS id, :email AS email FROM dual) src
            ON (ai.ID = src.id)
            WHEN MATCHED THEN
              UPDATE SET ai.EMAIL = src.email
            WHEN NOT MATCHED THEN
              INSERT (ID, EMAIL) VALUES (src.id, src.email)
            """, nativeQuery = true)
    void upsertEmail(@Param("email") String email);
}
