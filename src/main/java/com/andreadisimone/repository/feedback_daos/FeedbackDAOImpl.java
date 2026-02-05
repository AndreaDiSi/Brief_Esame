package com.andreadisimone.repository.feedback_daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.feedback_dtos.FeedbackRequestDTO;
import com.andreadisimone.dtos.feedback_dtos.FeedbackResponseDTO;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeedbackDAOImpl implements FeedbackDAO {

    @Override
    public FeedbackResponseDTO create(FeedbackRequestDTO request) {
        String sql = """
            INSERT INTO feedback(title, text_feedback, points, id_reservation) 
            VALUES(?, ?, ?, ?) 
            RETURNING id_feed, title, text_feedback, points, id_reservation
            """;

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.getTitle());
            ps.setString(2, request.getTextFeedback());
            ps.setInt(3, request.getPoints());
            ps.setInt(4, request.getIdReservation());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Feedback created successfully for Reservation ID: {}", request.getIdReservation());
                    return mapResultSetToResponseDTO(rs);
                }
            }

        } catch (SQLException exception) {
            log.error("Error creating feedback: {}", exception.getMessage());
            throw new RuntimeException("SQLException: ", exception);
        }
        throw new RuntimeException("Failed to create feedback");
    }

    @Override
    public List<FeedbackResponseDTO> findAll() {
        String sql = "SELECT * FROM feedback";
        List<FeedbackResponseDTO> feedbacks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                feedbacks.add(mapResultSetToResponseDTO(rs));
            }

        } catch (SQLException ex) {
            log.error("Error finding all feedbacks: {}", ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        return feedbacks;
    }

    @Override
    public Optional<FeedbackResponseDTO> findById(Integer idFeed) {
        String sql = "SELECT * FROM feedback WHERE id_feed = ?";

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idFeed);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToResponseDTO(rs));
                }
            }
        } catch (SQLException ex) {
            log.error("Error finding feedback ID {}: {}", idFeed, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        return Optional.empty();
    }

    @Override
    public FeedbackResponseDTO update(Integer id, FeedbackRequestDTO request) {
        String sql = """
            UPDATE feedback 
            SET title = ?, 
                text_feedback = ?, 
                points = ?, 
                id_reservation = ?
            WHERE id_feed = ? 
            RETURNING id_feed, title, text_feedback, points, id_reservation
            """;

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.getTitle());
            ps.setString(2, request.getTextFeedback());
            ps.setInt(3, request.getPoints());
            ps.setInt(4, request.getIdReservation());
            ps.setInt(5, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Feedback updated successfully - ID: {}", id);
                    return mapResultSetToResponseDTO(rs);
                }
            }
        } catch (SQLException ex) {
            log.error("Error updating feedback ID {}: {}", id, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
        throw new RuntimeException("Feedback not found with ID: " + id);
    }

    @Override
    public boolean deleteById(Integer idFeed) {
        String sql = "DELETE FROM feedback WHERE id_feed = ?";

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idFeed);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            log.error("Error deleting feedback ID {}: {}", idFeed, ex.getMessage());
            throw new RuntimeException("SQLException: ", ex);
        }
    }

    // ========= UTILITY ============= //
    private FeedbackResponseDTO mapResultSetToResponseDTO(ResultSet rs) throws SQLException {
        return new FeedbackResponseDTO(
            rs.getInt("id_feed"),
            rs.getString("title"),
            rs.getString("text_feedback"),
            rs.getInt("points"),
            rs.getInt("id_reservation")
        );
    }
}