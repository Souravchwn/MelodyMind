package com.sourav.melodymind.repository;

import com.sourav.melodymind.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    
    List<UserFavorite> findByUserIdOrderByAddedAtDesc(Long userId);
    
    Optional<UserFavorite> findByUserIdAndSongId(Long userId, Long songId);
    
    boolean existsByUserIdAndSongId(Long userId, Long songId);
    
    void deleteByUserIdAndSongId(Long userId, Long songId);
    
    @Query("SELECT uf FROM UserFavorite uf JOIN FETCH uf.song WHERE uf.userId = :userId ORDER BY uf.addedAt DESC")
    List<UserFavorite> findByUserIdWithSongs(@Param("userId") Long userId);
    
    long countByUserId(Long userId);
}