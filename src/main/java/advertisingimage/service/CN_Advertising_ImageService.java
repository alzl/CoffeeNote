package advertisingimage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import advertisingimage.dao.CN_Advertising_ImageDao;
import advertisingimage.model.CN_Advertising_Image;

import java.util.List;

@Service
public class CN_Advertising_ImageService {
    @Autowired
    private CN_Advertising_ImageDao imageDao;

    public void createImage(CN_Advertising_Image image) {
        imageDao.insert(image);
    }

    public void updateImage(CN_Advertising_Image image) {
        imageDao.update(image);
    }

    public void deleteImage(int advertisingImageId) {
        imageDao.delete(advertisingImageId);
    }

    public CN_Advertising_Image findImageById(int advertisingImageId) {
        return imageDao.findById(advertisingImageId);
    }

    public List<CN_Advertising_Image> findAllImages() {
        return imageDao.findAll();
    }
    
    //0902추가 - 홍보id에 해당하는 홍보이미지 가져오기
    public List<CN_Advertising_Image> findImagesByAdvertisingId(int advertisingId) {
        return imageDao.findImagesByAdvertisingId(advertisingId);
    }
    //0924추가 - 해당 카페의 모든 광고이미지를 리스트로 반환
    public List<CN_Advertising_Image> findImagesByCafeId(String cafeId) {
        return imageDao.findImagesByCafeId(cafeId);
    }
    
}
