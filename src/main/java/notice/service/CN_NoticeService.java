package notice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import notice.dao.CN_NoticeDao;
import notice.model.CN_Notice;

import java.util.List;

@Service
public class CN_NoticeService {

    @Autowired
    private CN_NoticeDao noticeDao;

    // Create a new CN_Notice
    public void createNotice(CN_Notice notice) {
        noticeDao.insert(notice);
    }

    // Update an existing CN_Notice
    public void updateNotice(CN_Notice notice) {
        noticeDao.update(notice);
    }

    // Delete a CN_Notice by ID
    public void deleteNotice(int noticeId) {
        noticeDao.delete(noticeId);
    }

    // Find a CN_Notice by ID
    public CN_Notice findNoticeById(int noticeId) {
        return noticeDao.findById(noticeId);
    }

    // Find all CN_Notices
    public List<CN_Notice> findAllNotices() {
        return noticeDao.findAll();
    }
}
