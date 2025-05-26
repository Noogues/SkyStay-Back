package com.example.skystayback.repositories;

import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageReducedVO;
import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageTableVO;
import com.example.skystayback.models.AdditionalBaggage;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


@Repository
public interface AdditionalBaggageRepository extends JpaRepository<AdditionalBaggage, Long>{

    @Query("""
        select new com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageTableVO(
            ab.id,
            ab.name,
            ab.weight,
            ab.extraAmount,
            new com.example.skystayback.dtos.airline.AirlineTableVO(
                ai.code,
                ai.name,
                ai.image,
                ai.phone,
                ai.email,
                ai.website,
                ai.iataCode
            )
        )
        from AdditionalBaggage ab
        join ab.airline ai
    """)
    Page<AdditionalBaggageTableVO> getAllAdditionalBaggage(Pageable pageable);


    @Query("""
        select new com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageReducedVO(
            ab.id,
            ab.name
        )
        from AdditionalBaggage ab
    """)
    Page<AdditionalBaggageReducedVO> getAllAdditionalBaggageReduced(Pageable pageable);
}
