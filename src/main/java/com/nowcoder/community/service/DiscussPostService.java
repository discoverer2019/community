package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }
    // 注意：sql如果直接将关联结果查询到得话，那么使用redis做缓存得时候，要怎么考虑？整体缓存还是局部？

    // 这里因为我们要查询得结果中有userId，正常得情况下，我们要将数据中得逻辑外键转化为对应得username
    // 有两种方式：1.写SQL得时候，关联查询用户表，查出用户名
    //           2.将discussPosts列表查询出之后，单独查询discussPost和user，然后将两者组合到一起返回给页面
    //  第二中方式，当使用redis得时候，缓存数据比较方便，性能比较高


    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
