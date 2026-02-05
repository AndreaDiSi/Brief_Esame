package com.andreadisimone.repository.accomodation_daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.exceptions.not_found.AccomodationNotFoundException;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.model.Host;
import com.andreadisimone.repository.host_daos.HostDAOImpl;
import com.andreadisimone.util.DatabaseConnection;
import com.andreadisimone.util.DateConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationDAOImpl implements AccomodationDAO {

    @Override
    public AccomodationResponseDTO create(AccomodationRequestDTO accomodation) {
        String sql = "INSERT INTO accomodation(accomodation_name, n_rooms, accomodation_address, id_host, n_bed_places, floor, starter_date, end_date, price) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
        AccomodationResponseDTO newAccomodationResponseDTO = new AccomodationResponseDTO();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, accomodation.getAccomodationName());
            ps.setInt(2, accomodation.getNRooms());
            ps.setString(3, accomodation.getAccomodationAddress());
            ps.setInt(4, accomodation.getHostId());
            ps.setInt(5, accomodation.getNBedPlaces());
            ps.setInt(6, accomodation.getFloor());
            ps.setDate(7, Date.valueOf(accomodation.getStartDate()));
            ps.setDate(8, Date.valueOf(accomodation.getEndDate()));
            ps.setInt(9, accomodation.getPrice());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return mapResultSetToAccomodtion(rs);
                } else {
                    throw new SQLException("Creating accomodation failed, no ID obtained.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                log.error("Accomodation creation execute query gone wrong");
            }

        } catch (SQLException exception) {
            log.error("Error during the creation of the accomodation: {}", newAccomodationResponseDTO.getAccomodationName(), exception);
            throw new RuntimeException("SQLException: ", exception);
        }

        log.info("Accomodation created successfully - ID: {}, ", newAccomodationResponseDTO.getIdAccomodation());

        return newAccomodationResponseDTO;
    }

    @Override
    public List<AccomodationResponseDTO> getAll() {
        String sql = "SELECT * FROM accomodation";
        List<AccomodationResponseDTO> accomodationsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("Connection to database done");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    accomodationsList.add(mapResultSetToAccomodtion(rs));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Accomodation getAll execute query gone wrong");
                throw new RuntimeException(ex);
            }
        } catch (SQLException ex) {
            log.error("Error during the connection to the db or during the creation of the get all Accomodations");
            throw new RuntimeException(ex);
        }
        return accomodationsList;
    }

    @Override
    public List<AccomodationResponseDTO> getAllHostAccomodations(Host idHost) {
        String sql = "SELECT * FROM accomodation WHERE id_host = ?";
        List<AccomodationResponseDTO> accomodationsList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            log.info("Connection to database done");
            try (ResultSet rs = ps.executeQuery()) {
                ps.setInt(1, idHost.getIdHost());
                while (rs.next()) {
                    accomodationsList.add(mapResultSetToAccomodtion(rs));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Accomodation getAll execute query gone wrong");
                throw new RuntimeException(ex);
            }
        } catch (SQLException ex) {
            log.error("Error during the connection to the db or during the creation of the get all Accomodations");
            throw new RuntimeException(ex);
        }
        return accomodationsList;
    }

    @Override
    public Optional<AccomodationResponseDTO> getById(Integer idAccomodation) {
        String sql = "SELECT * FROM accomodation WHERE id_accomodation = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idAccomodation);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccomodtion(rs));
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding accomodation with ID {}: {}", idAccomodation, ex.getMessage(), ex);
            throw new AccomodationNotFoundException("Accomodation not found with ID: " + idAccomodation);
        }
        return Optional.empty();
    }

    @Override
    public AccomodationResponseDTO getBestAccomodation() {
        String sql = """
        SELECT a.*, top_accomodation.n_reservations
        FROM accomodation a
        JOIN (
            SELECT id_accomodation, COUNT(*) as n_reservations
            FROM reservation
            GROUP BY id_accomodation
            ORDER BY n_reservations DESC
            LIMIT 1
        ) AS top_accomodation ON a.id_accomodation = top_accomodation.id_accomodation
        """;
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AccomodationResponseDTO accomodation = new AccomodationResponseDTO();
                    accomodation.setIdAccomodation(rs.getInt("id_accomodation"));
                    accomodation.setAccomodationName(rs.getString("accomodation_name"));
                    accomodation.setNRooms(rs.getInt("n_rooms"));
                    accomodation.setAccomodationAddress(rs.getString("accomodation_address"));
                    HostDAOImpl hostDAO = new HostDAOImpl();
                    //lambda expression, funzione senza parametri che torna un oggetto
                    accomodation.setHost(hostDAO.findById(rs.getInt("id_host")).orElseThrow(() -> new HostNotFoundException("Host not found")));
                    accomodation.setNBedPlaces(rs.getInt("n_bed_places"));
                    accomodation.setFloor(rs.getInt("floor"));
                    accomodation.setStartDate(DateConverter.date2LocalDate(rs.getDate("starter_date")));
                    accomodation.setEndDate(DateConverter.date2LocalDate(rs.getDate("end_date")));
                    accomodation.setPrice(rs.getInt("price"));
                    accomodation.setHostId(rs.getInt("id_host"));
                    accomodation.setNReservations(rs.getInt("n_reservations"));
                    return accomodation;
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding best accomodation ", ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        throw new AccomodationNotFoundException("No accomodations found");
    }

    //============DELETE==================//
    @Override
    public boolean deleteById(Integer idAccomodation) {
        String sql = "DELETE FROM accomodation WHERE id_accomodation = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idAccomodation);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            log.error("Error deleting accomodation with ID {}: {}", idAccomodation, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }
    }

    @Override
    public AccomodationResponseDTO update(AccomodationResponseDTO accomodation, Integer idAccomodation) {
        String sql = """
            UPDATE accomodation 
            SET accomodation_name = ?, 
                n_rooms = ?, 
                accomodation_address = ?, 
                id_host = ?,
                n_bed_places = ?, 
                floor = ?, 
                starter_date = ?, 
                end_date = ?, 
                price = ? 
            WHERE id_accomodation = ? 
            RETURNING id_accomodation, accomodation_name, n_rooms, accomodation_address, id_host, n_bed_places, floor, starter_date, end_date, price
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, accomodation.getAccomodationName());
            ps.setInt(2, accomodation.getNRooms());
            ps.setString(3, accomodation.getAccomodationAddress());
            ps.setInt(4, accomodation.getHostId());
            ps.setInt(5, accomodation.getNBedPlaces());
            ps.setInt(6, accomodation.getFloor());
            ps.setDate(7, Date.valueOf(accomodation.getStartDate()));
            ps.setDate(8, Date.valueOf(accomodation.getEndDate()));
            ps.setInt(9, accomodation.getPrice());
            ps.setInt(10, idAccomodation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccomodtion(rs);
                } else {
                    throw new AccomodationNotFoundException("Accomodation not found with ID: " + idAccomodation);
                }
            }

        } catch (SQLException ex) {
            log.error("Error updating accomodation with ID {}: {}", idAccomodation, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

    }

    @Override
    public int deleteAllHostAccomodations(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //=========UTILITY=============//
    private AccomodationResponseDTO mapResultSetToAccomodtion(ResultSet rs) throws SQLException {
        AccomodationResponseDTO accomodation = new AccomodationResponseDTO();
        accomodation.setIdAccomodation(rs.getInt("id_accomodation"));
        accomodation.setAccomodationName(rs.getString("accomodation_name"));
        accomodation.setNRooms(rs.getInt("n_rooms"));
        accomodation.setAccomodationAddress(rs.getString("accomodation_address"));
        HostDAOImpl hostDAO = new HostDAOImpl();
        //lambda expression, funzione senza parametri che torna un oggetto
        accomodation.setHost(hostDAO.findById(rs.getInt("id_host")).orElseThrow(() -> new HostNotFoundException("Host not found")));
        accomodation.setNBedPlaces(rs.getInt("n_bed_places"));
        accomodation.setFloor(rs.getInt("floor"));
        accomodation.setStartDate(DateConverter.date2LocalDate(rs.getDate("starter_date")));
        accomodation.setEndDate(DateConverter.date2LocalDate(rs.getDate("end_date")));
        accomodation.setPrice(rs.getInt("price"));
        accomodation.setHostId(rs.getInt("id_host"));
        return accomodation;
    }

}
