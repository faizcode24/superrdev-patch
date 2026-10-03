package com.internal.tasktracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Search non-archived tasks by term (title OR description) and optional status.
    // The OR is parenthesized so that "archived = FALSE" and the status filter
    // apply to BOTH title and description matches.
    // Pagination is done in the database via LIMIT/OFFSET.
    @Query(value = "SELECT * FROM tasks "
                 + "WHERE archived = FALSE "
                 + "AND (LOWER(title) LIKE :term ESCAPE '\\' OR LOWER(description) LIKE :term ESCAPE '\\') "
                 + "AND (:status IS NULL OR status = :status) "
                 + "ORDER BY created_at DESC, id DESC "
                 + "LIMIT :limit OFFSET :offset",
           nativeQuery = true)
    List<Task> searchTasks(@Param("term") String term,
                           @Param("status") String status,
                           @Param("limit") int limit,
                           @Param("offset") int offset);

    // Same filter as above, used for the total count shown in pagination.
    @Query(value = "SELECT COUNT(*) FROM tasks "
                 + "WHERE archived = FALSE "
                 + "AND (LOWER(title) LIKE :term ESCAPE '\\' OR LOWER(description) LIKE :term ESCAPE '\\') "
                 + "AND (:status IS NULL OR status = :status)",
           nativeQuery = true)
    long countTasks(@Param("term") String term, @Param("status") String status);
}
