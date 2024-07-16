package com.jlu.webcommunity.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.dao.PostDao;
import com.jlu.webcommunity.dao.SectionDao;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.dto.post.*;
import com.jlu.webcommunity.entity.vo.SelectPostByPageVo;
import com.jlu.webcommunity.filter.context.UserContext;
import com.jlu.webcommunity.service.PostService;
import com.jlu.webcommunity.util.PageParam;
import com.jlu.webcommunity.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Post getPost(Long id) {
        Post post = getPostWithLock(id);
        if(post == null || post.getDeleted()){
            redisUtil.set(RedisConstant.postContentKey + id, null, 1800);
            return null;
        }
        redisUtil.set(RedisConstant.postContentKey + id, post, 1800);
        return post;
    }

    // 加锁获取数据库中的文章内容，减少数据库访问量
    private Post getPostWithLock(Long postId){
        if(redisUtil.hasKey(RedisConstant.postContentKey + postId)){
            return (Post) redisUtil.get(RedisConstant.postContentKey + postId);
        }
        String key = RedisConstant.postLockKey + postId;
        String value = RandomUtil.randomString(6); // 每个线程设置自身的value，防止删掉其他线程设置的锁
        // 上锁
        Boolean isLockSuccess = redisUtil.setIfAbsent(key, value, 60);
        Post post = null;
        try {
            if (isLockSuccess) {
                post = postDao.getById(postId);
            } else {
                Thread.sleep(200);
                post = getPostWithLock(postId);
            }
        } catch (InterruptedException e) {
            log.error(e.toString());
        } finally {
            /*
                String lockValue = (String) redisUtil.get(key);
                if (lockValue != null && lockValue.equals(value)) {
                    // 解锁
                    redisUtil.del(key);
                }
            */
            redisUtil.delByLua(key, value); // Lua脚本实现原子化
        }
        return post;
    }

    @Override
    public Long addPost(AddPostDto addPostDto) {
        Post post = new Post();
        Long sectionId = addPostDto.getSectionId();
        if(sectionDao.getById(sectionId) == null){
            return -1L; //没找到版块
        }
        post.setSectionId(sectionId);
        Long userId = UserContext.getUserData().getId();
        post.setUserId(userId);
        post.setTitle(addPostDto.getTitle());
        post.setContent(addPostDto.getContent());
        post.setDeleted(false);
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        if(postDao.save(post)){
            return post.getId(); //返回自增主键（大于0）
        };
        return 0L;
    }

    @Override
    public boolean modifyPost(ModifyPostDto modifyPostDto) {
        Post post = postDao.getById(modifyPostDto.getPostId());
        if(post == null){
            return false;
        }
        if(!Objects.equals(post.getUserId(), UserContext.getUserData().getId())){
            return false;
        }
        post.setTitle(modifyPostDto.getTitle());
        post.setContent(modifyPostDto.getContent());
        post.setUpdateTime(new Date());
        postDao.updateById(post);
        redisUtil.del(RedisConstant.postContentKey + post.getId());
        return true;
    }

    @Override
    public boolean deletePost(Long id) {
        Post post = postDao.getById(id);
        if(post == null){
            return false;
        }
        if(!Objects.equals(post.getUserId(), UserContext.getUserData().getId())){
            return false;
        }
        post.setDeleted(true);
        post.setUpdateTime(new Date());
        postDao.updateById(post);
        redisUtil.del(RedisConstant.postContentKey + post.getId());
        return true;
    }

    @Override
    public List<SelectPostByPageVo> getPostByPage(GetPostByPageDto dto) {
        List<SelectPostByPageVo> posts = postDao.getBaseMapper().selectPostByPage(
                dto.getSectionId(), dto.getUserId(), dto.getTop(),
                PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
        return posts;
    }

    @Override
    public Integer getPostCount(GetPostCountDto dto) {
        int count = postDao.getBaseMapper().selectPostCount(dto.getSectionId(), dto.getUserId(), dto.getTop());
        return count;
    }

    @Override
    public boolean modifyPostTop(ModifyPostTopDto dto) {
        Post post = postDao.getById(dto.getPostId());
        if(post != null) {
            post.setTop(dto.getTop());
            post.setUpdateTime(new Date());
            postDao.updateById(post);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean adminDeletePost(Long id) {
        Post post = postDao.getById(id);
        if(post != null){
            post.setDeleted(true);
            post.setUpdateTime(new Date());
            postDao.updateById(post);
            return true;
        }
        return false;
    }
}
