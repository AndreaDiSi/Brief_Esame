package com.andreadisimone.dtos.reservation_dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {
    private Integer idReservation;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private Integer idTenant;
    private Integer idAccomodation;
}