package com.andreadisimone.repository.host_daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.dtos.host_dtos.HostRequestDTO;
import com.andreadisimone.dtos.host_dtos.HostResponseDTO;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostDAOImpl implements HostDAO {

    @Override
    public HostResponseDTO create(HostRequestDTO request) {
        String sql = """
            INSERT INTO host(is_superhost, email, host_name, surname, host_address) 
            VALUES(?, ?, ?, ?, ?) 
            RETURNING id_host, is_superhost, email, host_name, surname, host_address
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBoolean(1, request.isSuperhost());
            ps.setString(2, request.getEmail());
            ps.setString(3, request.getHostName());
            ps.setString(4, request.getSurname());
            ps.setString(5, request.getHostAddress());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Host created successfully - Email: {}", request.getEmail());
                    return mapResultSetToResponseDTO(rs);
                }
            }

        } catch (SQLException exception) {
            log.error("Error during the creation of the host: {}", request.getEmail(), exception);
            throw new RuntimeException("SQLException: ", exception);
        }

        throw new RuntimeException("Failed to create host");
    }

    @Override
    public List<HostResponseDTO> findAll() {
        String sql = "SELECT * FROM host ORDER BY id_host desc";
        List<HostResponseDTO> hosts = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                hosts.add(mapResultSetToResponseDTO(rs));
            }

        } catch (SQLException ex) {
            log.error("Error finding all hosts: {}", ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        log.info("Found {} hosts", hosts.size());
        return hosts;
    }

    @Override
    public Optional<HostResponseDTO> findById(Integer idHost) {
        String sql = "SELECT * FROM host WHERE id_host = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idHost);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToResponseDTO(rs));
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding host with ID {}: {}", idHost, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        log.debug("No host found with ID {}", idHost);
        return Optional.empty();
    }

    @Override
    public HostResponseDTO getBestHost() {

        String sql = """
        SELECT 
            h.*,
            ROUND(AVG(f.points), 2) AS avg_feedback,
            COUNT(f.id_feed) AS n_feedback
        FROM host h
        JOIN accomodation a ON h.id_host = a.id_host
        JOIN reservation r ON a.id_accomodation = r.id_accomodation
        JOIN feedback f ON r.id_reservation = f.id_reservation
        GROUP BY h.id_host
        HAVING COUNT(f.id_feed) > 0
        ORDER BY avg_feedback DESC
        LIMIT 1
        """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    HostResponseDTO host = new HostResponseDTO();

                    host.setIdHost(rs.getInt("id_host"));
                    host.setHostName(rs.getString("host_name"));
                    host.setSurname(rs.getString("surname"));
                    host.setEmail(rs.getString("email"));
                    host.setHostAddress(rs.getString("host_address"));
                    host.setSuperhost(rs.getBoolean("is_superhost"));
                    host.setAvgFeedback(rs.getDouble("avg_feedback"));
                    host.setNFeedback(rs.getInt("n_feedback"));

                    
                    return host;
                }
            }

        } catch (SQLException ex) {
            log.error("Error finding best host: {}", ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        throw new HostNotFoundException("No hosts with feedback found");
    }

    @Override
    public List<HostResponseDTO> getAllSuperHosts() {
        String sql = """
        SELECT *
        FROM host
        WHERE is_superhost = TRUE
        ORDER BY id_host
        """;
        List<HostResponseDTO> superHosts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HostResponseDTO host = new HostResponseDTO();
                host.setIdHost(rs.getInt("id_host"));
                host.setHostName(rs.getString("host_name"));
                host.setSurname(rs.getString("surname"));
                host.setEmail(rs.getString("email"));
                host.setHostAddress(rs.getString("host_address"));
                host.setSuperhost(rs.getBoolean("is_superhost"));

                superHosts.add(host);
            }

        } catch (SQLException ex) {
            log.error("Error finding super hosts", ex);
            throw new RuntimeException("SQLException: ", ex);
        }
        return superHosts;
    }

    @Override
    public HostResponseDTO update(Integer id, HostResponseDTO request) {
        String sql = """
            UPDATE host 
            SET is_superhost = ?, 
                email = ?, 
                host_name = ?, 
                surname = ?,
                host_address = ?
            WHERE id_host = ? 
            RETURNING id_host, is_superhost, email, host_name, surname, host_address
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBoolean(1, request.isSuperhost());
            ps.setString(2, request.getEmail());
            ps.setString(3, request.getHostName());
            ps.setString(4, request.getSurname());
            ps.setString(5, request.getHostAddress());
            ps.setInt(6, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Host updated successfully - ID: {}", id);
                    return mapResultSetToResponseDTO(rs);
                }
            }

        } catch (SQLException ex) {
            log.error("Error updating host with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        throw new RuntimeException("Host not found with ID: " + id);
    }

    @Override
    public boolean deleteById(Integer idHost) {
        String sql = "DELETE FROM host WHERE id_host = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idHost);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Host deleted successfully - ID: {}", idHost);
                return true;
            }

        } catch (SQLException ex) {
            log.error("Error deleting host with ID {}: {}", idHost, ex.getMessage(), ex);
            throw new RuntimeException("SQLException: ", ex);
        }

        log.debug("No host found to delete with ID {}", idHost);
        return false;
    }

    // =========UTILITY============= //
    private HostResponseDTO mapResultSetToResponseDTO(ResultSet rs) throws SQLException {
        HostResponseDTO dto = new HostResponseDTO();
        dto.setIdHost(rs.getInt("id_host"));
        dto.setSuperhost(rs.getBoolean("is_superhost"));
        dto.setEmail(rs.getString("email"));
        dto.setHostName(rs.getString("host_name"));
        dto.setSurname(rs.getString("surname"));
        dto.setHostAddress(rs.getString("host_address"));
        return dto;
    }
}
