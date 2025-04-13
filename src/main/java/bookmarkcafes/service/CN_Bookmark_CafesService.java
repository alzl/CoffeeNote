package bookmarkcafes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bookmarkcafes.dao.CN_Bookmark_CafesDao;
import bookmarkcafes.model.CN_Bookmark_Cafes;

import java.util.List;

@Service
public class CN_Bookmark_CafesService {
    @Autowired
    private CN_Bookmark_CafesDao bookmarkCafesDao;

    public void createBookmarkCafes(CN_Bookmark_Cafes bookmarkCafes) {
        bookmarkCafesDao.insert(bookmarkCafes);
    }

    public void updateBookmarkCafes(CN_Bookmark_Cafes bookmarkCafes) {
        bookmarkCafesDao.update(bookmarkCafes);
    }

    public void deleteBookmarkCafes(int bookmarkCafesId) {
        bookmarkCafesDao.delete(bookmarkCafesId);
    }

    public CN_Bookmark_Cafes findBookmarkCafesById(int bookmarkCafesId) {
        return bookmarkCafesDao.findById(bookmarkCafesId);
    }

    public List<CN_Bookmark_Cafes> findAllBookmarkCafes() {
        return bookmarkCafesDao.findAll();
    }
    
    public List<CN_Bookmark_Cafes> findCafesByBookmarkId(int bookmarkId) {
        return bookmarkCafesDao.findByBookmarkId(bookmarkId);
    }
    //0903추가
    public void deleteCafesByBookmarkId(int bookmarkId) {
        bookmarkCafesDao.deleteByBookmarkId(bookmarkId);
    }

}
