package advertising.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import advertising.dao.CN_AdvertisingDao;
import advertising.model.CN_Advertising;

import java.util.List;

@Service
public class CN_AdvertisingService {
    @Autowired
    private CN_AdvertisingDao advertisingDao;

    // Create a new CN_Advertising
    public void createAdvertising(CN_Advertising advertising) {
        advertisingDao.insert(advertising);
    }

    // Update an existing CN_Advertising
    public void updateAdvertising(CN_Advertising advertising) {
        advertisingDao.update(advertising);
    }

    // Delete a CN_Advertising by ID
    public void deleteAdvertising(int advertisingId) {
        advertisingDao.delete(advertisingId);
    }

    // Find a CN_Advertising by ID
    public CN_Advertising findAdvertisingById(int advertisingId) {
        return advertisingDao.findById(advertisingId);
    }

    // Find all CN_Advertisings
    public List<CN_Advertising> findAllAdvertisings() {
        return advertisingDao.findAll();
    }
    
    //0902 추가 - cafeid에 해당하는 홍보가져오기
    public List<CN_Advertising> findAdvertisingsByCafeId(String cafeId) {
        return advertisingDao.findAdvertisingsByCafeId(cafeId);
    }
}
