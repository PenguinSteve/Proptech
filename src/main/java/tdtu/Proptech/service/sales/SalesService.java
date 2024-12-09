package tdtu.Proptech.service.sales;

import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.request.UploadSoldPropertyRequest;

import java.util.List;


public interface SalesService {
    List<Sales> getPendingSoldProperties();
    List<Sales> getSoldProperties();
    Sales uploadSoldProperty(String userEmail, UploadSoldPropertyRequest request);
    Sales updatePendingSoldProperty(Long propertyId, String status);
    Sales getSalesByPropertyId(Long propertyId);

    SalesDTO convertSalesToSalesDTO(Sales sales);
    List<SalesDTO> convertListSalesToListSalesDTO(List<Sales> sales);
}