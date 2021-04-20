package com.planzajec.services;

import com.planzajec.dao.CzasTrwaniaLekcjiDao;
import com.planzajec.dao.GodzinaLekcyjnaDao;
import com.planzajec.dao.Impl.CzasTrwaniaLekcjiDaoImpl;
import com.planzajec.dao.Impl.GodzinaLekcyjnaDaoImpl;
import com.planzajec.entity.CzasTrwaniaLekcji;
import com.planzajec.entity.GodzinaLekcyjna;
import com.planzajec.entity.Klasa;
import com.planzajec.entity.Nauczyciel;
import com.planzajec.entity.enums.Dni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlanZajecImpl implements PlanZajec {


    private GodzinaLekcyjnaDao godzinaLekcyjnaDao;
    private CzasTrwaniaLekcjiDao czasTrwaniaLekcjiDao;


    private Map<String, List<GodzinaLekcyjna>> plan = new HashMap<>();
    private List<CzasTrwaniaLekcji> czasTrwaniaWszystkichLekcji;
    private String nazwa;

    public PlanZajecImpl(){
        fillWithNull();
        loadCzasTrwania();
    }

    public PlanZajecImpl(Klasa wybranaKlasa) {
        loadCzasTrwania();
        loadPlan(wybranaKlasa.getIdKlasa(), wybranaKlasa.getClass());
        this.nazwa = "Klasa: " + wybranaKlasa.getNazwa();
    }

    public PlanZajecImpl(Nauczyciel wybranyNauczyciel) {
        loadCzasTrwania();
        loadPlan(wybranyNauczyciel.getIdNauczyciel(), wybranyNauczyciel.getClass());
        this.nazwa = wybranyNauczyciel.getImie() + " " + wybranyNauczyciel.getNazwisko();
    }

    public Map<String, List<GodzinaLekcyjna>> getPlan() {
        return plan;
    }

    public void setPlan(Map<String, List<GodzinaLekcyjna>> plan) {
        this.plan = plan;
    }

    public List<CzasTrwaniaLekcji> getCzasTrwaniaWszystkichLekcji() {
        return czasTrwaniaWszystkichLekcji;
    }

    public void setCzasTrwaniaWszystkichLekcji(List<CzasTrwaniaLekcji> czasTrwaniaWszystkichLekcji) {
        this.czasTrwaniaWszystkichLekcji = czasTrwaniaWszystkichLekcji;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void addGodzinaLekcyjna(GodzinaLekcyjna godzinaLekcyjna) {
        if (godzinaLekcyjnaDao == null)
            godzinaLekcyjnaDao = new GodzinaLekcyjnaDaoImpl();
        this.plan.get(godzinaLekcyjna.getDzien().getNazwa()).set(godzinaLekcyjna.getCzasTrwaniaLekcji().getNumer() - 1, godzinaLekcyjna);
        godzinaLekcyjnaDao.saveOrUpdate(godzinaLekcyjna);
    }

    public void deleteGodzinaLekcyjna(GodzinaLekcyjna godzinaLekcyjna) {
        if (godzinaLekcyjnaDao == null)
            godzinaLekcyjnaDao = new GodzinaLekcyjnaDaoImpl();
        this.plan.get(godzinaLekcyjna.getDzien().getNazwa()).set(godzinaLekcyjna.getCzasTrwaniaLekcji().getNumer() - 1, null);
        godzinaLekcyjnaDao.delete(godzinaLekcyjna);
    }

    private void loadCzasTrwania() {
        if (czasTrwaniaLekcjiDao == null)
            czasTrwaniaLekcjiDao = new CzasTrwaniaLekcjiDaoImpl();
        this.czasTrwaniaWszystkichLekcji = czasTrwaniaLekcjiDao.findAllOrderBy("numer", true);
    }


    private void loadPlan(int id, Class c) {
        fillWithNull();
        if (godzinaLekcyjnaDao == null)
            godzinaLekcyjnaDao = new GodzinaLekcyjnaDaoImpl();
        List<GodzinaLekcyjna> godziny = godzinaLekcyjnaDao.findAll(id, c);
        for (GodzinaLekcyjna jednaGodzina : godziny) {
            if(this.plan.get(jednaGodzina.getDzien().getNazwa()) != null)
                this.plan.get(jednaGodzina.getDzien().getNazwa()).set(jednaGodzina.getCzasTrwaniaLekcji().getNumer() - 1, jednaGodzina);

        }
    }


    private void fillWithNull() {
        for (Dni dzien : Dni.values()) {
            this.plan.put(dzien.name(), new ArrayList<>());
            for (int i = 0; i < czasTrwaniaWszystkichLekcji.size(); i++) {
                this.plan.get(dzien.name()).add(null);
            }
        }
    }


    public void displayEachElement() {
        for (Map.Entry<String, List<GodzinaLekcyjna>> entry : plan.entrySet()) {
            String a = entry.getKey();
            List<GodzinaLekcyjna> b = entry.getValue();
            System.out.println("Key: " + a + ", Value: ");
            for (GodzinaLekcyjna x : b) {
                if(x == null)
                    System.out.print("");
                else
                    System.out.println(x.getCzasTrwaniaLekcji().toString());

            }
        }
    }



}
