package tdtu.Proptech.service.sales;

import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.request.UploadSoldPropertyRequest;

import java.util.List;


public interface SalesService {
    List<Property> getPendingSoldProperties();
    List<Sales> getSoldProperties();
    Sales uploadSoldProperty(String userEmail, UploadSoldPropertyRequest request);
    Sales updatePendingSoldProperty(Long id, String status);
}