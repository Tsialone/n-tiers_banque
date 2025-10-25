package com.example.services;

import com.example.dto.DeviseDto;
import com.example.remotes.ChangeServiceRemote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Singleton;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Remote(ChangeServiceRemote.class)
public class ChangeService implements ChangeServiceRemote {

    private final List<DeviseDto> devises = new ArrayList<>();

    @PostConstruct
    private void init() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("devises.json")) {

            if (is == null) {
                System.err.println("⚠️ devises.json introuvable dans le classpath !");
                return;
            }

            String jsonTxt = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            List<DeviseDto> loaded = gson.fromJson(jsonTxt, new TypeToken<List<DeviseDto>>() {
            }.getType());
            devises.addAll(loaded);

            System.out.println("✅ " + devises.size() + " devises chargées avec Gson");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des devises, le bean sera initialisé vide");
        }
    }

    @Override
    public DeviseDto addDevise(DeviseDto devise) {
        devises.add(devise);
        return devise;
    }

    @Override
    public List<DeviseDto> getAllDevises() {
        return new ArrayList<>(devises);
    }

    @Override
    public DeviseDto getDevise(Long id) {
        return devises.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public DeviseDto updateDevise(Long id, DeviseDto updated) {
        for (int i = 0; i < devises.size(); i++) {
            if (devises.get(i).getId().equals(id)) {
                devises.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    @Override
    public boolean deleteDevise(Long id) {
        return devises.removeIf(d -> d.getId().equals(id));
    }

    @Override
    public DeviseDto getByDateBtw(LocalDate transactionDate, String devise) {
        for (DeviseDto dto : devises) {
            LocalDate dateDebut = LocalDate.parse(dto.getDateDebut());
            LocalDate dateFin;

            if (dto.getDateFin() != null && !dto.getDateFin().isEmpty()) {
                dateFin = LocalDate.parse(dto.getDateFin());
            } else {
                dateFin = dateDebut.plusYears(10);
            }

            if (!transactionDate.isBefore(dateDebut) &&
                    !transactionDate.isAfter(dateFin) &&
                    dto.getLibelle().equalsIgnoreCase(devise)) {
                return dto;
            }
        }
        return null;
    }

}
