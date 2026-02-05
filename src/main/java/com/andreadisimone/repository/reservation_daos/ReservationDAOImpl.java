package com.andreadisimone.repository.reservation_daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.reservation_dtos.ReservationRequestDTO;
import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO request) {
        String sql = """
            INSERT INTO reservation(reservation_start_date, reservation_end_date, id_tenant, id_accomodation) 
            VALUES(?, ?, ?, ?) 
            RETURNING id_reservation, reservation_start_date, reservation_end_date, id_tenant, id_accomodation
            """;

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, request.getReservationStartDate());
            ps.setObject(2, request.getReservationEndDate());
            ps.setInt(3, request.getIdTenant());
            ps.setInt(4, request.getIdAccomodation());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Reservation created successfully for Tenant ID: {}", request.getIdTenant());
                    return mapResultSetToResponseDTO(rs);
                }
            }

        } catch (SQLException exception) {
            log.error("Error creating reservation: {}", exception.getMessage());
            throw new RuntimeException("SQLException: ", exception);
        }
        throw new RuntimeException("Failed to create reservation");
    }

    @Override
    public List<ReservationResponseDTO> findAll() {
        String sql = "SELECT * FROM reservation";
        List<ReservationResponseDTO> reservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapResultSetToResponseDTO(rs));
            }

        } catch (SQLException ex) {
            log.error("Error finding all reservations: {}", ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        return reservations;
    }

    @Override
    public Optional<ReservationResponseDTO> findById(Integer idReservation) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToResponseDTO(rs));
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding reservation ID {}: {}", idReservation, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        return Optional.empty();
    }

    @Override
    public ReservationResponseDTO update(Integer id, ReservationRequestDTO request) {
        String sql = """
            UPDATE reservation 
            SET reservation_start_date = ?, 
                reservation_end_date = ?, 
                id_tenant = ?, 
                id_accomodation = ?
            WHERE id_reservation = ? 
            RETURNING id_reservation, reservation_start_date, reservation_end_date, id_tenant, id_accomodation
            """;

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, request.getReservationStartDate());
            ps.setObject(2, request.getReservationEndDate());
            ps.setInt(3, request.getIdTenant());
            ps.setInt(4, request.getIdAccomodation());
            ps.setInt(5, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Reservation updated successfully - ID: {}", id);
                    return mapResultSetToResponseDTO(rs);
                }
            }
        } catch (SQLException ex) {
            log.error("Error updating reservation ID {}: {}", id, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        throw new RuntimeException("Reservation not found with ID: " + id);
    }

    @Override
    public boolean deleteById(Integer idReservation) {
        String sql = "DELETE FROM reservation WHERE id_reservation = ?";

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            //executeUpdate se funziona ritorna il numero di righe affette
            //quindi se è > 0 significa che ha cancellato qualcosa
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            log.error("Error deleting reservation ID {}: {}", idReservation, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
    }

    // ========= UTILITY ============= //
    private ReservationResponseDTO mapResultSetToResponseDTO(ResultSet rs) throws SQLException {
        return new ReservationResponseDTO(
            rs.getInt("id_reservation"),
            rs.getObject("reservation_start_date", LocalDate.class),
            rs.getObject("reservation_end_date", LocalDate.class),
            rs.getInt("id_tenant"),
            rs.getInt("id_accomodation")
        );
    }
}