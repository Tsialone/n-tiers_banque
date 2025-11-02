package com.example.services;

import com.example.change_dtos.DeviseDto;
import com.example.remotes.ChangeServiceRemote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Stateless;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
@Remote(ChangeServiceRemote.class)
public class ChangeService implements ChangeServiceRemote {

    private final List<DeviseDto> devises = new ArrayList<>();

    private final double percentCheck = 20;

    // private CompteCourantServiceRemote compteCourantServiceRemote;
    // @PostConstruct
    // private void initRemoteEJB() {
    // try {
    // Hashtable<String, Object> jndiProps = new Hashtable<>();
    // jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,
    // "org.wildfly.naming.client.WildFlyInitialContextFactory");
    // jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:8080");

    // // Ajout de l'authentification
    // jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
    // jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

    // Context ctx = new InitialContext(jndiProps);

    // compteCourantServiceRemote = (CompteCourantServiceRemote) ctx
    // .lookup("change-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote");

    // System.out.println("ChangeServiceRemote initialisé avec succès !");
    // } catch (Exception e) {
    // System.err.println("Erreur lors de l'initialisation de
    // CompteCourantServiceRemote:");
    // e.printStackTrace();
    // }
    // }

    @PostConstruct
    private void init() {
        File jsonFile = new File("/opt/devises.json");
        // try (InputStream is =
        // getClass().getClassLoader().getResourceAsStream("/opt/devises.json")) {
        try (InputStream is = new FileInputStream(jsonFile)) {

            // if (is == null) {
            // System.err.println("devises.json introuvable dans le classpath !");
            // return;
            // }

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
    public DeviseDto annulerDevise(Long id) throws Exception {
        reloadDevises();

        DeviseDto devise = getById(id);
        if (devise == null) {
            throw new Exception("Devise introuvable avec id=" + id);
        }

        devise.setValide(false);
        devise.setDateValidation(LocalDate.now().toString());

        saveDevises();
        return devise;
    }
    @Override
    public DeviseDto validerDevise(Long id) throws Exception {
        reloadDevises();

        DeviseDto devise = getById(id);
        if (devise == null) {
            throw new Exception("Devise introuvable avec id=" + id);
        }

        devise.setValide(true);
        devise.setDateValidation(LocalDate.now().toString());
        System.out.println("validation avec succees");
        saveDevises();
        return devise;
    }

    @Override
    public DeviseDto getLastDeviseDto(String libelle, LocalDate date, Long excludeId) {
        reloadDevises();
        DeviseDto last = null;

        for (DeviseDto dto : devises) {
            if (!dto.getLibelle().equalsIgnoreCase(libelle))
                continue;
            if (excludeId != null && dto.getId().equals(excludeId))
                continue;

            LocalDate dateDebut = LocalDate.parse(dto.getDateDebut());
            // On prend la dernière dont la dateDébut est AVANT ou ÉGALE à la date donnée
            if (!dateDebut.isAfter(date)) {
                if (last == null || LocalDate.parse(last.getDateDebut()).isBefore(dateDebut)) {
                    last = dto;
                }
            }
        }

        return last;
    }

    @Override
    public DeviseDto addDevise(DeviseDto devise) throws Exception {
        DeviseDto lastDevise = getLastDeviseDto(devise.getLibelle(), devise.getDateDebutDate(), devise.getId());
        DeviseDto fetchIfExist = getByDateBtw(devise.getDateDebutDate(), devise.getLibelle());
        if (fetchIfExist != null)
            throw new Exception("Cette devise est deja presente: " + fetchIfExist.getDateDebut() + " - "
                    + fetchIfExist.getDateFin());
        if (lastDevise != null) {
            double lastAr = lastDevise.getArriary();

            double minAr = lastAr - (lastAr * percentCheck / 100);
            double maxAr = lastAr + (lastAr * percentCheck / 100);

            double newAr = devise.getArriary();

            if (newAr < minAr || newAr > maxAr) {
                throw new Exception(
                        "Variation du taux trop importante (" + percentCheck + "% max autorisés). " +
                                "Ancien: " + lastAr + ", Nouveau: " + newAr + " diff: +" + maxAr + " -" + minAr);
            }
        }
        devises.add(devise);
        saveDevises();
        return devise;
    }

    private void saveDevises() {
        try (Writer writer = new FileWriter("/opt/devises.json")) {
            new Gson().toJson(devises, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DeviseDto> getDistinct() {
        reloadDevises();
        List<DeviseDto> resp = new ArrayList<>();
        List<String> dejaVue = new ArrayList<>();
        for (DeviseDto deviseDto : getAllDevises()) {
            if (!dejaVue.contains(deviseDto.getLibelle())) {
                resp.add(deviseDto);
            }
            dejaVue.add(deviseDto.getLibelle());
        }
        return resp;
    }

    @Override
    public List<DeviseDto> getAllDevises() {
        reloadDevises();
        System.out.println("oiruejkloijygh");
        return new ArrayList<>(devises);
    }

    @Override
    public DeviseDto getById(Long id) {
        reloadDevises();
        return devises.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void reloadDevises() {
        devises.clear();
        File jsonFile = new File("/opt/devises.json");
        try (InputStream is = new FileInputStream(jsonFile)) {
            String jsonTxt = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            List<DeviseDto> loaded = new Gson().fromJson(
                    jsonTxt,
                    new TypeToken<List<DeviseDto>>() {
                    }.getType());
            devises.addAll(loaded);
            System.out.println("✅ Devises rechargées depuis JSON");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Schedule(hour = "*", minute = "*", second = "*/1", persistent = false)
    // public void refresh() {
    // reloadDevises();
    // }

    @Override
    public DeviseDto updateDevise(Long id, DeviseDto updated) throws Exception {
        reloadDevises();

        DeviseDto existing = getById(id);
        if (existing == null) {
            throw new Exception("La devise avec l'ID " + id + " n'existe pas.");
        }

        DeviseDto overlapping = getByDateBtw(updated.getDateDebutDate(), updated.getLibelle());
        if (overlapping != null && !overlapping.getId().equals(id)) {
            throw new Exception(
                    "Conflit détecté : une autre devise " + overlapping.getLibelle() +
                            " couvre déjà cette période (" + overlapping.getDateDebut() +
                            " - " + overlapping.getDateFin() + ").");
        }

        DeviseDto lastDevise = getLastDeviseDto(updated.getLibelle(), updated.getDateDebutDate(), id);
        System.out.println("last devise " + lastDevise.getId() + " et lui " + id);
        if (lastDevise != null && !lastDevise.getId().equals(id)) {
            double lastAr = lastDevise.getArriary();

            double minAr = lastAr - (lastAr * percentCheck / 100);
            double maxAr = lastAr + (lastAr * percentCheck / 100);

            double newAr = updated.getArriary();

            System.out.println("newAr: " + newAr);
            System.out.println("minAr: " + minAr);
            System.out.println("maxAr: " + maxAr);

            if (newAr < minAr || newAr > maxAr) {
                throw new Exception(
                        "Variation du taux trop importante (" + percentCheck + "% max autorisés). " +
                                "Ancien: " + lastAr + ", Nouveau: " + newAr + " diff: +" + maxAr + " -" + minAr);
            }
        }

        for (int i = 0; i < devises.size(); i++) {
            if (devises.get(i).getId().equals(id)) {
                devises.set(i, updated);
                saveDevises();
                return updated;
            }
        }

        return null;
    }

    @Override
    public boolean deleteDevise(Long id) {
        reloadDevises();
        return devises.removeIf(d -> d.getId().equals(id));
    }

    @Override
    public DeviseDto getByDateBtw(LocalDate transactionDate, String devise) {
        reloadDevises();
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
