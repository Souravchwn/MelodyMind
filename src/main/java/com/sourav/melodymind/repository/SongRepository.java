package com.sourav.melodymind.repository;

import com.sourav.melodymind.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    Optional<Song> findByTitleAndArtist(String title, String artist);
    
    List<Song> findByGenreIgnoreCase(String genre);
    
    List<Song> findByLanguageIgnoreCase(String language);
    
    List<Song> findByRegionIgnoreCase(String region);
    
    List<Song> findByMoodIgnoreCase(String mood);
    
    @Query("SELECT s FROM Song s WHERE " +
           "(:genre IS NULL OR LOWER(s.genre) = LOWER(:genre)) AND " +
           "(:language IS NULL OR LOWER(s.language) = LOWER(:language)) AND " +
           "(:region IS NULL OR LOWER(s.region) = LOWER(:region)) AND " +
           "(:mood IS NULL OR LOWER(s.mood) = LOWER(:mood)) " +
           "ORDER BY s.popularityScore DESC, s.fetchedAt DESC")
    List<Song> findRecommendations(@Param("genre") String genre,
                                 @Param("language") String language,
                                 @Param("region") String region,
                                 @Param("mood") String mood);
    
    @Query("SELECT s FROM Song s ORDER BY s.popularityScore DESC, s.fetchedAt DESC")
    List<Song> findTrendingSongs();
    
    List<Song> findByFetchedAtAfter(LocalDateTime dateTime);
    
    @Query("SELECT DISTINCT s.genre FROM Song s WHERE s.genre IS NOT NULL")
    List<String> findDistinctGenres();
    
    @Query("SELECT DISTINCT s.language FROM Song s WHERE s.language IS NOT NULL")
    List<String> findDistinctLanguages();
    
    @Query("SELECT DISTINCT s.region FROM Song s WHERE s.region IS NOT NULL")
    List<String> findDistinctRegions();
    
    @Query("SELECT DISTINCT s.mood FROM Song s WHERE s.mood IS NOT NULL")
    List<String> findDistinctMoods();
    
    @Query("SELECT s.id FROM Song s WHERE s.fetchedAt < :cutoffDate AND s.id NOT IN " +
           "(SELECT DISTINCT uf.songId FROM UserFavorite uf)")
    List<Long> findOldSongsNotInFavorites(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    void deleteByIdIn(List<Long> ids);
    
    @Query("SELECT COUNT(s) FROM Song s WHERE s.fetchedAt < :cutoffDate")
    long countSongsOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT COUNT(s) FROM Song s WHERE s.fetchedAt >= :startDate AND s.fetchedAt < :endDate")
    long countSongsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}