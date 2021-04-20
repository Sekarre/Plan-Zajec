package com.planzajec.services;

import com.planzajec.entity.CzasTrwaniaLekcji;
import com.planzajec.entity.GodzinaLekcyjna;

import java.util.List;
import java.util.Map;


public interface PlanZajec {

    void addGodzinaLekcyjna(GodzinaLekcyjna godzinaLekcyjna);

    void deleteGodzinaLekcyjna(GodzinaLekcyjna godzinaLekcyjna);

    String getNazwa();

    Map<String, List<GodzinaLekcyjna>> getPlan();

    List<CzasTrwaniaLekcji> getCzasTrwaniaWszystkichLekcji();
}
