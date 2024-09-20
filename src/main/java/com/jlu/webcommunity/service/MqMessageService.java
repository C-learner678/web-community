package com.jlu.webcommunity.service;

public interface MqMessageService {
    void notifyAddUserFollow(Long userId, Long followUserId);
    void notifyAddLikePost(Long fromUserId, Long postId);
    void notifyAddComment(Long fromUserId, Long postId, Long commentId);
    void notifyBanUser(Long userId);
    void notifyCancelBanUser(Long userId);
    void notifyAddLikeComment(Long fromUserId, Long commentId);
    void notifyDeletePost(Long userId, Long postId);
    void notifyDeleteComment(Long userId, Long postId, Long commentId);
}
