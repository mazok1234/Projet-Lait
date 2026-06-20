package com.example.demo.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Aliments;
import com.example.demo.entity.Fournisseurs;
import com.example.demo.entity.MouvementStockFille;
import com.example.demo.entity.MouvementsStock;
import com.example.demo.entity.TypeMouvement;
import com.example.demo.repository.AlimentsRepository;
import com.example.demo.repository.FournisseursRepository;
import com.example.demo.repository.MouvementStockFilleRepository;
import com.example.demo.repository.MouvementsStockRepository;
import com.example.demo.repository.StockActuelProjection;
import com.example.demo.repository.TypeMouvementRepository;

@Service
public class StockAlimentaireService {
    public static final String TYPE_APPROVISIONNEMENT = "Approvisionnement";
    public static final String TYPE_CONSOMMATION = "Consommation";

    private final AlimentsRepository alimentsRepository;
    private final FournisseursRepository fournisseursRepository;
    private final TypeMouvementRepository typeMouvementRepository;
    private final MouvementsStockRepository mouvementsStockRepository;
    private final MouvementStockFilleRepository mouvementStockFilleRepository;

    public StockAlimentaireService(AlimentsRepository alimentsRepository,
            FournisseursRepository fournisseursRepository,
            TypeMouvementRepository typeMouvementRepository,
            MouvementsStockRepository mouvementsStockRepository,
            MouvementStockFilleRepository mouvementStockFilleRepository) {
        this.alimentsRepository = alimentsRepository;
        this.fournisseursRepository = fournisseursRepository;
        this.typeMouvementRepository = typeMouvementRepository;
        this.mouvementsStockRepository = mouvementsStockRepository;
        this.mouvementStockFilleRepository = mouvementStockFilleRepository;
    }

    public List<Fournisseurs> rechercherFournisseurs(String q) {
        if (isBlank(q)) {
            return fournisseursRepository.findAllByOrderByNomAsc();
        }
        return fournisseursRepository.findByNomContainingIgnoreCaseOrderByNomAsc(q.trim());
    }

    public Fournisseurs getFournisseur(Integer id) {
        return fournisseursRepository.findById(id).orElseThrow();
    }

    public Fournisseurs saveFournisseur(Fournisseurs fournisseur) {
        return fournisseursRepository.save(fournisseur);
    }

    public void deleteFournisseur(Integer id) {
        fournisseursRepository.deleteById(id);
    }

    public List<Aliments> rechercherAliments(String q) {
        if (isBlank(q)) {
            return alimentsRepository.findAllByOrderByNomAsc();
        }
        return alimentsRepository.findByNomContainingIgnoreCaseOrderByNomAsc(q.trim());
    }

    public Aliments getAliment(Integer id) {
        return alimentsRepository.findById(id).orElseThrow();
    }

    public Aliments saveAliment(Aliments aliment) {
        if (aliment.getSeuilStockMin() == null) {
            aliment.setSeuilStockMin(0.0);
        }
        return alimentsRepository.save(aliment);
    }

    public void deleteAliment(Integer id) {
        alimentsRepository.deleteById(id);
    }

    public List<TypeMouvement> getTypesMouvement() {
        ensureTypesMouvement();
        return typeMouvementRepository.findAll();
    }

    public List<MouvementStockFille> rechercherApprovisionnements(
            LocalDate dateDebut,
            LocalDate dateFin,
            Integer fournisseurId,
            String q) {
        return mouvementStockFilleRepository.rechercher(TYPE_APPROVISIONNEMENT,
                startOfDay(dateDebut), endOfDay(dateFin), null, fournisseurId, normalize(q));
    }

    public List<MouvementStockFille> rechercherConsommations(
            LocalDate dateDebut,
            LocalDate dateFin,
            Integer alimentId,
            String q) {
        return mouvementStockFilleRepository.rechercher(TYPE_CONSOMMATION,
                startOfDay(dateDebut), endOfDay(dateFin), alimentId, null, normalize(q));
    }

    public List<MouvementsStock> rechercherMouvements(LocalDate dateDebut, LocalDate dateFin, Integer typeId) {
        return mouvementsStockRepository.rechercher(startOfDay(dateDebut), endOfDay(dateFin), typeId);
    }

    public MouvementsStock getMouvement(Integer id) {
        return mouvementsStockRepository.findById(id).orElseThrow();
    }

    public List<MouvementStockFille> getLignesMouvement(Integer mouvementId) {
        return mouvementStockFilleRepository.findByMouvementStockId(mouvementId);
    }

    @Transactional
    public void enregistrerApprovisionnement(Integer alimentId,
            Integer fournisseurId,
            Double quantite,
            Double prixUnitaire,
            LocalDate dateMouvement) {
        MouvementsStock mouvement = creerMouvement(TYPE_APPROVISIONNEMENT, dateMouvement);
        mouvement.setFournisseur(getFournisseur(fournisseurId));
        mouvement.setMotif("Approvisionnement");
        mouvementsStockRepository.save(mouvement);

        MouvementStockFille ligne = creerLigne(mouvement, alimentId, quantite);
        ligne.setPrixUnitaire(prixUnitaire);
        mouvementStockFilleRepository.save(ligne);
    }

