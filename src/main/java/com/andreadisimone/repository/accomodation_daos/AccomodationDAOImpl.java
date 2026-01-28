package com.andreadisimone.repository.accomodation_daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Host;
import com.andreadisimone.util.DatabaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationDAOImpl implements AccomodationDAO {

    @Override
    public Accomodation create(Accomodation accomodation) {
        String sql = "INSERT INTO accomodation(accomodation_name, n_rooms, accomodation_address, id_host, n_bed_places, floor, starter_date, end_date, price) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_accomodation";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accomodation.getName());
            ps.setInt(2, accomodation.getNRooms());
            ps.setString(3, accomodation.getAccomodationAddress());
            ps.setInt(4, accomodation.getHost().getIdHost());
            ps.setInt(5, accomodation.getNBedPlaces());
            ps.setInt(6, accomodation.getFloor());
            ps.setDate(7, Date.valueOf(accomodation.getStartDate()));
            ps.setDate(8, Date.valueOf(accomodation.getEndDate()));
            ps.setInt(9, accomodation.getPrice());
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                accomodation.setIdAccomodation((rs.getInt("id")));
            }}
            
        } catch (SQLException exception) {
            log.error("Error during the creation of the accomodation: {}", accomodation.getName(), exception);
            throw new RuntimeException("SQLException: ", exception);
        }

        log.info("Accomodation created successfully - ID: {}, Email: {}", accomodation.getIdAccomodation(), accomodation.getAccomodationAddress());
        return accomodation;
    }

    @Override
    public List<Accomodation> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Accomodation> findAllHostAccomodations(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Accomodation> findById(Integer idAccomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Accomodation> update(Accomodation accomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteAllHostAccomodations(Host idHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(Integer idAccomodation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
