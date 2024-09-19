package com.jlu.webcommunity.service.impl;

import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.dao.CommentDao;
import com.jlu.webcommunity.dao.NotifyMessageDao;
import com.jlu.webcommunity.dao.PostDao;
import com.jlu.webcommunity.entity.NotifyMessage;
import com.jlu.webcommunity.service.NotifyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

// 这个类用来消化消息队列里的消息
@Slf4j
@Service
public class NotifyMessageServiceImpl implements NotifyMessageService {
    @Autowired
    private NotifyMessageDao notifyMessageDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    public void notifyAddUserFollow(Long userId, Long followUserId){
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setFromUserId(followUserId);
        notifyMessage.setType(MessageTypeConstant.ADD_USER_FOLLOW);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyAddLikePost(Long fromUserId, Long postId) {
        Long userId = postDao.getById(postId).getUserId();
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setFromUserId(fromUserId);
        notifyMessage.setPostId(postId);
        notifyMessage.setType(MessageTypeConstant.ADD_LIKE_POST);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyAddComment(Long fromUserId, Long postId, String comment) {
        Long userId = postDao.getById(postId).getUserId();
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setFromUserId(fromUserId);
        notifyMessage.setPostId(postId);
        notifyMessage.setType(MessageTypeConstant.ADD_COMMENT);
        notifyMessage.setMessage(comment);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyBanUser(Long userId) {
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setType(MessageTypeConstant.BAN_USER);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyCancelBanUser(Long userId) {
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setType(MessageTypeConstant.CANCEL_BAN_USER);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyAddLikeComment(Long fromUserId, Long commentId) {
        Long userId = commentDao.getById(commentId).getUserId();
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setFromUserId(fromUserId);
        notifyMessage.setCommentId(commentId);
        notifyMessage.setType(MessageTypeConstant.ADD_LIKE_COMMENT);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyDeletePost(Long userId, Long postId) {
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setPostId(postId);
        notifyMessage.setType(MessageTypeConstant.DELETE_POST);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }

    @Override
    public void notifyDeleteComment(Long userId, Long commentId) {
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUserId(userId);
        notifyMessage.setCommentId(commentId);
        notifyMessage.setType(MessageTypeConstant.DELETE_COMMENT);
        notifyMessage.setCreateTime(new Date());
        notifyMessage.setUpdateTime(new Date());
        notifyMessageDao.save(notifyMessage);
    }
}