    @Transactional
    public void enregistrerConsommation(Integer alimentId,
            Integer bovinId,
            Double quantite,
            LocalDate dateMouvement) {
        MouvementsStock mouvement = creerMouvement(TYPE_CONSOMMATION, dateMouvement);
        mouvement.setMotif("Consommation");
        mouvementsStockRepository.save(mouvement);

        MouvementStockFille ligne = creerLigne(mouvement, alimentId, quantite);
        ligne.setBovinId(bovinId);
        mouvementStockFilleRepository.save(ligne);
    }

    public List<StockActuelProjection> getStockActuel(String q) {
        return alimentsRepository.findStockActuel(normalize(q));
    }

    public List<StockActuelProjection> getStockActuel(String q, LocalDate dateEtat) {
        return getStockActuel(q, dateEtat, dateEtat);
    }

    public List<StockActuelProjection> getStockActuel(String q, LocalDate du, LocalDate au) {
        if (du == null && au == null) {
            return getStockActuel(q);
        }

        LocalDate dateFin = au != null ? au : du;
        return alimentsRepository.findStockActuelByDate(normalize(q), endOfDay(dateFin));
    }

    public List<StockActuelProjection> getStockActuel(String q, String etat) {
        List<StockActuelProjection> stock = getStockActuel(q);
        if (isBlank(etat) || "TOUS".equalsIgnoreCase(etat)) {
            return stock;
        }

        if ("BAS".equalsIgnoreCase(etat)) {
            return stock.stream().filter(this::isStockBas).toList();
        }

        if ("OK".equalsIgnoreCase(etat)) {
            return stock.stream().filter(ligne -> !isStockBas(ligne)).toList();
        }

        return stock;
    }

    public List<StockActuelProjection> getStockActuel(String q, String etat, LocalDate dateEtat) {
        return getStockActuel(q, etat, dateEtat, dateEtat);
    }

    public List<StockActuelProjection> getStockActuel(String q, String etat, LocalDate du, LocalDate au) {
        List<StockActuelProjection> stock = getStockActuel(q, du, au);
        if (isBlank(etat) || "TOUS".equalsIgnoreCase(etat)) {
            return stock;
        }

        if ("BAS".equalsIgnoreCase(etat)) {
            return stock.stream().filter(this::isStockBas).toList();
        }

        if ("OK".equalsIgnoreCase(etat)) {
            return stock.stream().filter(ligne -> !isStockBas(ligne)).toList();
        }

        return stock;
    }

    public List<StockActuelProjection> getAlertesStockBas() {
        return alimentsRepository.findAlertesStockBas();
    }

    private MouvementsStock creerMouvement(String typeLibelle, LocalDate dateMouvement) {
        MouvementsStock mouvement = new MouvementsStock();
        mouvement.setType(getOrCreateType(typeLibelle));
        mouvement.setDateMvt(Timestamp.valueOf(dateOrToday(dateMouvement).atStartOfDay()));
        return mouvement;
    }

    private MouvementStockFille creerLigne(MouvementsStock mouvement, Integer alimentId, Double quantite) {
        MouvementStockFille ligne = new MouvementStockFille();
        ligne.setMouvementStock(mouvement);
        ligne.setAliment(getAliment(alimentId));
        ligne.setQuantite(quantite);
        return ligne;
    }

    private void ensureTypesMouvement() {
        getOrCreateType(TYPE_APPROVISIONNEMENT);
        getOrCreateType(TYPE_CONSOMMATION);
    }

    private TypeMouvement getOrCreateType(String libelle) {
        return typeMouvementRepository.findByLibelleIgnoreCase(libelle)
                .orElseGet(() -> {
                    TypeMouvement type = new TypeMouvement();
                    type.setLibelle(libelle);
                    return typeMouvementRepository.save(type);
                });
    }

    private LocalDate dateOrToday(LocalDate value) {
        return value == null ? LocalDate.now() : value;
    }

    private Timestamp startOfDay(LocalDate value) {
        if (value == null) {
            return Timestamp.valueOf(LocalDateTime.of(1900, 1, 1, 0, 0));
        }
        return Timestamp.valueOf(LocalDateTime.of(value, LocalTime.MIN));
    }

    private Timestamp endOfDay(LocalDate value) {
        if (value == null) {
            return Timestamp.valueOf(LocalDateTime.of(2999, 12, 31, 23, 59, 59));
        }
        return Timestamp.valueOf(LocalDateTime.of(value, LocalTime.MAX));
    }

    private String normalize(String value) {
        return isBlank(value) ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isStockBas(StockActuelProjection ligne) {
        double stock = ligne.getStock() == null ? 0.0 : ligne.getStock();
        double seuil = ligne.getSeuilStockMin() == null ? 0.0 : ligne.getSeuilStockMin();
        return stock <= seuil;
    }
}
