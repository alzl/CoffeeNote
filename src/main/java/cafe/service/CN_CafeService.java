package cafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cafe.dao.CN_CafeDao;
import cafe.model.CN_Cafe;

import java.util.List;

@Service
public class CN_CafeService {
    @Autowired
    private CN_CafeDao cafeDao;

    // Create a new CN_Cafe
    public void createCafe(CN_Cafe cafe) {
        cafeDao.insert(cafe);
    }

    // Update an existing CN_Cafe
    public void updateCafe(CN_Cafe cafe) {
        cafeDao.update(cafe);
    }

    // Delete a CN_Cafe by ID
    public void deleteCafe(String cafeId) {
        cafeDao.delete(cafeId);
    }

    // Find a CN_Cafe by ID
    public CN_Cafe findCafeById(String cafeId) {
        return cafeDao.findById(cafeId);
    }

    // Find all CN_Cafes
    public List<CN_Cafe> findAllCafes() {
        return cafeDao.findAll();
    }
}
