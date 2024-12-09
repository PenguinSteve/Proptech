package tdtu.Proptech.service.sales;

import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.request.UploadSoldPropertyRequest;

import java.util.List;


public interface SalesService {
    List<Property> getPendingSoldProperties();
    List<Sales> getSoldProperties();
    Sales uploadSoldProperty(String userEmail, UploadSoldPropertyRequest request);
    Sales updatePendingSoldProperty(Long id, String status);
    Sales getSalesByPropertyId(Long id);

    SalesDTO convertSalesToSalesDTO(Sales sales);
    List<SalesDTO> convertListSalesToListSalesDTO(List<Sales> sales);
}