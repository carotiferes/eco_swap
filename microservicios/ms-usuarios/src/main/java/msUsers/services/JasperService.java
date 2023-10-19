package msUsers.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.OrdenDeEnvio;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Collections;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JasperService {

    private static final String PATH_TEMPLATE_INVOICE = "reports/ordenEnvio.jrxml";
/*
    public byte[] getInvoice(OrdenDeEnvio transactionPdfData) throws Exception {
        log.info(">> Building INVONCE VOUCHER TEMPLATE");
        return getInvoiceReport(transactionPdfData, PATH_TEMPLATE_INVOICE);
    }
/*
    public byte[] getInvoiceReport(OrdenDeEnvio transactionPdfData, String pathTypeInvoice) throws Exception {
        try {
            Resource resource = new ClassPathResource(pathTypeInvoice);
            InputStream inputStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            InvoiceReportModel data = InvoiceReportModel.fromDomain(transactionPdfData);
            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(Collections.singleton(data));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Error generate pdf since JasperReport: {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new Exception("Error durante la construcción del reporte");
        }
    }

 */

}