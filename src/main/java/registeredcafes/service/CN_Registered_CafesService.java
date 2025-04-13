package registeredcafes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import registeredcafes.dao.CN_Registered_CafesDao;
import registeredcafes.model.CN_Registered_Cafes;
import login.model.CN_User;
import login.service.CN_UserService; // CN_UserService import 추가

import java.util.List;

@Service
public class CN_Registered_CafesService {

    @Autowired
    private CN_Registered_CafesDao registeredCafesDao;

    @Autowired
    private CN_UserService userService; // CN_UserService 주입

    // Create a new CN_Registered_Cafes
    public void createRegisteredCafes(CN_Registered_Cafes registeredCafes) {
        registeredCafesDao.insert(registeredCafes);

        // 사업장을 추가한 사용자의 권한을 'USER'에서 'BUSINESS'로 변경
        registerUserAsBusiness(registeredCafes.getUserId());
    }

    // 사업장을 등록한 사용자의 권한을 'BUSINESS'로 업데이트하는 메서드
    private void registerUserAsBusiness(String userId) {
    	CN_User user = userService.findUserById(userId);
    	System.out.println("usertype?"+user.getUserType());
    	if ("USER".equals(user.getUserType())) { // 권한이 user라면 변환
    	    System.out.println(userId + " 권한이 user에서 business로 변경됨");
    	    user.setUserType("BUSINESS");
    	    userService.updateUser(user);
    	} else {
    	    System.out.println(userId + "는 이미 권한이 business");
    	}
    }

    // Update an existing CN_Registered_Cafes
    public void updateRegisteredCafes(CN_Registered_Cafes registeredCafes) {
        registeredCafesDao.update(registeredCafes);
    }

    // Delete a CN_Registered_Cafes by ID
    public void deleteRegisteredCafes(int registeredId) {
        registeredCafesDao.delete(registeredId);
    }

    // Find a CN_Registered_Cafes by ID
    public CN_Registered_Cafes findRegisteredCafesById(int registeredId) {
        return registeredCafesDao.findByRegisteredId(registeredId);
    }

    // Find all CN_Registered_Cafes
    public List<CN_Registered_Cafes> findAllRegisteredCafes() {
        return registeredCafesDao.findAll();
    }

    // 특정 사용자 ID로 등록된 모든 카페 찾기
    public List<CN_Registered_Cafes> findAllRegisteredCafesByUserId(String userId) {
        return registeredCafesDao.findAllByUserId(userId);
    }

    // 카페 ID로 등록된 사업장을 찾는 메서드
    public CN_Registered_Cafes findRegisteredCafeByCafeId(String cafeId) {
        return registeredCafesDao.findByCafeId(cafeId);
    }

    // businessRegNum으로 중복 등록 확인 메서드 추가
    public boolean isBusinessRegNumRegistered(int businessRegNum) {
        return findRegisteredCafesById(businessRegNum) != null;
    }
}
