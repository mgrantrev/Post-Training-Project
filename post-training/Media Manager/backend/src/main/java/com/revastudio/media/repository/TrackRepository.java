package com.revastudio.media.repository;

import com.revastudio.media.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {

    // Native query joining invoices (works for typical Chinook Postgres naming). Adjust identifiers if your DB uses different names.
    @Query(value = "SELECT t.* FROM track t JOIN invoice_line il ON il.track_id = t.track_id JOIN invoice i ON i.invoice_id = il.invoice_id WHERE i.customer_id = :customerId", nativeQuery = true)
    List<Track> findTracksPurchasedByCustomer(@Param("customerId") Integer customerId);
}
