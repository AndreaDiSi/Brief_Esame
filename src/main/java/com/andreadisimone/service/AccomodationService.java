package com.andreadisimone.service;
import java.time.LocalDate;
import java.util.List;

import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Host;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAO;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationService {
    
    //dependency inversion per principi SOLID
    // passo l'interfaccia come attributo nello stato
    // inzializzo l'implementazione dell interfaccia assegnandolo all'interfaccia
    private final AccomodationDAO accomodationDao;

    public AccomodationService(){
        accomodationDao = new AccomodationDAOImpl();
    }

    public Accomodation insertAccomodation(String name, Integer nRooms, String address, Host idHost, Integer nBedPlaces, Integer floor, LocalDate starterDate, LocalDate endDate, Integer price){
        log.info("Trying to insert Accomodation - name: {}", name);

        Accomodation accomodation2save = new Accomodation(name, nRooms, address, idHost, nBedPlaces, floor, starterDate, endDate, price);


        return accomodationDao.create(accomodation2save);
    }

    public List<Accomodation> getAllAccomodation(){
        log.info("Trying to get all Accomodations");

        return accomodationDao.findAll();
    };

}
