package org.naukma.spring.modulith.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAllByOrganiserId(Long organiserId);
    @Query("SELECT e FROM EventEntity e JOIN e.participants p WHERE p.id = :participantId")
    List<EventEntity> findAllByParticipantId(Long participantId);

}
