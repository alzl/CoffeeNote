package bookmark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bookmark.dao.CN_BookmarkDao;
import bookmark.model.CN_Bookmark;

import java.util.List;

@Service
public class CN_BookmarkService {
    @Autowired
    private CN_BookmarkDao bookmarkDao;

    public void createBookmark(CN_Bookmark bookmark) {
        bookmarkDao.insert(bookmark);
    }

    public void updateBookmark(CN_Bookmark bookmark) {
        bookmarkDao.update(bookmark);
    }

    public void deleteBookmark(int bookmarkId) {
        bookmarkDao.delete(bookmarkId);
    }

    public CN_Bookmark findBookmarkById(int bookmarkId) {
        return bookmarkDao.findById(bookmarkId);
    }

    public List<CN_Bookmark> findAllBookmarks() {
        return bookmarkDao.findAll();
    }
    
    // 추가: 주어진 사용자 ID에 대한 가장 최근에 생성된 북마크 가져오기
    public CN_Bookmark findLatestBookmarkByUserId(String userId) {
        return bookmarkDao.findLatestBookmarkByUserId(userId);
    }
    
    // 추가: 공개된 CN_Bookmarks 찾기
    public List<CN_Bookmark> findPublicBookmarks() {
        return bookmarkDao.findPublicBookmarks();
    }
    
    //0903추가
    public List<CN_Bookmark> findBookmarksByUserId(String userId) {
        return bookmarkDao.findByUserId(userId);
    }
    
    //0910추가 - 북마크 제목 검색
    public List<CN_Bookmark> searchBookmarksByTitle(String keyword) {
        return bookmarkDao.searchByTitle(keyword);
    }

}
