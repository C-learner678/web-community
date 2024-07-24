package com.jlu.webcommunity.service;

public interface NotifyMessageService {
    void notifyAddUserFollow(Long userId, Long followUserId);
    void notifyAddLikePost(Long fromUserId, Long postId);
    void notifyAddComment(Long fromUserId, Long postId, String comment);
    void notifyBanUser(Long userId);
    void notifyCancelBanUser(Long userId);
    void notifyAddLikeComment(Long fromUserId, Long commentId);
    void notifyDeletePost(Long userId, Long postId);
    void notifyDeleteComment(Long userId, Long commentId);
}
