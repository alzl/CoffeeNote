package login.service;

import login.dao.CN_UserDao;
import login.model.CN_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.UUID; //이 망할 거 안쓰고 말아 그냥

@Service
public class CN_UserService {

    @Autowired
    private CN_UserDao cnUserDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create a new CN_User
    public void createUser(CN_User user) {
        // 비밀번호를 암호화한 후 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 데이터베이스에 사용자 정보 삽입
        cnUserDao.insert(user);
    }

    // Update an existing CN_User
    public void updateUser(CN_User user) {
    	//이미 암호화된 비밀번호는 아닌지 확인
        CN_User existingUser = cnUserDao.findById(user.getUserId());
        if (!user.getPassword().equals(existingUser.getPassword())) {
            // 비밀번호를 암호화한 후 저장
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        cnUserDao.update(user);
    }

    // Delete a CN_User by ID
    public void deleteUser(String userId) {
        cnUserDao.delete(userId);
    }

    // Find a CN_User by ID
    public CN_User findUserById(String userId) {
        return cnUserDao.findById(userId);
    }

    // Find all CN_Users
    public List<CN_User> findAllUsers() {
        return cnUserDao.findAll();
    }

    // id 중복 검사
    public boolean idExists(String id) {
        return cnUserDao.idExists(id);
    }
    
    // 이메일 중복 검사 (기존)
    public boolean emailExists(String email) {
        return cnUserDao.emailExists(email);
    } 
    
    // 사용자 인증
    public boolean authenticate(String username, String password) {
        CN_User user = cnUserDao.findById(username);
        if (user == null) {
            return false; // 사용자 존재하지 않음
        }
        return passwordEncoder.matches(password, user.getPassword());
    }
    //0827
	public boolean setUserName(String userId, String userName) {
		return cnUserDao.setUserName(userId, userName);
	}
	// 이메일 변경
    public boolean setUserEmail(String userId, String userEmail) {
        // 이메일 중복 확인
        if (emailExists(userEmail)) {
            return false;
        }

        // 이메일 업데이트 로직
        CN_User user = cnUserDao.findById(userId);
        if (user != null) {
            user.setEmail(userEmail);
            cnUserDao.update(user);
            return true;
        }
        return false;
    }
    
    //0923 추가 이메일로 사용자 찾기
    public CN_User findUserByEmail(String email) {
        return cnUserDao.findByEmail(email);
    }

}

