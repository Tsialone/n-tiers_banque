package com.example.services;

import com.example.dto.DeviseDto;
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
@Remote(ChangeServiceRemote.class)
public class ChangeService implements ChangeServiceRemote {

    private final List<DeviseDto> devises = new ArrayList<>();

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

    // @Override 
    // public void test ()
    // {
    //     System.out.println("test");
    // }
    @Override
    public DeviseDto addDevise(DeviseDto devise) {
        devises.add(devise);
        return devise;
    }


    @Override
    public List<DeviseDto> getDistinct (){
        reloadDevises();
        List<DeviseDto> resp = new ArrayList<>();
        List<String> dejaVue = new ArrayList<>();
        for (DeviseDto deviseDto : getAllDevises()) {
            if (!dejaVue.contains(deviseDto.getLibelle())){
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
    public DeviseDto getDevise(Long id) {
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
    //     reloadDevises();
    // }

    @Override
    public DeviseDto updateDevise(Long id, DeviseDto updated) {
        reloadDevises();
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
