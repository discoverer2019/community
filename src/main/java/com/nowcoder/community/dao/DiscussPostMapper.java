package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 根据用户id，查询用户得帖子列表
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // 查询用户id为xxx得，帖子得数量
    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if> 里使用，必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    // 通过帖子的id查找帖子
    DiscussPost selectDiscussPostById(int id);

    // 插入一条帖子数据
    int insertDiscussPost(DiscussPost discussPost);

    // 更新帖子评论数量
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}
