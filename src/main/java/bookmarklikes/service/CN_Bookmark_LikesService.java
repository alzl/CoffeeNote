package bookmarklikes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bookmark.model.CN_Bookmark;
import bookmarklikes.dao.CN_Bookmark_LikesDao;
import bookmarklikes.model.CN_Bookmark_Likes;

import java.util.List;

@Service
public class CN_Bookmark_LikesService {
    @Autowired
    private CN_Bookmark_LikesDao bookmarkLikesDao;

    public void createBookmarkLike(CN_Bookmark_Likes bookmarkLike) {
        bookmarkLikesDao.insert(bookmarkLike);
    }

    public void deleteBookmarkLike(int bookmarkLikeId) {
        bookmarkLikesDao.delete(bookmarkLikeId);
    }

    public CN_Bookmark_Likes findBookmarkLikeById(int bookmarkLikeId) {
        return bookmarkLikesDao.findById(bookmarkLikeId);
    }

    public List<CN_Bookmark_Likes> findAllBookmarkLikes() {
        return bookmarkLikesDao.findAll();
    }
    
    public CN_Bookmark_Likes findByUserIdAndBookmarkId(String userId, int bookmarkId) {
        return bookmarkLikesDao.findByUserIdAndBookmarkId(userId, bookmarkId);
    }
    //0903추가
    // 추가: 사용자가 좋아요한 북마크 목록 가져오기
    public List<CN_Bookmark> findBookmarksLikedByUserId(String userId) {
        return bookmarkLikesDao.findBookmarksLikedByUserId(userId);
    }
    public void deleteLikesByBookmarkId(int bookmarkId) {
        bookmarkLikesDao.deleteByBookmarkId(bookmarkId);
    }

}
