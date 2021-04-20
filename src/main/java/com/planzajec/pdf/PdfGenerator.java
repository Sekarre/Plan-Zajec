package com.planzajec.pdf;

import com.planzajec.services.PlanZajec;

public interface PdfGenerator {
    void drawTable(final String PDF_PATH, PlanZajec planZajec) throws Exception;
}
