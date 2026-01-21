package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.ChatMessage;
import com.academiaSpringBoot.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m WHERE m.user = :user ORDER BY m.createdAt DESC")
    List<ChatMessage> findRecentMessages(@Param("user") User user, Pageable pageable);
}
